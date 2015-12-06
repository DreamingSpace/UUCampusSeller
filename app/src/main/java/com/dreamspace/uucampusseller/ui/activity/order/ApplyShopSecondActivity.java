package com.dreamspace.uucampusseller.ui.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.common.utils.TLog;
import com.dreamspace.uucampusseller.model.api.CreateShopReq;
import com.dreamspace.uucampusseller.model.api.CreateShopRes;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.ui.dialog.GoodsClassifyDialog;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
/**
 * Created by wufan on 2015/10/22.
 */
public class ApplyShopSecondActivity extends AbsActivity{
    @Bind(R.id.apply_shop_commit_btn)
    Button mCommitBtn;
    @Bind(R.id.apply_shop_classify_edit_text)
    EditText mClassifyEt;
    @Bind(R.id.apply_shop_introduction_et)
    EditText mShopIntroductionEt;
    @Bind(R.id.apply_shop_business_area_et)
    EditText mBusinessAreaEt;

    public static final String EXTRA_SHOP_ID="shop_id";

    private ArrayList<String> classifys;
    private boolean correct;
    private CreateShopReq req=new CreateShopReq();
    private String shop_id=null;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_shop_second;
    }

    @Override
    protected void prepareDatas() {
        Bundle bundle = getIntent().getExtras();
        req.setName(bundle.getString(ApplyShopFirstActivity.EXTRA_SHOP_NAME));
        req.setOwner(bundle.getString(ApplyShopFirstActivity.EXTRA_SHOP_HOST_NAME));
        req.setPhone_num(bundle.getString(ApplyShopFirstActivity.EXTRA_CONNECT_PHONE));
        req.setAddress(bundle.getString(ApplyShopFirstActivity.EXTRA_CONNECT_ADDRESS));
        req.setImage(bundle.getString(ApplyShopFirstActivity.EXTRA_SHOP_PHOTO));
        req.setLocation(PreferenceUtils.getString(this, PreferenceUtils.Key.LOCATION));
    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.apply_shop_commit_btn)
    void commit(){
        if(isCorrect()) {
            createShop();
        }
    }

    private void createShop() {
        req.setCategory(mClassifyEt.getText().toString());
        req.setMain(mBusinessAreaEt.getText().toString());
        req.setDescription(mShopIntroductionEt.getText().toString());
        if(NetUtils.isNetworkConnected(getApplicationContext())){
            ApiManager.getService(getApplicationContext()).createShop(req, new Callback<CreateShopRes>() {
                @Override
                public void success(CreateShopRes createShopRes, Response response) {
                    shop_id=createShopRes.getShop_id();
                    TLog.i("创建店铺成功：",shop_id);
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_SHOP_ID,shop_id);
                    readyGoThenKill(ApplyShopDoneActivity.class, bundle);
                }

                @Override
                public void failure(RetrofitError error) {
                    TLog.i("error:",error.getMessage());
                    showInnerError(error);
                }
            });
        }else {
            showNetWorkError();
        }
    }

    @OnClick(R.id.apply_shop_classify_edit_text)
    void selectClassify(){
        final GoodsClassifyDialog dialog = new GoodsClassifyDialog(this,getClassifys());
        dialog.setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setClassifyName();
                mClassifyEt.setText(dialog.getClassifyName());
                dialog.dismiss();
            }
        });
    }

    public ArrayList<String> getClassifys() {
        ArrayList<String> classifys = new ArrayList<String>();
        Collections.addAll(classifys, SharedData.goodsClassify);
        return classifys;
    }

    public boolean isCorrect() {
        //判断填写信息正确
        boolean correct = true;
        if(mClassifyEt.length()==0){
            showToast("请选择店铺类型");
            return false;
        }
        if(mBusinessAreaEt.length()==0){
            showToast("请填写经营范围");
            return false;
        }
        if(mShopIntroductionEt.length()<10){
            showToast("店铺简介字数不少于10个");
            return false;
        }
        return correct;
    }
}
