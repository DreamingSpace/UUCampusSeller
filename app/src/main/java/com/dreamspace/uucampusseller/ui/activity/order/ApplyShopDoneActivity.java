package com.dreamspace.uucampusseller.ui.activity.order;

import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.TLog;
import com.dreamspace.uucampusseller.model.api.ShopInfoRes;
import com.dreamspace.uucampusseller.model.api.ShopStatusRes;
import com.dreamspace.uucampusseller.ui.MainActivity;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by wufan on 2015/10/23.
 */
public class ApplyShopDoneActivity extends AbsActivity {
    @Bind(R.id.apply_shop_done_back_btn)
    Button mBackBtn;
    @Bind(R.id.apply_shop_done_photo_circle_Iv)
    CircleImageView mPhotoCv;
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
    @Bind(R.id.apply_shop_done_shop_business_area_tv)
    TextView mMainTv;

    private String shop_id=null;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_shop_done;
    }

    @Override
    protected void prepareDatas() {
        shop_id=getIntent().getExtras().getString(ApplyShopSecondActivity.EXTRA_SHOP_ID);
        getShopInfo();
    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.apply_shop_done_back_btn)
    void backHome() {
        checkShop();
    }

    private void checkShop() {
        if(NetUtils.isNetworkConnected(getApplicationContext())){
            ApiManager.getService(getApplicationContext()).getShopStatus(new Callback<ShopStatusRes>() {
                @Override
                public void success(ShopStatusRes shopStatusRes, Response response) {
                    if(shopStatusRes.getStatus().equals("ok")){
                        showToast("恭喜您的店铺通过审核！");
                        readyGoThenKill(MainActivity.class);
                    }else{
                        showToast("您的店铺还在审核中，请耐心等候！");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });
        }else{
            showNetWorkError();
        }

    }

    public void getShopInfo() {
        if(NetUtils.isNetworkConnected(getApplicationContext())){
            ApiManager.getService(getApplicationContext()).getShopInfo(shop_id, new Callback<ShopInfoRes>() {
                @Override
                public void success(ShopInfoRes shopInfoRes, Response response) {
                    initData(shopInfoRes);
                }

                @Override
                public void failure(RetrofitError error) {
                    TLog.i("error",error.getMessage());
                    showInnerError(error);
                }
            });
        }else {
            showNetWorkError();
        }

    }

    private void initData(ShopInfoRes shopInfoRes) {  //从后台获取的数据更新UI
        CommonUtils.showImageWithGlide(getApplicationContext(), mPhotoCv, shopInfoRes.getImage());
        mShopNameTv.setText(shopInfoRes.getName());
        mShopHostNameTv.setText(shopInfoRes.getOwner());
        mConnectPhoneTv.setText(shopInfoRes.getPhone_num());
        mConnectAddressTv.setText(shopInfoRes.getAddress());
//        mShopClassifyTv.setText(shopInfoRes.getMain());
        mShop_introductionTv.setText(shopInfoRes.getDescription());
        mMainTv.setText(shopInfoRes.getMain());
    }
}
