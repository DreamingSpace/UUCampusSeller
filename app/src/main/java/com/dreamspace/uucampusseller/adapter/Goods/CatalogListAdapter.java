package com.dreamspace.uucampusseller.adapter.Goods;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.base.BasisAdapter;
import com.dreamspace.uucampusseller.common.utils.NetUtils;

import java.util.List;

/**
 * Created by Lx on 2015/9/23.
 */
public class CatalogListAdapter extends BasisAdapter<String,CatalogListAdapter.ViewHolder> {
    private boolean showEdit = false;
    private OnGroupDeleteClickListener deleteListener;
    private OnGroupEditClickListener editListener;

    public CatalogListAdapter(Context mContext, List<String> mEntities, Class<ViewHolder> classType) {
        super(mContext, mEntities, classType);
    }

    public void showEdit(boolean showEdit){
        this.showEdit = showEdit;
        notifyDataSetChanged();
    }

    @Override
    protected void setDataIntoView(ViewHolder holder, String entity) {
        if(showEdit){
            holder.editLl.setVisibility(View.VISIBLE);
        }else{
            holder.editLl.setVisibility(View.GONE);
        }
        holder.catalogInfo.setText(entity);
        setListeners(holder,entity);
    }

    private void setListeners(ViewHolder holder, final String entity){
        holder.catalogEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editListener != null){
                    editListener.onEditClick(entity);
                }
            }
        });

        holder.catalogDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteListener != null){
                    deleteListener.onGoodDeleteClick(entity);
                }
            }
        });
    }

    @Override
    protected void initViewHolder(View convertView, ViewHolder holder) {
        holder.catalogInfo = (TextView) convertView.findViewById(R.id.catalog_info_tv);
        holder.catalogEdit = (ImageView) convertView.findViewById(R.id.catalog_edit_iv);
        holder.catalogDelete = (ImageView) convertView.findViewById(R.id.catalog_delete_iv);
        holder.editLl = (LinearLayout) convertView.findViewById(R.id.catalog_edit_delete_ll);
    }

    @Override
    public int getItemLayout() {
        return R.layout.catalog_list_item;
    }

    public void setDeleteClickListener(OnGroupDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setEditClickListener(OnGroupEditClickListener editListener) {
        this.editListener = editListener;
    }

    public static class ViewHolder{
        public TextView catalogInfo;
        public ImageView catalogEdit;
        public ImageView catalogDelete;
        public LinearLayout editLl;
    }

    //分类删除点击回调
    public interface OnGroupDeleteClickListener{
        void onGoodDeleteClick(String groupName);
    }

    //分类编辑点击回调
    public interface OnGroupEditClickListener{
        void onEditClick(String groupName);
    }
}
