package com.dreamspace.uucampusseller.ui.fragment.Goods;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.Goods.CatalogListAdapter;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.ui.dialog.AddNewInfoDialog;
import com.dreamspace.uucampusseller.ui.dialog.GoodsClassifyDialog;
import com.dreamspace.uucampusseller.ui.dialog.GoodsClassifyWithAddDialog;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Lx on 2015/10/20.
 */
public class GoodsClassifyFragment extends BaseLazyFragment {
    @Bind(R.id.catalog_lv)
    ListView listView;
    @Bind(R.id.new_edit_catalog_ll)
    LinearLayout newEditCataLl;
    @Bind(R.id.finish_edit_btn)
    Button finishEditBtn;
    @Bind(R.id.new_catalog_btn)
    Button newCatalogBtn;
    @Bind(R.id.edit_catalog_btn)
    Button editCatalogBtn;

    private CatalogListAdapter catalogAdapter;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        initCatalogViews();
        initCatalogListeners();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_goods_classify;
    }

    private void initCatalogViews(){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0;i < 5;i++){
            list.add(i+"");
        }
        catalogAdapter = new CatalogListAdapter(mContext,list,CatalogListAdapter.ViewHolder.class);
        listView.setAdapter(catalogAdapter);
        newEditCataLl.setVisibility(View.VISIBLE);
        finishEditBtn.setVisibility(View.INVISIBLE);
    }

    private void initCatalogListeners(){
        editCatalogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            catalogAdapter.showEdit(true);
            }
        });

        newCatalogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createShowNewCataDialog();
            }
        });
    }

    private void createShowNewCataDialog(){
        final AddNewInfoDialog dialog = new AddNewInfoDialog(mContext,getString(R.string.add_new_catalog));
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            list.add(i + "");
        }
//        final GoodsClassifyWithAddDialog dialog = new GoodsClassifyWithAddDialog(mContext,list,getString(R.string.good_catalog));
        dialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
