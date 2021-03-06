package com.dreamspace.uucampusseller.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.widget.WheelView;

import java.util.ArrayList;

/**
 * Created by wufan on 2015/9/22.
 */
public class GoodsClassifyDialog {

    private Button commitButton;
    private Button cancelButton;
    public String classifyName = "商品分类";
    private TextView titleTv;
    private android.app.AlertDialog ad;
    private WheelView goodsClassifyWV;
    private Context context;
    private ArrayList<String> classifys;

    public GoodsClassifyDialog(Context context,ArrayList<String>classifys) {
        this.context = context;
        this.classifys=classifys;
        ad = new android.app.AlertDialog.Builder(context).create();
        ad.show();
        Window window = ad.getWindow();
        window.setContentView(R.layout.dialog_choose_goods_classify);
        goodsClassifyWV = (WheelView) window
                .findViewById(R.id.goods_classify_wheel);

        goodsClassifyWV.setDefault(1);
        goodsClassifyWV.setData(classifys);

        commitButton = (Button) window
                .findViewById(R.id.goods_classify_ensure_btn);
        cancelButton = (Button) window
                .findViewById(R.id.goods_classify_cancel_btn);
    }



    //设置选中的item
    public void setClassifyName() {
        classifyName =classifys.get(goodsClassifyWV.getSelected());
    }

    public String getClassifyName(){
        return classifyName;
    }

    public void setTopTitle(String title) {
        titleTv.setText(title);
    }

    /**
     * 关闭
     */
    public void dismiss() {
        ad.dismiss();
    }

    /**
     * 设置按钮
     *
     * @param text
     * @param listener
     */
    public void setPositiveButton(String text,
                                  final View.OnClickListener listener) {
        commitButton.setText(text);
        commitButton.setOnClickListener(listener);
    }

    /**
     * 设置按钮
     *
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text,
                                  final View.OnClickListener listener) {
        cancelButton.setText(text);
        cancelButton.setOnClickListener(listener);
    }

}
