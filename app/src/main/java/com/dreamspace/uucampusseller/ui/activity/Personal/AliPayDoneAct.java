package com.dreamspace.uucampusseller.ui.activity.Personal;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by money on 2015/11/9.
 */
public class AliPayDoneAct extends AbsActivity {
    @Bind(R.id.ali_pay_acount_text)
    TextView aliPayAcountText;
    @Bind(R.id.ali_pay_up_button)
    Button aliPayUpButton;

    @Override
    protected int getContentView() {
        return R.layout.activity_ali_pay_done;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        aliPayUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoThenKill(AliPayAct.class);
            }
        });
    }
}
