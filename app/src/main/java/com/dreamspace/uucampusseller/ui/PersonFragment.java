package com.dreamspace.uucampusseller.ui;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.model.api.ShopBindRes;
import com.dreamspace.uucampusseller.model.api.ShopInfoRes;
import com.dreamspace.uucampusseller.model.api.ShopStatusRes;
import com.dreamspace.uucampusseller.ui.activity.Personal.AboutAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.AliPayAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.AliPayDoneAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.AliPayWaitAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.DirectionAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.FeedBackAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.InfoChangeAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.SettingAct;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by wufan on 2015/10/18.
 */
public class PersonFragment extends BaseLazyFragment {
    @Bind(R.id.personal_relative1)
    RelativeLayout personalRelative1;
    @Bind(R.id.personal_relative2)
    RelativeLayout personalRelative2;
    @Bind(R.id.personal_relative3)
    RelativeLayout personalRelative3;
    @Bind(R.id.personal_relative4)
    RelativeLayout personalRelative4;
    @Bind(R.id.personal_relative5)
    RelativeLayout personalRelative5;
    @Bind(R.id.person_linear)
    LinearLayout personLinear;
    @Bind(R.id.personal_iron)
    CircleImageView personalIron;
    @Bind(R.id.person_call_telephone)
    TextView personCallTelephone;
    @Bind(R.id.personal_name)
    TextView personalName;
    @Bind(R.id.ali_bind_text)
    TextView aliBindText;

    @Override
    protected void onFirstUserVisible() {
        getShopStatus();
    }

    @Override
    protected void onUserVisible() {
        //修改店铺资料后重新刷新，获取数据
        getShopStatus();

    }

    @Override
    protected void initViewsAndEvents() {
        initListeners();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_person;
    }

    @Override
    protected void onUserInvisible() {
    }

    //获取商家店铺状态shop_id
    private void getShopStatus() {
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity()).getShopStatus(new Callback<ShopStatusRes>() {
                @Override
                public void success(ShopStatusRes shopStatusRes, Response response) {
                    SharedData.shopStatus = shopStatusRes;
                    getShopInfo();
                    getBindStatus();
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

    //获取店铺信息
    private void getShopInfo() {
        ApiManager.getService(getActivity()).getShopInfo(SharedData.shopStatus.getShop_id(), new Callback<ShopInfoRes>() {
            @Override
            public void success(ShopInfoRes shopInfoRes, Response response) {
                SharedData.shopInfo = shopInfoRes;
                personalName.setText(SharedData.shopInfo.getName());
                Log.d("TestData", "get:" + SharedData.shopInfo.getImage());
                CommonUtils.showImageWithGlideInCiv(getActivity(), personalIron, SharedData.shopInfo.getImage());

            }

            @Override
            public void failure(RetrofitError error) {
                showInnerError(error);
            }
        });
    }

    //获取店铺支付宝绑定状态
    private void getBindStatus() {
        ApiManager.getService(getActivity()).getShopBindStatus(new Callback<ShopBindRes>() {
            @Override
            public void success(ShopBindRes shopBindRes, Response response) {
                SharedData.alipay = shopBindRes.getAlipay();
                SharedData.status = shopBindRes.getAlipay_identified();
                if (SharedData.status == 0) {
                    //绑定状态为审核中
                    aliBindText.setText("审核中");
                }else if(SharedData.status == -1){
                    aliBindText.setText("已绑定");
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void initListeners() {
        personLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(InfoChangeAct.class);
            }
        });

        personalRelative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (SharedData.status){
                    case -1:
                        readyGo(AliPayAct.class);
                        break;
                    case 0:
                        readyGo(AliPayWaitAct.class);
                        break;
                    case 1:
                        readyGo(AliPayDoneAct.class);
                        break;
                }
            }
        });

        personalRelative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(DirectionAct.class);
            }
        });

        personalRelative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(SettingAct.class);
            }
        });

        personalRelative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(AboutAct.class);
            }
        });

        personalRelative5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(FeedBackAct.class);
            }
        });

        personCallTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用Intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:18051017809"));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

}
