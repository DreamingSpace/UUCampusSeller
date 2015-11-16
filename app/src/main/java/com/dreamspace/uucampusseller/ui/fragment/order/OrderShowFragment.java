package com.dreamspace.uucampusseller.ui.fragment.order;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.base.BasisAdapter;
import com.dreamspace.uucampusseller.adapter.base.order.OrderItemAdapter;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.TLog;
import com.dreamspace.uucampusseller.model.OrderItem;
import com.dreamspace.uucampusseller.model.api.GetShopOrderListRes;
import com.dreamspace.uucampusseller.ui.OnRefreshListener;
import com.dreamspace.uucampusseller.ui.activity.order.OrderDetailActivity;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.widget.LoadMoreListView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by wufan on 2015/10/21.
 */
public class OrderShowFragment extends BaseLazyFragment {

    @Bind(R.id.order_load_more_lv)
    LoadMoreListView mMoreListView;
    @Bind(R.id.order_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int tabPosition = 0;
    private BasisAdapter mAdapter;
    private int page = 1;
    private int status = 1;
    private String order_id = null;
    private boolean isFragDestroy = false;

    public static final int LOAD = 1;
    public static final int ADD = 2;


    @Override
    protected void onFirstUserVisible() {
        tabPosition = FragmentPagerItem.getPosition(getArguments());

        initStatus(tabPosition);
        loadingInitData();
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void initViewsAndEvents() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPullDown();
            }
        });
        mMoreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onPullUp();
            }
        });

        initDatas();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_order_show;
    }

    private void initDatas() {
        mAdapter = new OrderItemAdapter(getActivity());
        mMoreListView.setAdapter(mAdapter);
        mMoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                order_id = onItemPicked((OrderItem) mAdapter.getItem(position), position);
                TLog.i("INFO", "position:  " + position + " order_id:" + order_id);
                bundle.putInt(OrderDetailActivity.EXTRA_TAB_POSITION, tabPosition);
                bundle.putString(OrderDetailActivity.EXTRA_ORDER_ID, order_id);
                readyGo(OrderDetailActivity.class, bundle);
            }
        });
    }

    private void initStatus(int tabPosition) {
        switch (tabPosition) {
            case 0:            //未付款
                status = 1;
                break;
            case 1:           //未消费
                status = 2;
                break;
            case 2:           //已完成
                status = 3;
                break;
            case 3:           //退款
                status = 0;
                break;
        }
        Log.i("order tab:", SharedData.orderTabs[tabPosition] + " status:" + status);
    }

    public void loadingInitData() {

//        toggleShowLoading(true, getString(R.string.common_loading_message));
        if (NetUtils.isNetworkConnected(getActivity().getApplicationContext())) {
            ApiManager.getService(getActivity().getApplicationContext()).getShopOrderList(page, status, new Callback<GetShopOrderListRes>() {
                @Override
                public void success(GetShopOrderListRes getShopOrderListRes, Response response) {
                    if (!isFragDestroy) {
                            if(getShopOrderListRes.getOrders()==null){
//                                toggleShowEmpty(true, getString(R.string.common_empty_msg), null);
                            }else {
//                                toggleShowLoading(false,null);
                                refreshDate(getShopOrderListRes.getOrders(), LOAD);
                            }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });
        } else {
            showNetWorkError();
        }
    }

    public void refreshDate(List<OrderItem> mEntities, int type) {
        switch (type) {
            case LOAD:
                mAdapter.setmEntities(mEntities);
                break;
            case ADD:
                mAdapter.addEntities(mEntities);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    public String onItemPicked(OrderItem mEntity, int position) {
        Log.i("INFO", mEntity.get_id());
        return mEntity.get_id();
    }

    public void onPullUp() {  //上拉刷新，加载更多
        loadingDataByPageStatus(++page, status, new OnRefreshListener() {
            @Override
            public void onFinish(List mEntities) {
                refreshDate(mEntities, ADD);
                onPullUpFinished();
            }

            @Override
            public void onError() {
                onPullUpFinished();
            }
        });
    }

    public void onPullDown() {  //下拉刷新，加载最新的
        page = 1;
        loadingDataByPageStatus(page, status, new OnRefreshListener() {
            @Override
            public void onFinish(List mEntities) {
                refreshDate(mEntities, LOAD);
                onPullDownFinished();
            }

            @Override
            public void onError() {
                onPullDownFinished();
            }
        });
    }

    public void onPullUpFinished() {
        tabPosition = FragmentPagerItem.getPosition(getArguments());
        Log.i("Refresh tab:", SharedData.orderTabs[tabPosition]);
        mMoreListView.setLoading(false);
    }

    public void onPullDownFinished() {
        tabPosition = FragmentPagerItem.getPosition(getArguments());
        Log.i("Refresh tab:", SharedData.orderTabs[tabPosition]);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    void loadingDataByPageStatus(int page, int status, final OnRefreshListener onRefreshListener) {
        if (NetUtils.isNetworkConnected(getActivity().getApplicationContext())) {
            ApiManager.getService(getActivity().getApplicationContext()).getShopOrderList(page, status, new Callback<GetShopOrderListRes>() {
                @Override
                public void success(GetShopOrderListRes getShopOrderListRes, Response response) {
                    if (getShopOrderListRes != null) {
                        onRefreshListener.onFinish(getShopOrderListRes.getOrders());
                    } else {
                        showToast(response.getReason());
                        onRefreshListener.onError();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    onRefreshListener.onError();
                    showInnerError(error);
                }
            });
        } else {
            onRefreshListener.onError();
            showNetWorkError();
        }
    }

    public interface RefreshTab {
        public void refreshTab(int tabPosition, String tabNum);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragDestroy = true;
    }
}
