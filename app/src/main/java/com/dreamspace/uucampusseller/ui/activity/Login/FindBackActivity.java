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
import com.dreamspace.uucampusseller.model.api.ResetReq;
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
public class FindBackActivity extends AbsActivity implements Handler.Callback{

    @Bind(R.id.findback_userName)
    EditText findBackUserName;
    @Bind(R.id.findback_verify_code)
    EditText findBackVerifyCode;
    @Bind(R.id.findback_get_code)
    TextView findBackGetCode;
    @Bind(R.id.findback_pwd)
    EditText findBackPwd;
    @Bind(R.id.findback_pwd_confirm)
    EditText findBackPwdConfirm;
    @Bind(R.id.findback_button)
    Button findBackButton;

    private int timer = 60;
    private Handler mHandler;
    private static final int BEGIN_TIMER = 23333;
    private String text = "发送验证码";

    @Override
    protected int getContentView() {
        return R.layout.activity_findback_pwd;
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
        findBackGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = findBackUserName.getText().toString();
                if(isPhoneValid(phoneNum)){
                    SendVerifyReq req = new SendVerifyReq();
                    req.setPhone_num(phoneNum);
                    sendVerifyCode(req);
                }
            }
        });

        findBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = findBackUserName.getText().toString();
                String code = findBackVerifyCode.getText().toString();
                String password = findBackPwd.getText().toString();
                String passwordConfirm = findBackPwdConfirm.getText().toString();
                if (isPhoneValid(phoneNum) && isRestValid(code, password, passwordConfirm)) {
                    ResetReq req = new ResetReq();
                    req.setPhone_num(phoneNum);
                    req.setCode(code);
                    req.setPassword(password);

                    reset(req);
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
                    findBackGetCode.setEnabled(false);
                    mHandler.sendEmptyMessage(BEGIN_TIMER);
                    ApiManager.clear();
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

    //重置密码
    private void reset(ResetReq req){

        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).resetPassword(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                        mHandler.removeMessages(BEGIN_TIMER);
                        showToast("密码重置成功，请重新登录~");
                        finish();
                        readyGoThenKill(LoginActivity.class);
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
            findBackUserName.requestFocus();
            return false;
        }
        return true;
    }

    //检测验证码密码是否输入正确
    private boolean isRestValid(String code,String password,String passwordConfirm){
        if (CommonUtils.isEmpty(code)) {
            showToast("请先输入您输入的验证码");
            findBackVerifyCode.requestFocus();
            return false;
        }
        if(CommonUtils.isEmpty(password)){
            showToast("请输入您设置的密码");
            findBackPwd.requestFocus();
            return false;
        }
        if(CommonUtils.isEmpty(passwordConfirm)){
            showToast("请再次输入您设置的密码");
            findBackPwdConfirm.requestFocus();
            return false;
        }
        if(!password.equals(passwordConfirm)){
            showToast("两次输入的密码不一致，请重新输入");
            findBackPwdConfirm.setText("");
            findBackPwdConfirm.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    //获取验证码的间隔限制
    public boolean handleMessage(Message message) {
        if(message.what == BEGIN_TIMER){
            if(timer==0){
                if(findBackGetCode!=null){
                    findBackGetCode.setText(text);
                    findBackGetCode.setEnabled(true);
                    timer=60;
                }
            }else{
                if(findBackGetCode!=null){
                    findBackGetCode.setText(timer + "秒后重新发送");
                    timer--;
                    mHandler.sendEmptyMessageDelayed(BEGIN_TIMER, 1000);
                }
            }
        }
        return true;
    }
}
