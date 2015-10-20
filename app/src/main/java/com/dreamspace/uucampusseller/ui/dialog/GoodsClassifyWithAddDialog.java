package com.dreamspace.uucampusseller.ui.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.widget.WheelView;

import java.util.ArrayList;

/**
 * Created by Lx on 2015/10/20.
 */
public class GoodsClassifyWithAddDialog {
    private WheelView wheelView;
    private LinearLayout confirmLl;
    private LinearLayout cancelLl;
    private ImageView addNewIv;
    private TextView titleTv;
    private Context mContext;
    private ArrayList<String> classifyList;
    private String title;
    private AlertDialog dialog;

    public GoodsClassifyWithAddDialog(Context context,ArrayList<String> data,String title){
        mContext = context;
        this.title = title;
        classifyList = data;
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.alterdialog_catalog,null);
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setView(dialogView);

        wheelView = (WheelView) dialogView.findViewById(R.id.dialog_wheelview);
        confirmLl = (LinearLayout) dialogView.findViewById(R.id.confirm_ll);
        cancelLl = (LinearLayout) dialogView.findViewById(R.id.cancel_ll);
        addNewIv = (ImageView) dialogView.findViewById(R.id.new_iv);

        wheelView.setDefault(1);
        wheelView.setData(classifyList);
    }

    public String getSelected(){
        return wheelView.getSelectedText();
    }

    public void show(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }

    public void setPositiveButton(View.OnClickListener listener){
        confirmLl.setOnClickListener(listener);
    }

    public void setNegativeButton(View.OnClickListener listener){
        cancelLl.setOnClickListener(listener);
    }

    public void setAddButton(View.OnClickListener listener){
        addNewIv.setOnClickListener(listener);
    }
}
