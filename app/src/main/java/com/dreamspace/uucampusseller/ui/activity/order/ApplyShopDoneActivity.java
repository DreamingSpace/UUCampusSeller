package com.dreamspace.uucampusseller.ui.activity.order;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.MainActivity;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wufan on 2015/10/23.
 */
public class ApplyShopDoneActivity extends AbsActivity {
    @Bind(R.id.apply_shop_done_back_btn)
    Button mBackBtn;
    @Bind(R.id.apply_shop_done_photo_circle_Iv)
    CircleImageView mPhoto;
    @Bind(R.id.apply_shop_done_shop_name_tv)
    TextView mShopNameTv;
    @Bind(R.id.apply_shop_done_shop_host_name_tv)
    TextView mShopHostNameTv;
    @Bind(R.id.apply_shop_done_connect_phone_tv)
    TextView mConnectPhoneTv;
    @Bind(R.id.apply_shop_done_connect_address_tv)
    TextView mConnectAddressTv;
    @Bind(R.id.apply_shop_done_shop_classify_tv)
    TextView mShopClassifyTv;
    @Bind(R.id.apply_shop_done_shop_introduction_tv)
    TextView mShop_introductionTv;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_shop_done;
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        Bundle bundle =getIntent().getExtras();

    }

    @OnClick(R.id.apply_shop_done_back_btn)
    void backHome() {
        readyGo(MainActivity.class);
    }
}
