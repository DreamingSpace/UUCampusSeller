package com.dlazaro66.qrcodereaderview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.QRCodeReaderView.R;

/**
 * Created by Lx on 2015/11/30.
 */
public class QRCodeView extends FrameLayout{
    private QRCodeReaderView qrCodeReaderView;
    private ScanBoxView scanBoxView;
    private QRCodeReaderView.OnQRCodeReadListener onQRCodeReadListener;

    public QRCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public QRCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public QRCodeView(Context context) {
        super(context);
        qrCodeReaderView = new QRCodeReaderView(context);
        scanBoxView = new ScanBoxView(context);
    }

    private void init(Context context,AttributeSet attrs){
        qrCodeReaderView = new QRCodeReaderView(context);
        qrCodeReaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scanBoxView = new ScanBoxView(context);
        this.addView(qrCodeReaderView);
        this.addView(scanBoxView);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QRCodeView);
        scanBoxView.setCornerColor(typedArray.getColor(R.styleable.QRCodeView_cornerColor, Color.parseColor("#76EE00")));
        scanBoxView.setCornerLength(typedArray.getDimensionPixelOffset(R.styleable.QRCodeView_cornerLength, DisplayUtils.dp2px(context, 20)));
        scanBoxView.setCornerSize(typedArray.getDimensionPixelOffset(R.styleable.QRCodeView_cornerSize, DisplayUtils.dp2px(context, 4)));
        scanBoxView.setMaskColor(typedArray.getColor(R.styleable.QRCodeView_maskColor, Color.parseColor("#60000000")));
        scanBoxView.setRectWidth(typedArray.getDimensionPixelOffset(R.styleable.QRCodeView_rectWidth, DisplayUtils.dp2px(context, 200)));
        scanBoxView.setScanLineColor(typedArray.getColor(R.styleable.QRCodeView_scanLineColor, Color.parseColor("#76EE00")));
        scanBoxView.setScanLineSize(typedArray.getDimensionPixelOffset(R.styleable.QRCodeView_scanLineSize, DisplayUtils.dp2px(context, 2)));
        scanBoxView.setTopOffset(typedArray.getDimensionPixelOffset(R.styleable.QRCodeView_topOffset,DisplayUtils.dp2px(context, 80)));
        typedArray.recycle();
    }

    public void setOnQRCodeReadListener(QRCodeReaderView.OnQRCodeReadListener onQRCodeReadListener) {
        if(qrCodeReaderView != null){
            qrCodeReaderView.setOnQRCodeReadListener(onQRCodeReadListener);
        }
    }
}
