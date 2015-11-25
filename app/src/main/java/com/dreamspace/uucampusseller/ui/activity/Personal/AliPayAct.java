package com.dreamspace.uucampusseller.ui.activity.Personal;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.model.api.ShopBindReq;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.ui.dialog.ProgressDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by money on 2015/11/9.
 */
public class AliPayAct extends AbsActivity {
    @Bind(R.id.ali_pay_account)
    EditText aliPayAccount;
    @Bind(R.id.ali_pay_again)
    EditText aliPayAgain;
    @Bind(R.id.ali_pay_button)
    Button aliPayButton;

    @Override
    protected int getContentView() {
        return R.layout.activity_ali_pay;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        aliPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alipay = aliPayAccount.getText().toString();
                String confirm = aliPayAgain.getText().toString();
                if(ifValid(alipay,confirm)){
                    uploadAli(alipay);
                }
            }
        });
    }

    //提交支付宝账号
    private void uploadAli(String alipay){
        if(NetUtils.isNetworkConnected(this)){
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setContent("正在提交");
            pd.show();

            ShopBindReq shopBindReq = new ShopBindReq();
            shopBindReq.setAlipay(alipay);
            ApiManager.getService(getApplicationContext()).putShopBind(shopBindReq, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    pd.dismiss();
                    showToast("支付宝信息提交成功");
                    //跳转到审核中的界面
                    readyGoThenKill(AliPayWaitAct.class);
                }

                @Override
                public void failure(RetrofitError error) {
                    pd.dismiss();
                    showInnerError(error);
                }
            });
        }else {
            showNetWorkError();
        }
    }

    //检查两次输入是否一致
    private boolean ifValid(String alipay,String confirm){
        if(alipay.equals(confirm)){
            return true;
        }else{
            showToast("两次输入的值不一致");
            return false;
        }
    }

}
