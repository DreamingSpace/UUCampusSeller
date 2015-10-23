package com.dreamspace.uucampusseller.ui.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.widget.photopicker.SelectPhotoActivity;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wufan on 2015/10/22.
 */
public class ApplyShopFirstActivity extends AbsActivity {
    @Bind(R.id.apply_shop_next_btn)
    Button mNextBtn;
    @Bind(R.id.apply_shop_photo_circle_Iv)
    CircleImageView mPhotoIv;
    @Bind(R.id.apply_shop_name_et)
    EditText mShopNameEt;
    @Bind(R.id.apply_shop_host_name_et)
    EditText mShopHostNameEt;
    @Bind(R.id.apply_shop_connect_phone_et)
    EditText mConnectPhoneEt;
    @Bind(R.id.apply_shop_connect_address_et)
    EditText mConnectAddressEt;

    public static final String EXTRA_SHOP_PHOTO = "shop_photo";
    public static final String EXTRA_SHOP_NAME = "shop_name";
    public static final String EXTRA_SHOP_HOST_NAME = "shop_host_name";
    public static final String EXTRA_CONNECT_PHONE = "connect_phone";
    public static final String EXTRA_CONNECT_ADDRESS = "connect_address";
    private boolean correct;

    public static int PHOTO_REQUEST_CODE = 1;
    private String mLocalImagePath = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_shop_first;
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.apply_shop_photo_circle_Iv)
    void selectPhoto() {
        readyGoForResult(SelectPhotoActivity.class, PHOTO_REQUEST_CODE);
    }

    @OnClick(R.id.apply_shop_next_btn)
    void nextApply() {
        if (isCorrect()) {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_SHOP_PHOTO, mLocalImagePath);
            bundle.putString(EXTRA_SHOP_NAME, mShopNameEt.getText().toString());
            bundle.putString(EXTRA_SHOP_HOST_NAME, mShopHostNameEt.getText().toString());
            bundle.putString(EXTRA_CONNECT_PHONE, mConnectPhoneEt.getText().toString());
            bundle.putString(EXTRA_CONNECT_ADDRESS, mConnectAddressEt.getText().toString());
            readyGo(ApplyShopSecondActivity.class, bundle);
        } else {
            showToast("填写信息有误");
        }
    }

    public boolean isCorrect() {
        //判断填写信息无误
        boolean correct = true;
        return correct;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String mLocalImagePath = data.getStringExtra(SelectPhotoActivity.PHOTO_PATH);
//                ArrayList<String> photo = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Glide.with(this)
                        .load(mLocalImagePath)
                        .centerCrop()
                        .into(mPhotoIv);
            }
        }
    }
}
