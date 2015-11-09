package com.dreamspace.uucampusseller.ui.fragment.Goods;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.Goods.CatalogListAdapter;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.model.GetGroupsRes;
import com.dreamspace.uucampusseller.model.GroupCreateReq;
import com.dreamspace.uucampusseller.model.GroupCreateRes;
import com.dreamspace.uucampusseller.model.GroupDeleteReq;
import com.dreamspace.uucampusseller.model.GroupDeleteRes;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.ui.dialog.AddNewInfoDialog;
import com.dreamspace.uucampusseller.ui.dialog.MsgDialog;
import com.dreamspace.uucampusseller.ui.dialog.ProgressDialog;


import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lx on 2015/10/20.
 */
public class GoodsClassifyFragment extends BaseLazyFragment {
    @Bind(R.id.catalog_lv)
    ListView listView;
    @Bind(R.id.new_edit_catalog_ll)
    LinearLayout newEditCataLl;
    @Bind(R.id.finish_edit_btn)
    Button finishEditBtn;
    @Bind(R.id.new_catalog_btn)
    Button newCatalogBtn;
    @Bind(R.id.edit_catalog_btn)
    Button editCatalogBtn;
    @Bind(R.id.category_content_rl)
    RelativeLayout contentRl;
    @Bind(R.id.swiperefresh_layout)
    SwipeRefreshLayout listSrl;

    private CatalogListAdapter catalogAdapter;
    private ProgressDialog progressDialog;
    private AddNewInfoDialog editGroupDialog;
    private AddNewInfoDialog createGroupDialog;
    private MsgDialog deleteGroupDialog;
    private boolean fragmentDestory = false;
    private boolean firstGetGroups = true;
    private String oldName;
    private String deleteName;

    @Override
    protected void onFirstUserVisible() {
        getGroups();
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return contentRl;
    }

    @Override
    protected void initViewsAndEvents() {
        initCatalogViews();
        initListeners();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_goods_classify;
    }

    private void initCatalogViews(){
        newEditCataLl.setVisibility(View.VISIBLE);
        finishEditBtn.setVisibility(View.INVISIBLE);
    }

    private void initListeners(){
        editCatalogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogAdapter.showEdit(true);
            }
        });

        newCatalogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCreateGroupDialog();
                createGroupDialog.show();
            }
        });

        listSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firstGetGroups = true;
                getGroups();
                listSrl.setRefreshing(false);
            }
        });
    }

    //获取分类信息
    private void getGroups(){
        if(firstGetGroups){
            toggleShowLoading(true, null);
        }
        if(!NetUtils.isNetworkConnected(mContext)){
            showNetWorkError();
            if(firstGetGroups){
                toggleNetworkError(true, getGroupsClickListener);
            }
            return;
        }

        ApiManager.getService(mContext).getGroups(PreferenceUtils.getString(mContext, PreferenceUtils.Key.SHOP_ID)
                , new Callback<GetGroupsRes>() {
            @Override
            public void success(GetGroupsRes getGroupsRes, Response response) {
                if (getGroupsRes != null && !fragmentDestory) {
                    catalogAdapter = new CatalogListAdapter(mContext, getGroupsRes.getGroup(),
                            CatalogListAdapter.ViewHolder.class);

                    catalogAdapter.setDeleteClickListener(new CatalogListAdapter.OnGroupDeleteClickListener() {
                        @Override
                        public void onGoodDeleteClick(String groupName) {
                            //删除分类
                            deleteName = groupName;
                            initDeleteGroupMsgDialog();
                            deleteGroupDialog.show();
                        }
                    });

                    catalogAdapter.setEditClickListener(new CatalogListAdapter.OnGroupEditClickListener() {
                        @Override
                        public void onEditClick(String groupName) {
                            //编辑分类,编辑操作为先删除此分类再创建一个新的分类
                            initEditGroupDialog();
                            editGroupDialog.setText(groupName);
                            oldName = groupName;
                            editGroupDialog.show();
                        }
                    });

                    listView.setAdapter(catalogAdapter);
                    if (firstGetGroups) {
                        toggleRestore();
                        firstGetGroups = false;
                    } else {
                        showToast(getString(R.string.success));
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showInnerError(error);
                if (firstGetGroups) {
                    toggleShowEmpty(true, null, getGroupsClickListener);
                }
            }
        });
    }

    //删除分类,若edit为true这是编辑分类，需先删除旧的group。若为false则是直接删除分类
    private void deleteGroup(String groupName, final boolean edit, final String newContent){
        if(!NetUtils.isNetworkConnected(mContext)){
            showNetWorkError();
            progressDialog.dismiss();
            return;
        }

        GroupDeleteReq deleteReq = new GroupDeleteReq();
        deleteReq.setName(groupName);

        ApiManager.getService(mContext).deleteGroup(PreferenceUtils.getString(mContext, PreferenceUtils.Key.SHOP_ID),
                deleteReq, new Callback<GroupDeleteRes>() {
                    @Override
                    public void success(GroupDeleteRes groupDeleteRes, Response response) {
                        if (groupDeleteRes != null && !fragmentDestory) {
                            if (!edit) {
                                progressDialog.dismiss();
                                getGroups();
                                showToast(getString(R.string.group_delete_success));
                            } else {
                                createGroup(newContent);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
//                        showInnerError(error);
                        System.out.println(error.getMessage());
                    }
                });
    }

    //创建分类
    private void createGroup(String groupName){
        if(!NetUtils.isNetworkConnected(mContext)){
            showNetWorkError();
            progressDialog.dismiss();
            return;
        }

        GroupCreateReq groupCreateReq = new GroupCreateReq();
        groupCreateReq.setName(groupName);
        ApiManager.getService(mContext).createGroup(PreferenceUtils.getString(mContext, PreferenceUtils.Key.SHOP_ID),
                groupCreateReq, new Callback<GroupCreateRes>() {
                    @Override
                    public void success(GroupCreateRes groupCreateRes, Response response) {
                        if (groupCreateRes != null && !fragmentDestory) {
                            getGroups();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showInnerError(error);
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onDestroy() {
        fragmentDestory = true;
        super.onDestroy();
    }

    private void initProgressDialog(){
        if(progressDialog != null){
            return;
        }
        progressDialog = new ProgressDialog(mContext);
    }

    //初始化编辑分类对话框
    private void initEditGroupDialog(){
        if(editGroupDialog != null){
            return;
        }
        editGroupDialog = new AddNewInfoDialog(mContext);
        editGroupDialog.setTitle(getString(R.string.modify_group));
        editGroupDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGroupDialog.dismiss();
            }
        });

        editGroupDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newContent = editGroupDialog.getText();
                if(CommonUtils.isEmpty(newContent)){
                    showToast(getString(R.string.group_cant_be_null));
                }else{
                    initProgressDialog();
                    progressDialog.setContent(getString(R.string.in_modify));
                    editGroupDialog.dismiss();
                    progressDialog.show();
                    deleteGroup(oldName,true,newContent);
                }
            }
        });
    }

    //初始化新建分类对话框
    private void initCreateGroupDialog(){
        if(createGroupDialog != null){
            return;
        }
        createGroupDialog = new AddNewInfoDialog(mContext);
        createGroupDialog.setTitle(getString(R.string.create_group));
        createGroupDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroupDialog.dismiss();
            }
        });

        createGroupDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.isEmpty(createGroupDialog.getText())){
                    showToast(getString(R.string.group_cant_be_null));
                }else{
                    initProgressDialog();
                    progressDialog.setContent(getString(R.string.in_create));
                    createGroupDialog.dismiss();
                    progressDialog.show();
                    createGroup(createGroupDialog.getText());
                }
            }
        });
    }

    private void initDeleteGroupMsgDialog(){
        if(deleteGroupDialog != null){
            return;
        }
        deleteGroupDialog = new MsgDialog(mContext);
        deleteGroupDialog.setContent(getString(R.string.group_delete_confirm));
        deleteGroupDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroupDialog.dismiss();
            }
        });

        deleteGroupDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroupDialog.dismiss();
                initProgressDialog();
                progressDialog.setContent(getString(R.string.in_delete));
                progressDialog.show();
                deleteGroup(deleteName,false,null);
            }
        });
    }

    private View.OnClickListener getGroupsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getGroups();
        }
    };
}
