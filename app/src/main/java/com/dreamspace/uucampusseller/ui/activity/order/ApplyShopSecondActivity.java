package com.dreamspace.uucampusseller.ui.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.ui.dialog.GoodsClassifyDialog;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.OnClick;

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

    private ArrayList<String> classifys;
    private boolean correct;

    public static final String EXTRA_SHOP_CLASSIFY = "shop_classify";
    public static final String EXTRA_SHOP_BUSINESS_AREA = "shop_business_area";
    public static final String EXTRA_SHOP_introduction = "shop_introduction";

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_shop_second;
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.apply_shop_commit_btn)
    void commit(){
        if(isCorrect()) {
            Bundle bundle = new Bundle();
//            bundle.putString(ApplyShopFirstActivity.EXTRA_SHOP_PHOTO, mLocalImagePath);
//            bundle.putString(ApplyShopFirstActivity.EXTRA_SHOP_NAME, mShopNameEt.getText().toString());
//            bundle.putString(ApplyShopFirstActivity.EXTRA_SHOP_HOST_NAME, mShopHostNameEt.getText().toString());
//            bundle.putString(ApplyShopFirstActivity.EXTRA_CONNECT_PHONE, mConnectPhoneEt.getText().toString());
//            bundle.putString(ApplyShopFirstActivity.EXTRA_CONNECT_ADDRESS, mConnectAddressEt.getText().toString());

            bundle.putString(EXTRA_SHOP_BUSINESS_AREA,mBusinessAreaEt.getText().toString());
            bundle.putString(EXTRA_SHOP_CLASSIFY,mClassifyEt.getText().toString());
            bundle.putString(EXTRA_SHOP_introduction,mShopIntroductionEt.getText().toString());
            readyGo(ApplyShopDoneActivity.class, bundle);
        }else{
            showToast("填写信息错误");
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

        return correct;
    }
}
