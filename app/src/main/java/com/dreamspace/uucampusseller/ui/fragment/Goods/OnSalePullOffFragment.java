package com.dreamspace.uucampusseller.ui.fragment.Goods;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;


import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.Goods.OnSaleListAdapter;
import com.dreamspace.uucampusseller.adapter.Goods.PullOffListAdapter;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.model.CommonStatusRes;
import com.dreamspace.uucampusseller.model.CreateGoodReq;
import com.dreamspace.uucampusseller.model.GetSelfGoodsRes;
import com.dreamspace.uucampusseller.model.UpdateGoodReq;
import com.dreamspace.uucampusseller.ui.GoodsFragment;
import com.dreamspace.uucampusseller.ui.activity.Goods.EditGoodInfoAct;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.ui.dialog.ProgressDialog;
import com.dreamspace.uucampusseller.widget.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lx on 2015/9/23.
 */
public class OnSalePullOffFragment extends BaseLazyFragment {
    @Bind(R.id.my_goods_sale_pulloff_lv)
    LoadMoreListView listView;
    @Bind(R.id.swiperefresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;

    private String type;
    private boolean firstGetData = true;
    private int goodPage = 1;
    private int is_active = -1;
    private OnSaleListAdapter onSaleListAdapter;
    private PullOffListAdapter pullOffListAdapter;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle == null? "null":bundle.getString(GoodsFragment.MANAGEMENT_TYPE);
        if(type.equals(getResources().getString(R.string.on_sale))){
            is_active = 1;//当前界面要获取的商品都是上架商品
        }else if(type.equals(getResources().getString(R.string.already_pull_off))){
            is_active = 0;//获取的都是下架商品
        }
    }

    @Override
    protected void onFirstUserVisible() {
        getSelfGoods();
    }

    @Override
    protected void onUserVisible() {
        //防止出现第一次进入没有商品，之后下架或上架商品再次返回界面任然看上去没有商品的结果
        if((is_active == 1 && onSaleListAdapter != null && onSaleListAdapter.getmEntities().size() == 0)
                || (is_active == 0 && pullOffListAdapter != null && pullOffListAdapter.getmEntities().size() == 0)
                || (is_active == 1 && onSaleListAdapter == null)
                || (is_active == 0 && pullOffListAdapter == null)){
            firstGetData = true;
            goodPage = 1;
            getSelfGoods();
        }
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return contentLl;
    }

    @Override
    protected void initViewsAndEvents() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_theme_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                goodPage = 1;
                firstGetData = true;
                getSelfGoods();
            }
        });

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                goodPage++;
                getSelfGoods();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_my_goods_onsale_pulloff;
    }

    //获取自己的商品列表
    private void getSelfGoods(){
        if(firstGetData){
            toggleShowLoading(true,null);
        }

        if(!NetUtils.isNetworkConnected(mContext)){
            showNetWorkError();
            if(firstGetData){
                toggleNetworkError(true,getGoodsClickListener);
            }
            listView.setLoading(false);
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        ApiManager.getService(mContext).getSelfGoods(goodPage, is_active, null, new Callback<GetSelfGoodsRes>() {
            @Override
            public void success(GetSelfGoodsRes getSelfGoodsRes, Response response) {
                if(getSelfGoodsRes != null){
                    listView.setLoading(false);
                    swipeRefreshLayout.setRefreshing(false);

                    if(goodPage == 1 && getSelfGoodsRes.getGoods().size() == 0){
                        toggleShowEmpty(true,getString(R.string.no_good),null);
                        return;
                    }

                    if(goodPage != 1 && getSelfGoodsRes.getGoods().size() == 0){
                        showToast(getString(R.string.no_more));
                        return;
                    }

                    if(is_active == 1){
                        if(firstGetData){
                            onSaleListAdapter = new OnSaleListAdapter(mContext,getSelfGoodsRes.getGoods(),OnSaleListAdapter.ViewHolder.class);
                            listView.setAdapter(onSaleListAdapter);
                            onSaleListAdapter.setOnGoodPullOffClickListener(new OnSaleListAdapter.OnGoodPullOffClickListener() {
                                @Override
                                public void onPullOffClick(String good_id, int position) {
                                    initProgressDialog();
                                    progressDialog.setContent(mContext.getString(R.string.in_pull_off));
                                    progressDialog.show();
                                    updateGoodInfo(good_id, 0, position);
                                }
                            });

                            onSaleListAdapter.setOnGoodEditClickListener(new OnSaleListAdapter.OnGoodEditClickListener() {
                                @Override
                                public void onEditClick(String good_id) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(EditGoodInfoAct.IN_WAY,EditGoodInfoAct.IN_FROM_EDIT);
                                    bundle.putString(EditGoodInfoAct.GOOD_ID, good_id);
                                    readyGo(EditGoodInfoAct.class,bundle);
                                }
                            });
                            toggleRestore();
                            firstGetData = false;
                        }else{
                            onSaleListAdapter.addEntities(getSelfGoodsRes.getGoods());
                            onSaleListAdapter.notifyDataSetChanged();
                        }
                    }else{
                        if(firstGetData){
                            pullOffListAdapter = new PullOffListAdapter(mContext,getSelfGoodsRes.getGoods(),PullOffListAdapter.ViewHolder.class);
                            listView.setAdapter(pullOffListAdapter);
                            pullOffListAdapter.setOnGoodEditClickListener(new PullOffListAdapter.OnGoodEditClickListener() {
                                @Override
                                public void onEditClick(String good_id) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(EditGoodInfoAct.IN_WAY,EditGoodInfoAct.IN_FROM_EDIT);
                                    bundle.putString(EditGoodInfoAct.GOOD_ID, good_id);
                                    readyGo(EditGoodInfoAct.class,bundle);
                                }
                            });

                            pullOffListAdapter.setOnGoodSaleClickListener(new PullOffListAdapter.OnGoodSaleClickListener() {
                                @Override
                                public void onSaleClick(String good_id, int position) {
                                    initProgressDialog();
                                    progressDialog.setContent(mContext.getString(R.string.in_pull_off));
                                    progressDialog.show();
                                    updateGoodInfo(good_id,1,position);
                                }
                            });
                            toggleRestore();
                            firstGetData = false;
                        }else{
                            pullOffListAdapter.addEntities(getSelfGoodsRes.getGoods());
                            pullOffListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(goodPage == 1){
                    toggleShowEmpty(true,null,getGoodsClickListener);
                }else{
                    showInnerError(error);
                }
                listView.setLoading(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateGoodInfo(String good_id, final int is_active, final int position){
        if(!NetUtils.isNetworkConnected(mContext)){
            progressDialog.dismiss();
            showNetWorkError();
            return;
        }

        UpdateGoodReq goodReq = new UpdateGoodReq();
        goodReq.setIs_active(is_active);
        ApiManager.getService(mContext).updateGoodInfo(good_id, goodReq, new Callback<CommonStatusRes>() {
            @Override
            public void success(CommonStatusRes commonStatusRes, Response response) {
                if (commonStatusRes != null) {
                    progressDialog.dismiss();
                    if (is_active == 0) {
                        onSaleListAdapter.removeItem(position);
                        showToast(getString(R.string.pull_off_success));
                    } else {
                        pullOffListAdapter.removeItem(position);
                        showToast(getString(R.string.on_sale_success));
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showInnerError(error);
            }
        });
    }
    private View.OnClickListener getGoodsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getSelfGoods();
        }
    };

    private void initProgressDialog(){
        if(progressDialog != null){
            return;
        }
        progressDialog = new ProgressDialog(mContext);
    }
}
