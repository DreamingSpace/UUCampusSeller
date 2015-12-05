package com.dreamspace.uucampusseller.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.TLog;
import com.dreamspace.uucampusseller.model.api.GetOrderStatusRes;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.ui.fragment.order.OrderShowFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by wufan on 2015/10/18.
 */
public class OrderFragment extends BaseLazyFragment {
    @Bind(R.id.order_smart_tab)
    SmartTabLayout mSmartTabLayout;
    @Bind(R.id.order_view_pager)
    ViewPager mViewPager;
    @Bind(R.id.order_content_linear_layout)
    LinearLayout contentLl;

    private boolean isFragmentDestroy = false;
    private boolean bNetWork=false;

    public static final String ORDER_TAB = "tab_index";

    @Override
    protected void onFirstUserVisible() {
        initTabs();
    }

    @Override
    protected void onUserVisible() {

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

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_order;
    }

    private void initTabs() {
        final List<String> items = new ArrayList<String>();
        if (NetUtils.isNetworkConnected(getActivity().getApplicationContext())) {
            ApiManager.getService(getActivity().getApplicationContext()).getOrderStatus(new Callback<GetOrderStatusRes>() {
                @Override
                public void success(GetOrderStatusRes getOrderStatusRes, Response response) {
                    TLog.i("success:",getOrderStatusRes.toString());
                        items.add(SharedData.orderTabs[0] + "(" + getOrderStatusRes.getOrder_status_1() + ")");
                        items.add(SharedData.orderTabs[1] + "(" + getOrderStatusRes.getOrder_status_2() + ")");
                        items.add(SharedData.orderTabs[2] + "(" + getOrderStatusRes.getOrder_status_3() + ")");
                        items.add(SharedData.orderTabs[3] + "(" + getOrderStatusRes.getOrder_status_0() + ")");
                        initFragment(items);
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });

        } else {
            showNetWorkError();
            toggleNetworkError(true, emtpyListener);
            bNetWork=true;
        }
    }

    private void initFragment(List<String> items) {
        if(bNetWork) {
            toggleRestore();
            bNetWork=false;
        }
            TLog.i("success:",items.toString());
        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
        for (String item : items) {
            pages.add(FragmentPagerItem.of(item, OrderShowFragment.class));
        }
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), pages);
        TLog.i("success:",adapter.getCount()+"");
        mViewPager.setAdapter(adapter);
        mSmartTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFragmentDestroy = true;
    }

    private View.OnClickListener emtpyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initTabs();
        }
    };

}
