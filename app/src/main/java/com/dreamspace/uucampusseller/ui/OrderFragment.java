package com.dreamspace.uucampusseller.ui;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.ui.fragment.order.OrderShowFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wufan on 2015/10/18.
 */
public class OrderFragment extends BaseLazyFragment {
    @Bind(R.id.order_smart_tab)
    SmartTabLayout mSmartTabLayout;
    @Bind(R.id.order_view_pager)
    ViewPager mViewPager;

    public static final String ORDER_TAB="tab_index";

    @Override
    protected void onFirstUserVisible() {

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
        final List<String> items = new ArrayList<String>();
        items.add(SharedData.orderTabs[0]+"(3)");
        items.add(SharedData.orderTabs[1]+"(0)");
        items.add((SharedData.orderTabs[2]+"(0)"));
        items.add(SharedData.orderTabs[3]+"(0)");
        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
        for (String item : items) {
            pages.add(FragmentPagerItem.of(item, OrderShowFragment.class));
        }
        FragmentStatePagerItemAdapter adapter = new FragmentStatePagerItemAdapter(
                getActivity().getSupportFragmentManager(), pages);
        mViewPager.setAdapter(adapter);
        mSmartTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_order;
    }

}
