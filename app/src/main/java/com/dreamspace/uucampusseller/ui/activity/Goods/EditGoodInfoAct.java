package com.dreamspace.uucampusseller.ui.activity.Goods;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.model.CreateGoodReq;
import com.dreamspace.uucampusseller.model.CreateGoodRes;
import com.dreamspace.uucampusseller.model.GetGroupsRes;
import com.dreamspace.uucampusseller.model.GoodDetailRes;
import com.dreamspace.uucampusseller.model.GroupCreateReq;
import com.dreamspace.uucampusseller.model.GroupCreateRes;
import com.dreamspace.uucampusseller.model.LabelRes;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.ui.dialog.AddNewInfoDialog;
import com.dreamspace.uucampusseller.ui.dialog.GoodsClassifyWithAddDialog;
import com.dreamspace.uucampusseller.ui.dialog.MsgDialog;
import com.dreamspace.uucampusseller.ui.dialog.ProgressDialog;
import com.dreamspace.uucampusseller.widget.photopicker.SelectPhotoActivity;

import java.util.ArrayList;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lx on 2015/10/21.
 */
public class EditGoodInfoAct extends AbsActivity{
    @Bind(R.id.good_image_iv)
    ImageView goodImageIv;
    @Bind(R.id.good_name_et)
    EditText goodNameEt;
    @Bind(R.id.good_org_price_et)
    EditText goodOrgPriceEt;
    @Bind(R.id.yoyo_price_et)
    EditText yoyoPriceEt;
    @Bind(R.id.good_discount_et)
    EditText discountEt;
    @Bind(R.id.good_catalog_tv)
    TextView catalogTv;
    @Bind(R.id.good_key_word_tv)
    TextView goodKeywordTv;
    @Bind(R.id.keyword_ll)
    LinearLayout keywordLl;
    @Bind(R.id.good_delete_btn)
    Button goodDeleteBtn;

    private CreateGoodReq goodInfo = new CreateGoodReq();
    private String imagePath;
    private boolean actDestory = false;
    private LabelRes mLabels;
    private GetGroupsRes mGroups;
    private GoodsClassifyWithAddDialog labelDialog;
    private GoodsClassifyWithAddDialog groupDialog;
    private MsgDialog noLabelsMsgDialog;
    private MsgDialog noGroupsMsgDialog;
    private AddNewInfoDialog newGroupDialog;
    private ProgressDialog progressDialog;
    private boolean imageChange = false;//用来判断用户是否更换了图片
    private int inWay = 0;
    private String goodId;

    private static final int SELECT_IMAGE = 1;
    private static final int EDIT_LABEL = 2;
    public static final String PART_GOOD_INFO = "part_good_info";
    public static final int IN_FROM_CRETAE = 1;//创建新商品
    public static final int IN_FROM_EDIT = 2;//编辑商品
    public static final String IN_WAY = "IN_WAY";
    public static final String GOOD_ID = "GOOD_ID";

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_good_info;
    }

    @Override
    protected void prepareDatas() {
        getLabels();
        getGroups();
        Bundle data = getIntent().getExtras();
        inWay = data.getInt(IN_WAY);
        if(inWay == IN_FROM_EDIT){
            goodId = data.getString(GOOD_ID);
            getGoodDetail(goodId);
        }
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(R.string.add_good1);
        initListeners();
    }

    private void initListeners(){
        goodImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGoForResult(SelectPhotoActivity.class, SELECT_IMAGE);
            }
        });

        catalogTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGroups == null){
                    initNoGroupsDialog();
                    noGroupsMsgDialog.show();
                }else{
                    initGroupDialog();
                    groupDialog.show();
                }
            }
        });

        goodKeywordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLabels == null) {
                    initNoLabelsDialog();
                    noLabelsMsgDialog.show();
                } else {
                    initLabelDialog();
                    labelDialog.show();
                }
            }
        });

        goodDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_good_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_next_step){
            goEditGoodLabel();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_IMAGE){
            if(resultCode == RESULT_OK){
                imagePath = data.getStringExtra(SelectPhotoActivity.PHOTO_PATH);
                Glide.with(EditGoodInfoAct.this)
                        .load(imagePath)
                        .placeholder(R.drawable.default_error)
                        .centerCrop()
                        .into(goodImageIv);
                imageChange = true;//更换了图片
            }
        }else if(requestCode == EDIT_LABEL){
            if(resultCode == RESULT_CANCELED){
                //用户返回第一页
                Bundle bundle = data.getExtras();
                goodInfo = bundle.getParcelable(EditGoodLabelAct.GOOD_INFO);
            }else if(resultCode == RESULT_OK){
                //用户完成编辑
                finish();
            }
        }
    }

    private void goEditGoodLabel(){
        if(isValid()){
            if(imageChange){
                goodInfo.setImage(imagePath);
            }
            goodInfo.setName(goodNameEt.getText().toString());
            goodInfo.setOriginal_price(goodOrgPriceEt.getText().toString());
            goodInfo.setPrice(yoyoPriceEt.getText().toString());
            goodInfo.setDiscount(discountEt.getText().toString());
            goodInfo.setGroup(catalogTv.getText().toString());
            goodInfo.setLabel(goodKeywordTv.getText().toString());

            Bundle bundle = new Bundle();
            bundle.putInt(IN_WAY,inWay);
            bundle.putParcelable(PART_GOOD_INFO,goodInfo);
            if(inWay == IN_FROM_EDIT){
                bundle.putString(GOOD_ID,goodId);
            }
            readyGoForResult(EditGoodLabelAct.class, EDIT_LABEL, bundle);
        }
    }

    private boolean isValid(){
        if(imagePath == null){
            showToast(getString(R.string.plz_select_image));
            return false;
        }else if(CommonUtils.isEmpty(goodNameEt.getText().toString())){
            showToast(getString(R.string.plz_enter_good_name));
            return false;
        }else if(CommonUtils.isEmpty(goodOrgPriceEt.getText().toString())){
            showToast(getString(R.string.plz_enter_good_org_price));
            return false;
        }else if(CommonUtils.isEmpty(yoyoPriceEt.getText().toString())){
            showToast(getString(R.string.plz_enter_good_price));
            return false;
        }else if(CommonUtils.isEmpty(catalogTv.getText().toString())){
            showToast(getString(R.string.plz_choose_good_group));
            return false;
        }else if(CommonUtils.isEmpty(goodKeywordTv.getText().toString())){
            showToast(getString(R.string.plz_choose_good_label));
            return false;
        }
        return true;
    }

    //获取分类
    private void getGroups(){
        if(!NetUtils.isNetworkConnected(this)){
            showNetWorkError();
            return;
        }

        ApiManager.getService(this).getGroups(PreferenceUtils.getString(this, PreferenceUtils.Key.SHOP_ID),
                new Callback<GetGroupsRes>() {
                    @Override
                    public void success(GetGroupsRes getGroupsRes, Response response) {
                        if (getGroupsRes != null && !actDestory) {
                            mGroups = getGroupsRes;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showInnerError(error);
                    }
                });
    }

    private void getLabels(){
        if(!NetUtils.isNetworkConnected(this)){
            showNetWorkError();
            return;
        }

        ApiManager.getService(this).getLabels(PreferenceUtils.getString(this, PreferenceUtils.Key.CATEGORY)
                , new Callback<LabelRes>() {
            @Override
            public void success(LabelRes labelRes, Response response) {
                if (labelRes != null && !actDestory) {
                    mLabels = labelRes;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showInnerError(error);
            }
        });
    }

    private void initLabelDialog(){
        if(labelDialog != null){
            return;
        }
        labelDialog = new GoodsClassifyWithAddDialog(this);
        labelDialog.setTitle(getString(R.string.choose_key_word));
        labelDialog.setData(mLabels.getLabels());
        labelDialog.showAdd(false);
        labelDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodKeywordTv.setText(labelDialog.getSelected());
                labelDialog.dismiss();
            }
        });

        labelDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelDialog.dismiss();
            }
        });
    }

    private void initGroupDialog(){
        if(groupDialog != null){
            return;
        }
        groupDialog = new GoodsClassifyWithAddDialog(this);
        groupDialog.setTitle(getString(R.string.choose_good_group));
        groupDialog.setData(mGroups.getGroup());
        groupDialog.showAdd(true);
        groupDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogTv.setText(groupDialog.getSelected());
                groupDialog.dismiss();
            }
        });

        groupDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupDialog.dismiss();
            }
        });

        groupDialog.setAddButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewGroupDialog();
                groupDialog.dismiss();
                newGroupDialog.show();
            }
        });
    }

    private void initNoLabelsDialog(){
        if(noLabelsMsgDialog != null){
            return;
        }
        noLabelsMsgDialog = new MsgDialog(this);
        noLabelsMsgDialog.setContent(getString(R.string.get_label_failed));

        noLabelsMsgDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noLabelsMsgDialog.dismiss();
            }
        });

        noLabelsMsgDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLabels();
                noLabelsMsgDialog.dismiss();
            }
        });
    }

    private void initNoGroupsDialog(){
        if(noGroupsMsgDialog != null){
            return;
        }
        noGroupsMsgDialog = new MsgDialog(this);
        noGroupsMsgDialog.setContent(getString(R.string.get_group_failed));

        noGroupsMsgDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noGroupsMsgDialog.dismiss();
            }
        });

        noGroupsMsgDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGroups();
                noGroupsMsgDialog.dismiss();
            }
        });
    }

    private void initNewGroupDialog(){
        if(newGroupDialog != null){
            return;
        }
        newGroupDialog = new AddNewInfoDialog(this);
        newGroupDialog.setTitle(getString(R.string.add_new_catalog));
        newGroupDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGroupDialog.dismiss();
            }
        });

        newGroupDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initProgressDialog();
                progressDialog.setContent(getString(R.string.in_create));
                createGroup(newGroupDialog.getText());
                newGroupDialog.dismiss();
                progressDialog.show();
            }
        });
    }

    //创建分类
    private void createGroup(String groupName){
        if(!NetUtils.isNetworkConnected(this)){
            showNetWorkError();
            progressDialog.dismiss();
            return;
        }

        GroupCreateReq groupCreateReq = new GroupCreateReq();
        groupCreateReq.setName(groupName);
        ApiManager.getService(this).createGroup(PreferenceUtils.getString(this, PreferenceUtils.Key.SHOP_ID),
                groupCreateReq, new Callback<GroupCreateRes>() {
                    @Override
                    public void success(GroupCreateRes groupCreateRes, Response response) {
                        if (groupCreateRes != null && !actDestory) {
                            progressDialog.dismiss();
                            showToast(getString(R.string.create_success));
                            groupDialog = null;
                            getGroups();//获得最新的group
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showInnerError(error);
                        progressDialog.dismiss();
                    }
                });
    }

    private void getGoodDetail(String goods_id){
        if(!NetUtils.isNetworkConnected(this)){
            showNetWorkError();
            return;
        }

        ApiManager.getService(this).getGoodDetail(goods_id, new Callback<GoodDetailRes>() {
            @Override
            public void success(GoodDetailRes goodDetailRes, Response response) {
                if(goodDetailRes != null && !actDestory){
                    CommonUtils.showImageWithGlide(EditGoodInfoAct.this,goodImageIv,goodDetailRes.getImage());
                    goodOrgPriceEt.setText(goodDetailRes.getOriginal_price());
                    yoyoPriceEt.setText(goodDetailRes.getPrice());
                    discountEt.setText(goodDetailRes.getDiscount());
                    goodNameEt.setText(goodDetailRes.getName());

                    goodInfo.setDescription(goodDetailRes.getDescription());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showInnerError(error);
            }
        });
    }

    private void initProgressDialog(){
        if(progressDialog != null){
            return;
        }
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        actDestory = true;
        super.onDestroy();
    }
}
