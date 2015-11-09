package com.dreamspace.uucampusseller.ui.activity.order;

import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wufan on 2015/10/23.
 */
public class ApplyShopHintActivity extends AbsActivity{
    @Bind(R.id.apply_shop_text_view)
    TextView mApplyShopTv;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_shop_hint;
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.apply_shop_text_view)
    void applyShop(){
        readyGo(ApplyShopFirstActivity.class);
    }
}
