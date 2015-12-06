package com.dreamspace.uucampusseller.ui.activity.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wufan on 2015/10/23.
 */
public class ApplyShopHintActivity extends AbsActivity{
    @Bind(R.id.apply_shop_text_view)
    TextView mApplyShopTv;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_shop_hint;
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.apply_shop_text_view)
    void applyShop(){
        readyGo(ApplyShopFirstActivity.class);
    }

    BroadcastReceiver broadcastReceiver =new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();             //接收销毁该activity时销毁此activity
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //在当前activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("destroyActivity");
        this.registerReceiver(this.broadcastReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.broadcastReceiver);
    }
}
