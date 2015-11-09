package com.dreamspace.uucampusseller.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.base.BaseFragment;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.ui.fragment.Goods.GoodsClassifyFragment;
import com.dreamspace.uucampusseller.ui.fragment.Goods.OnSalePullOffFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

import butterknife.Bind;

/**
 * Created by wufan on 2015/10/18.
 */
public class GoodsFragment extends BaseLazyFragment{
    @Bind(R.id.no_goods_ll)
    LinearLayout noGoodsLl;
    @Bind(R.id.have_goods_ll)
    LinearLayout haveGoodsLl;
    @Bind(R.id.my_shop_act_smarttablayout)
    SmartTabLayout smartTabLayout;
    @Bind(R.id.my_shop_act_view_pager)
    ViewPager pager;

    public static String MANAGEMENT_TYPE = "management_type";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onFirstUserVisible() {
        FragmentStatePagerItemAdapter adapter = new FragmentStatePagerItemAdapter(getFragmentManager(),
                FragmentPagerItems.with(getActivity())
                        .add(getResources().getString(R.string.catalog_list),GoodsClassifyFragment.class,getBundle(0))
                        .add(getResources().getString(R.string.on_sale), OnSalePullOffFragment.class, getBundle(1))
                        .add(getResources().getString(R.string.already_pull_off),OnSalePullOffFragment.class,getBundle(2))
                        .create());
        smartTabLayout.setCustomTabView(R.layout.goods_smart_tab_title_tab,R.id.title_tv);
        pager.setAdapter(adapter);
        smartTabLayout.setViewPager(pager);
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

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_goods;
    }

    private Bundle getBundle(int position){
        Bundle bundle = new Bundle();
        if(position < 3){
            switch (position){
                case 0:
                    bundle.putString(MANAGEMENT_TYPE,getString(R.string.catalog_list));
                    break;

                case 1:
                    bundle.putString(MANAGEMENT_TYPE,getString(R.string.on_sale));
                    break;

                case 2:
                    bundle.putString(MANAGEMENT_TYPE,getString(R.string.already_pull_off));
                    break;
            }
        }
        return  bundle;
    }
}
