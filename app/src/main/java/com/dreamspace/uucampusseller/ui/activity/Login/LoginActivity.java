package com.dreamspace.uucampusseller.ui.activity.Login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.model.api.LoginReq;
import com.dreamspace.uucampusseller.model.api.LoginRes;
import com.dreamspace.uucampusseller.model.api.ShopStatusRes;
import com.dreamspace.uucampusseller.ui.MainActivity;
import com.dreamspace.uucampusseller.ui.activity.order.ApplyShopDoneActivity;
import com.dreamspace.uucampusseller.ui.activity.order.ApplyShopHintActivity;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by money on 2015/10/25.
 */
public class LoginActivity extends AbsActivity {
    @Bind(R.id.Login_userName)
    EditText LoginUserName;
    @Bind(R.id.Login_pwd)
    EditText LoginPwd;
    @Bind(R.id.login_page_loginButton)
    Button loginPageLoginButton;
    @Bind(R.id.login_page_forget)
    TextView loginPageForget;
    @Bind(R.id.login_page_register)
    TextView loginPageRegister;
    @Bind(R.id.custom_title_tv)
    TextView customTitleTv;

    ProgressDialog progressDialog;


    @Override
    protected int getContentView() {
        return R.layout.activity_login_page;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        customTitleTv.setText("登录");
        initListeners();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        LoginUserName.setText(PreferenceUtils.getString(this,PreferenceUtils.Key.PHONE));
    }

    private void initListeners() {
        loginPageLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = LoginUserName.getText().toString();
                String password = LoginPwd.getText().toString();
                if (isValid(phoneNum, password)) {
                    LoginReq req = new LoginReq();
                    req.setPhone_num(phoneNum);
                    req.setPassword(password);
                    login(req);
                }
            }
        });

        loginPageForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(FindBackActivity.class);
            }
        });

        loginPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(RegisterActivity.class);
            }
        });
    }

    //登录操作
    private void login(final LoginReq loginReq) {
        progressDialog = ProgressDialog.show(this, "", "正在登录", true, false);
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(this.getApplicationContext()).createAccessToken(loginReq, new Callback<LoginRes>() {
                @Override
                public void success(LoginRes loginRes, Response response) {
                    progressDialog.dismiss();
                    PreferenceUtils.putString(LoginActivity.this.getApplicationContext(),
                            PreferenceUtils.Key.ACCESS, loginRes.getAccess_token());
                    PreferenceUtils.putString(LoginActivity.this,PreferenceUtils.Key.PHONE,loginReq.getPhone_num());
                    ApiManager.clear();
                    getShopInfo();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
                    showInnerError(error);
                }
            });
        } else {
            progressDialog.dismiss();
            showNetWorkError();
        }
    }

    private void getShopInfo(){
        if(!NetUtils.isNetworkConnected(this)){
            showNetWorkError();
            return;
        }

        ApiManager.getService(this).getShopStatus(new Callback<ShopStatusRes>() {
            @Override
            public void success(ShopStatusRes shopStatusRes, Response response) {
                if(shopStatusRes!=null){
                    PreferenceUtils.putString(LoginActivity.this,PreferenceUtils.Key.SHOP_ID,shopStatusRes.getShop_id());
                    PreferenceUtils.putString(LoginActivity.this,PreferenceUtils.Key.CATEGORY,shopStatusRes.getCategory());
                    progressDialog.dismiss();
                    showToast("登录成功~");
                }
                if(shopStatusRes.getStatus().equals("ok")){
                    readyGoThenKill(MainActivity.class);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_id",shopStatusRes.getShop_id());
                    readyGoThenKill(ApplyShopDoneActivity.class, bundle);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                if(CommonUtils.getErrorInfo(error).getState().equals("error")){
                    readyGo(ApplyShopHintActivity.class);
                    return;
                }else{
                    showInnerError(error);
                }
            }
        });
    }

    //输入有效性判断
    private boolean isValid(String phoneNum, String pwd) {

        if (CommonUtils.isEmpty(phoneNum)) {
            showToast("请输入您的手机号码");
            LoginUserName.requestFocus();
            return false;
        }
        if (phoneNum.length() != 11) {
            showToast("请检查您的输入格式");
            LoginUserName.requestFocus();
            return false;
        }
        if (CommonUtils.isEmpty(pwd)) {
            showToast("请输入您的密码");
            return false;
        }
        return true;
    }
}
