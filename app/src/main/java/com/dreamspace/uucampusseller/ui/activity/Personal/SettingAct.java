package com.dreamspace.uucampusseller.ui.activity.Personal;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.ui.MainActivity;
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
                PreferenceUtils.putString(SettingAct.this, PreferenceUtils.Key.ACCESS, "");
                Intent intent = new Intent();
                intent.setAction(MainActivity.ACTION_FINISH);
                intent.putExtra(MainActivity.LOGOUT,"logout");
                LocalBroadcastManager.getInstance(SettingAct.this).sendBroadcast(intent);
                readyGoThenKill(LoginActivity.class);
            }
        });
    }

}
