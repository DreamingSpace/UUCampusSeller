package com.dreamspace.uucampusseller.ui.activity.order;

import android.graphics.PointF;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeView;
import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.TLog;
import com.dreamspace.uucampusseller.model.api.ConsumeOrderReq;
import com.dreamspace.uucampusseller.ui.MainActivity;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.ui.dialog.MsgDialog;

import butterknife.Bind;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lx on 2015/11/30.
 */
public class QRcodeScanAct extends AbsActivity{
    @Bind(R.id.qr)
    QRCodeView qrCodeView;

    private MsgDialog dialog;
    private ConsumeOrderReq req=new ConsumeOrderReq();

    @Override
    protected int getContentView() {
        return R.layout.activity_qrcode_scan;
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        if(dialog==null){
            dialog=new MsgDialog(this);
        }
        dialog.setContent("已消费订单！");
        dialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                readyGo(MainActivity.class);
            }
        });
        dialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        qrCodeView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                consumeOrder(text);
            }

            @Override
            public void cameraNotFound() {
                showToast("设备没有相机");
            }

            @Override
            public void QRCodeNotFoundOnCamImage() {

            }
        });
    }

    //商家消费订单
    private void consumeOrder(String resultString) {
        req.setCode(resultString);
        if(NetUtils.isNetworkConnected(getApplicationContext())) {
            ApiManager.getService(getApplicationContext()).consumeOrder(req, new retrofit.Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    TLog.i("success:", response2.getReason() + response2.getBody());
                    dialog.show();
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
}
