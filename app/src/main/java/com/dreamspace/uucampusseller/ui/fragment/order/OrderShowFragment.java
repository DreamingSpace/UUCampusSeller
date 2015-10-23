package com.dreamspace.uucampusseller.ui.fragment.order;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.base.BasisAdapter;
import com.dreamspace.uucampusseller.adapter.base.order.OrderItemAdapter;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.model.OrderItem;
import com.dreamspace.uucampusseller.ui.activity.order.OrderDetailActivity;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.widget.LoadMoreListView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wufan on 2015/10/21.
 */
public class OrderShowFragment extends BaseLazyFragment {

    @Bind(R.id.order_load_more_lv)
    LoadMoreListView mMoreListView;
    @Bind(R.id.order_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int tabPosition=0;
    private BasisAdapter mAdapter;

    @Override
    protected void onFirstUserVisible() {
        tabPosition = FragmentPagerItem.getPosition(getArguments());
        Log.i("order tab:", SharedData.orderTabs[tabPosition]);
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
        return null;
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
                onItemPicked((OrderItem) mAdapter.getItem(position), position);
                Log.i("INFO", "position:  " + position);
                readyGo(OrderDetailActivity.class);
            }
        });
    }

    public void loadingInitData() {

//        ApiManager.getService(getActivity().getApplicationContext()).get
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (int i = 0; i < 10; i++) {
            OrderItem l = new OrderItem();
            l.setName("Hello");
            orderItems.add(l);
        }
        refreshDate(orderItems);
    }

    public void refreshDate(List<OrderItem> mEntities) {
        mAdapter.setmEntities(mEntities);
        mAdapter.notifyDataSetChanged();
    }

    public void onItemPicked(OrderItem mEntity, int position) {
        Log.i("INFO", mEntity.toString());
    }

    public void onPullUp() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("onLoad", "on load complete");
                onPullUpFinished();
            }
        }, 3000);
    }

    public void onPullDown() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onPullDownFinished();
            }
        }, 3000);
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

}
