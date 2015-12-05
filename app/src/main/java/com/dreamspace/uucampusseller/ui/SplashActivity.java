package com.dreamspace.uucampusseller.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.ui.activity.Login.LoginActivity;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
                intent = (!PreferenceUtils.hasKey(SplashActivity.this,PreferenceUtils.Key.ACCESS)
                        || CommonUtils.isEmpty(PreferenceUtils.getString(SplashActivity.this,PreferenceUtils.Key.ACCESS)))?
                        new Intent(SplashActivity.this, LoginActivity.class) :
                        new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            }
        }, 2000);
    }
}
