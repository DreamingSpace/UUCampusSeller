package com.dreamspace.uucampusseller.ui.activity.Personal;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.activity.Login.LoginActivity;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by money on 2015/11/10.
 */
public class SettingAct extends AbsActivity {
    @Bind(R.id.check_update_rl)
    RelativeLayout checkUpdateRl;
    @Bind(R.id.logout_btn)
    Button logoutBtn;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        initListeners();
    }

    private void initListeners(){
        checkUpdateRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检查更新界面
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoThenKill(LoginActivity.class);
            }
        });
    }

}
