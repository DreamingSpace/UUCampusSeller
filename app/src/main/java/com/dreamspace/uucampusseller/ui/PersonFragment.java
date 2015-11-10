package com.dreamspace.uucampusseller.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.model.api.ShopInfoRes;
import com.dreamspace.uucampusseller.model.api.ShopStatusRes;
import com.dreamspace.uucampusseller.ui.activity.Personal.AboutAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.AliPayAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.DirectionAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.FeedBackAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.InfoChangeAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.SettingAct;
import com.dreamspace.uucampusseller.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by wufan on 2015/10/18.
 */
public class PersonFragment extends BaseFragment {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        Log.d("TestData", "create");
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //解决从诸如修改店铺信息界面返回之后的界面需要刷新的问题
        if (isVisibleToUser) {
            Log.d("TestData", "holoholo~");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    public void initViews(View view) {
        initListeners();
    }

    @Override
    public void initDatas() {
        //获取商家店铺状态和信息
        getShopStatus();
    }

    //获取商家店铺状态shop_id
    private void getShopStatus() {
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity()).getShopStatus(new Callback<ShopStatusRes>() {
                @Override
                public void success(ShopStatusRes shopStatusRes, Response response) {
                    SharedData.shopStatus = shopStatusRes;
                    getShopInfo();
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
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity()).getShopInfo(SharedData.shopStatus.getShop_id(), new Callback<ShopInfoRes>() {
                @Override
                public void success(ShopInfoRes shopInfoRes, Response response) {
                    SharedData.shopInfo = shopInfoRes;
                    CommonUtils.showImageWithGlideInCiv(getActivity(), personalIron, SharedData.shopInfo.getImage());
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
                readyGo(AliPayAct.class);
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
}
