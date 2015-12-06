package com.dreamspace.uucampusseller.ui.activity.order;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.UploadImage;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.TLog;
import com.dreamspace.uucampusseller.model.api.QnRes;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.widget.photopicker.SelectPhotoActivity;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
    private boolean correct = true;

    public static int PHOTO_REQUEST_CODE = 1;
    private String mLocalImagePath = null;
    private String mPhotoPath = null;
    ProgressDialog pd = null;

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
            bundle.putString(EXTRA_SHOP_PHOTO, mPhotoPath);
            bundle.putString(EXTRA_SHOP_NAME, mShopNameEt.getText().toString());
            bundle.putString(EXTRA_SHOP_HOST_NAME, mShopHostNameEt.getText().toString());
            bundle.putString(EXTRA_CONNECT_PHONE, mConnectPhoneEt.getText().toString());
            bundle.putString(EXTRA_CONNECT_ADDRESS, mConnectAddressEt.getText().toString());
            readyGo(ApplyShopSecondActivity.class, bundle);
        }
    }

    public boolean isCorrect() {
        //判断填写信息无误
        if (mPhotoPath == null) {
            showToast("请上传照片");
            return false;
        }
        if (mShopNameEt.length() == 0) {
            showToast("请填写店铺名称");
            return false;
        }
        if (mShopHostNameEt.length() == 0) {
            showToast("请填写店主姓名");
            return false;
        }
        if (mConnectPhoneEt.length() != 11) {
            showToast("联系电话填写有误");
            return false;
        }
        if (mConnectAddressEt.length() == 0) {
            showToast("请填写联系地址");
            return false;
        }
        return correct;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                mLocalImagePath = data.getStringExtra(SelectPhotoActivity.PHOTO_PATH);
                upLoadImage(mLocalImagePath);  //图片上传至七牛服务器

            }
        }
    }

    //上传图片
    private void upLoadImage(final String path) {
        TLog.i("Path:", path);
        pd = ProgressDialog.show(this, "", "正在创建", true, false);
        if (NetUtils.isNetworkConnected(this.getApplicationContext())) {
            ApiManager.getService(this).createQiNiuToken(new Callback<QnRes>() {
                @Override
                public void success(QnRes qnRes, Response response) {
                    UploadImage.upLoadImage(path, qnRes.getKey(), qnRes.getToken(), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (info.isOK()) {
                                mPhotoPath = key;   //获取上传的七牛服务器图片url，并传到下一个activity
                                pd.dismiss();
                                Glide.with(ApplyShopFirstActivity.this)
                                        .load(mLocalImagePath)
                                        .centerCrop()
                                        .into(mPhotoIv);
                            } else if (info.isServerError()) {
                                pd.dismiss();
                                showToast("服务暂时不可用，请稍后重试");
                            }
                        }
                    }, null);
                }

                @Override
                public void failure(RetrofitError error) {
                    pd.dismiss();
                    TLog.i("error:", error.getMessage() + error.toString());
                    showInnerError(error);
                }
            });
        } else {
            pd.dismiss();
            showNetWorkError();
        }
    }

    BroadcastReceiver broadcastReceiver =new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();             //接收销毁该activity时销毁此activity
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //在当前activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("destroyActivity");
        this.registerReceiver(this.broadcastReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.broadcastReceiver);
    }

}
