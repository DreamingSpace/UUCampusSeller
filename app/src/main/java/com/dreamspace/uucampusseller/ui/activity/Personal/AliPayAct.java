package com.dreamspace.uucampusseller.ui.activity.Personal;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

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
                uploadAli();
                readyGoThenKill(AliPayWaitAct.class);
            }
        });
    }

    //提交支付宝账号
    private void uploadAli(){

    }

    //检查两次输入是否一致
    private void ifValid(){

    }

}
