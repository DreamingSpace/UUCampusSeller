package com.dreamspace.uucampusseller.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.ui.activity.Goods.EditGoodInfoAct;
import com.dreamspace.uucampusseller.ui.activity.order.ApplyShopHintActivity;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends AbsActivity implements View.OnClickListener{

    @Bind(R.id.homepage_view_pager)
    ViewPager mViewPager;
    @Bind(R.id.homepage_order_linear_layout)
    LinearLayout mOrder;
    @Bind(R.id.homepage_goods_linear_layout)
    LinearLayout mGoods;
    @Bind(R.id.homepage_person_linear_layout)
    LinearLayout mPerson;

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<LinearLayout> mLinearLayouts = new ArrayList<LinearLayout>();

    private int currentIndex = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void prepareDatas() {
        PreferenceUtils.putString(this,PreferenceUtils.Key.ACCESS,"4d06cc40-82e0-11e5-bd1c-00163e021195");
        PreferenceUtils.putString(this,PreferenceUtils.Key.SHOP_ID,"5636f3cf90c49063a04cabb8");
        PreferenceUtils.putString(this,PreferenceUtils.Key.CATEGORY,"旅游");
    }

    @Override
    protected void initViews() {
        initView();
        initDates();
        initListener();
    }

    private void initView() {
        mLinearLayouts.add(mOrder);
        mLinearLayouts.add(mGoods);
        mLinearLayouts.add(mPerson);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titleTv = (TextView) mToolBar.findViewById(R.id.custom_title_tv);
        titleTv.setText(getString(R.string.app_name));
    }

    private void initDates() {

        //测试
       // access_token存放
//        PreferenceUtils.putString(this.getApplicationContext(),
//                PreferenceUtils.Key.ACCESS,"e75d1024-8155-11e5-a16f-00163e021195");// aae889e0-82d4-11e5-bd1c-00163e021195
//
//        TLog.i("acces_token:",PreferenceUtils.getString(this.getApplicationContext(), PreferenceUtils.Key.ACCESS));

        OrderFragment orderFragment = new OrderFragment();
        GoodsFragment goodsFragment = new GoodsFragment();
        PersonFragment personFragment = new PersonFragment();
        mFragments.add(orderFragment);
        mFragments.add(goodsFragment);
        mFragments.add(personFragment);

        FragmentStatePagerAdapter mAdapter;
        mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        currentIndex = 0;
                        break;
                    case 1:
                        currentIndex = 1;
                        break;
                    case 2:
                        currentIndex = 2;
                        break;
                }
                invalidateOptionsMenu(); //重新加载menu item
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void initListener() {
        mOrder.setOnClickListener(this);
        mGoods.setOnClickListener(this);
        mPerson.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem scanCode = menu.findItem(R.id.scan_ensure);
        MenuItem addGood = menu.findItem(R.id.add_good);
        switch (currentIndex){  //根据fragment的移动，选择相应的菜单栏
            case 0:
                scanCode.setVisible(true);
                addGood.setVisible(false);
                break;

            case 1:
                scanCode.setVisible(false);
                addGood.setVisible(true);
                break;

            case 2:
                scanCode.setVisible(false);
                addGood.setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.scan_ensure) {
            readyGo(ApplyShopHintActivity.class);
            return true;
        }
        //添加商品
        else if(id == R.id.add_good) {
            Bundle bundle = new Bundle();
            bundle.putInt(EditGoodInfoAct.IN_WAY, EditGoodInfoAct.IN_FROM_CRETAE);
            readyGo(EditGoodInfoAct.class, bundle);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homepage_order_linear_layout:
                mViewPager.setCurrentItem(0,false);
                break;
            case R.id.homepage_goods_linear_layout:
                mViewPager.setCurrentItem(1,false);
                break;
            case R.id.homepage_person_linear_layout:
                mViewPager.setCurrentItem(2,false);
                break;
        }
    }
}
