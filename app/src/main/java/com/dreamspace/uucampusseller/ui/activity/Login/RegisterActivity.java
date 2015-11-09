package com.dreamspace.uucampusseller.ui.activity.Login;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.model.api.RegisterReq;
import com.dreamspace.uucampusseller.model.api.RegisterRes;
import com.dreamspace.uucampusseller.model.api.SendVerifyReq;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by money on 2015/11/9.
 */
public class RegisterActivity extends AbsActivity implements Handler.Callback{
    @Bind(R.id.register_userName)
    EditText registerUserName;
    @Bind(R.id.register_verify_code)
    EditText registerVerifyCode;
    @Bind(R.id.register_get_code)
    TextView registerGetCode;
    @Bind(R.id.register_pwd)
    EditText registerPwd;
    @Bind(R.id.register_pwd_confirm)
    EditText registerPwdConfirm;
    @Bind(R.id.register_button)
    Button registerButton;

    private int timer = 60;
    private Handler mHandler;
    private static final int BEGIN_TIMER = 23333;
    private String text = "发送验证码";

    @Override
    protected int getContentView() {
        return R.layout.activity_register_page;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
        mHandler = new Handler(this);
    }

    @Override
    protected void initViews() {
        initListeners();
    }

    private void initListeners(){
        registerGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = registerUserName.getText().toString();
                if(isPhoneValid(phoneNum)){
                    SendVerifyReq req = new SendVerifyReq();
                    req.setPhone_num(phoneNum);
                    sendVerifyCode(req);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = registerUserName.getText().toString();
                String code = registerVerifyCode.getText().toString();
                String password = registerPwd.getText().toString();
                String passwordConfirm = registerPwdConfirm.getText().toString();
                if(isPhoneValid(phoneNum)&&isRestValid(code,password,passwordConfirm)){
                    RegisterReq registerReq = new RegisterReq();
                    registerReq.setPhone_num(phoneNum);
                    registerReq.setCode(code);
                    registerReq.setPassword(password);

                    register(registerReq);
                }
            }
        });
    }

    //获取手机验证码
    private void sendVerifyCode(SendVerifyReq req){
        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).sendVerifyCode(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    showToast("验证码发送成功~");
                    registerGetCode.setEnabled(false);
                    mHandler.sendEmptyMessage(BEGIN_TIMER);
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });
        }else {
            showNetWorkError();
        }
    }

    //注册
    private void register(RegisterReq req){

        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).register(req, new Callback<RegisterRes>() {
                @Override
                public void success(RegisterRes registerRes, Response response) {
                    if (response.getStatus() == 200) {
                        //保存user_id,access_token,timelimit
                        PreferenceUtils.putString(RegisterActivity.this.getApplicationContext(),
                                PreferenceUtils.Key.ACCESS, registerRes.getAccess_token());
                        mHandler.removeMessages(BEGIN_TIMER);
                        readyGoThenKill(RegisterSucceedActivity.class);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });
        }else {
            showNetWorkError();
        }
    }

    //检查输入的手机号码是否正确
    private boolean isPhoneValid(String phoneNum){
        if (CommonUtils.isEmpty(phoneNum)) {
            showToast("请先输入您的手机号");
            return false;
        }
        if (phoneNum.length() != 11) {
            showToast("请检查您的手机号是否正确");
            registerUserName.requestFocus();
            return false;
        }
        return true;
    }

    //检测验证码密码是否输入正确
    private boolean isRestValid(String code,String password,String passwordConfirm){
        if (CommonUtils.isEmpty(code)) {
            showToast("请先输入您输入的验证码");
            registerVerifyCode.requestFocus();
            return false;
        }
        if(CommonUtils.isEmpty(password)){
            showToast("请输入您设置的密码");
            registerPwd.requestFocus();
            return false;
        }
        if(CommonUtils.isEmpty(passwordConfirm)){
            showToast("请再次输入您设置的密码");
            registerPwdConfirm.requestFocus();
            return false;
        }
        if(!password.equals(passwordConfirm)){
            showToast("两次输入的密码不一致，请重新输入");
            registerPwdConfirm.setText("");
            registerPwdConfirm.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    //获取验证码的间隔限制
    public boolean handleMessage(Message message) {
        if(message.what == BEGIN_TIMER){
            if(timer==0){
                if(registerGetCode!=null){
                    registerGetCode.setText(text);
                    registerGetCode.setEnabled(true);
                    timer=60;
                }
            }else{
                if(registerGetCode!=null){
                    registerGetCode.setText(timer + "秒后重新发送");
                    timer--;
                    mHandler.sendEmptyMessageDelayed(BEGIN_TIMER, 1000);
                }
            }
        }
        return true;
    }
}
