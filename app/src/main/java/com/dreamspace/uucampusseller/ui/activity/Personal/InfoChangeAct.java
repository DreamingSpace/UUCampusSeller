package com.dreamspace.uucampusseller.ui.activity.Personal;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.common.UploadImage;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.model.api.QnRes;
import com.dreamspace.uucampusseller.model.api.UpdateShopInfoReq;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.widget.photopicker.SelectPhotoActivity;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by money on 2015/11/9.
 */
public class InfoChangeAct extends AbsActivity {
    @Bind(R.id.info_change_iron)
    CircleImageView infoChangeIron;
    @Bind(R.id.info_text1)
    EditText infoText1;
    @Bind(R.id.info_text2)
    EditText infoText2;
    @Bind(R.id.info_text3)
    EditText infoText3;
    @Bind(R.id.info_text4)
    EditText infoText4;
    @Bind(R.id.info_text5)
    EditText infoText5;
    @Bind(R.id.info_text6)
    EditText infoText6;
    @Bind(R.id.info_text7)
    EditText infoText7;
    @Bind(R.id.info_change_button)
    Button infoChangeButton;

    public static int PHOTO_REQUEST_CODE = 1;
    private boolean imgChange = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_info_change;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {

        CommonUtils.showImageWithGlideInCiv(this,infoChangeIron,SharedData.shopInfo.getImage());
        infoText1.setText(SharedData.shopInfo.getName());
        infoText2.setText(SharedData.shopInfo.getOwner());
        infoText3.setText(SharedData.shopInfo.getPhone_num());
        infoText4.setText(SharedData.shopInfo.getAddress());
        infoText5.setText(SharedData.shopInfo.getCategory());
        infoText6.setText(SharedData.shopInfo.getMain());
        infoText7.setText(SharedData.shopInfo.getDescription());

        infoChangeIron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(SelectPhotoActivity.class, PHOTO_REQUEST_CODE);
            }
        });

        infoChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateShopInfoReq req = new UpdateShopInfoReq();
                if(imgChange){
                    req.setImage(SharedData.shopInfo.getImage());
                }
                Log.d("TestData",SharedData.shopInfo.getImage());
                req.setName(infoText1.getText().toString());
                req.setOwner(infoText2.getText().toString());
                req.setPhone_num(infoText3.getText().toString());
                req.setAddress(infoText4.getText().toString());
                req.setCategory(infoText5.getText().toString());
                req.setMain(infoText6.getText().toString());
                req.setDescription(infoText7.getText().toString());
                updateShopInfo(req);
            }
        });
    }

    private void updateShopInfo(UpdateShopInfoReq req){
        if(NetUtils.isNetworkConnected(this)){
            final com.dreamspace.uucampusseller.ui.dialog.ProgressDialog progressDialog = new com.dreamspace.uucampusseller.ui.dialog.ProgressDialog(this);
            progressDialog.setContent("正在更新店铺信息");
            progressDialog.show();
            ApiManager.getService(this).updateShopInfo(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    progressDialog.dismiss();
                    showToast("店铺信息更新成功~");
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 2000);
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                    progressDialog.dismiss();
                }
            });
        }else{
            showNetWorkError();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String path = data.getStringExtra(SelectPhotoActivity.PHOTO_PATH);
                upLoadImage(path);  //图片上传至七牛服务器
                Glide.with(this)
                        .load(path)
                        .centerCrop()
                        .into(infoChangeIron);
            }
        }
    }

    //上传图片
    private void upLoadImage(final String path){
        if (NetUtils.isNetworkConnected(this.getApplicationContext())) {
            final com.dreamspace.uucampusseller.ui.dialog.ProgressDialog progressDialog = new com.dreamspace.uucampusseller.ui.dialog.ProgressDialog(this);
            progressDialog.setContent("正在上传头像");
            progressDialog.show();
            ApiManager.getService(this).createQiNiuToken(new Callback<QnRes>() {
                @Override
                public void success(QnRes qnRes, Response response) {
                    UploadImage.upLoadImage(path, qnRes.getKey(), qnRes.getToken(), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (info.isOK()) {
                                //获取上传的七牛服务器图片url
                                SharedData.shopInfo.setImage(key);
                                imgChange = true;
                                progressDialog.dismiss();
                                showToast("头像上传成功");
                            } else if (info.isServerError()) {
                                progressDialog.dismiss();
                                showToast("服务暂时不可用，请稍后重试");
                            }
                        }
                    }, null);
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
                    showInnerError(error);
                }
            });
        }else{
            showNetWorkError();
        }
    }
}
