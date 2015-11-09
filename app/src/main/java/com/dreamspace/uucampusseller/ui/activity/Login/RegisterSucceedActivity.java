package com.dreamspace.uucampusseller.ui.activity.Login;

import android.view.View;
import android.widget.Button;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by money on 2015/11/9.
 */
public class RegisterSucceedActivity extends AbsActivity {
    @Bind(R.id.register_succeed_btn)
    Button registerSucceedBtn;

    @Override
    protected int getContentView() {
        return R.layout.activity_register_succeed;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        registerSucceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoThenKill(LoginActivity.class);
            }
        });
    }
}
