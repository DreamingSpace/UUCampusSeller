package com.dreamspace.uucampusseller.ui.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;

/**
 * Created by Lx on 2015/10/20.
 */
public class AddNewInfoDialog {
    private LinearLayout confirmLl;
    private LinearLayout cancelLl;
    private TextView titleTv;
    private EditText infoEt;
    private AlertDialog dialog;
    private Context mContext;
    private String title;

    public AddNewInfoDialog(Context context,String title){
        mContext = context;
        this.title = title;

        dialog = new AlertDialog.Builder(mContext).create();
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.alterdialog_new_catalog,null);
        dialog.setView(dialogView);

        titleTv = (TextView) dialogView.findViewById(R.id.title_tv);
        confirmLl = (LinearLayout) dialogView.findViewById(R.id.new_catalog_confirm_ll);
        cancelLl = (LinearLayout) dialogView.findViewById(R.id.new_catalog_cancel_ll);
        infoEt = (EditText) dialogView.findViewById(R.id.new_catalog_et);
    }

    public String getText(){
        return infoEt.getText().toString();
    }

    public void setPositiveButton(View.OnClickListener listener){
        confirmLl.setOnClickListener(listener);
    }

    public void setNegativeButton(View.OnClickListener listener){
        cancelLl.setOnClickListener(listener);
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public void show(){
        dialog.show();
    }
}
