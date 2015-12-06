package com.dreamspace.uucampusseller.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.ui.activity.Login.LoginActivity;

/**
 * Created by Lx on 2015/11/28.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                //如果没有申请店铺则跳转到登录界面
                if(PreferenceUtils.hasKey(SplashActivity.this,PreferenceUtils.Key.SHOP_ID)){
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
        }, 2000);
    }
}
