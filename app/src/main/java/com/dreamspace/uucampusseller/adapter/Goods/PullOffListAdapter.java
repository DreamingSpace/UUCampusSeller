package com.dreamspace.uucampusseller.adapter.Goods;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.base.BasisAdapter;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.model.SelfGoodItem;

import java.util.List;

/**
 * Created by Lx on 2015/9/24.
 */
public class PullOffListAdapter extends BasisAdapter<SelfGoodItem,PullOffListAdapter.ViewHolder> {
    private Context mContext;
    private OnGoodSaleClickListener onGoodSaleClickListener;
    private OnGoodEditClickListener onGoodEditClickListener;
    public PullOffListAdapter(Context mContext, List<SelfGoodItem> mEntities, Class<ViewHolder> classType) {
        super(mContext, mEntities, classType);
        this.mContext = mContext;
    }

    @Override
    protected void setDataIntoView(ViewHolder holder, final SelfGoodItem entity) {
        holder.goodName.setText(entity.getName());
        CommonUtils.showImageWithGlide(mContext, holder.image, entity.getImage());
        holder.price.setText(mContext.getString(R.string.RMB) + entity.getPrice());
        holder.sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onGoodSaleClickListener != null){
                    onGoodSaleClickListener.onSaleClick(entity.getGoods_id(), getmEntities().indexOf(entity));
                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onGoodEditClickListener != null){
                    onGoodEditClickListener.onEditClick(entity.getGoods_id());
                }
            }
        });
    }

    @Override
    protected void initViewHolder(View convertView, ViewHolder holder) {
        holder.image = (ImageView) convertView.findViewById(R.id.sm_pulloff_good_image_iv);
        holder.goodName = (TextView) convertView.findViewById(R.id.sm_pulloff_good_name_tv);
//        holder.shopName = (TextView) convertView.findViewById(R.id.sm_pulloff_good_shop_name_tv);
//        holder.browseNum = (TextView) convertView.findViewById(R.id.sm_pulloff_good_browse_tv);
//        holder.likeNum = (TextView) convertView.findViewById(R.id.sm_pulloff_good_like_tv);
//        holder.preferential = (TextView) convertView.findViewById(R.id.sm_pulloff_good_preferential_tv);
        holder.price = (TextView) convertView.findViewById(R.id.good_price_tv);
        holder.sale = (Button) convertView.findViewById(R.id.sale_btn);
        holder.edit = (Button) convertView.findViewById(R.id.edit_btn);
    }

    @Override
    public int getItemLayout() {
        return R.layout.list_item_goods_pull_off;
    }

    public void setOnGoodSaleClickListener(OnGoodSaleClickListener onGoodSaleClickListener) {
        this.onGoodSaleClickListener = onGoodSaleClickListener;
    }

    public void setOnGoodEditClickListener(OnGoodEditClickListener onGoodEditClickListener) {
        this.onGoodEditClickListener = onGoodEditClickListener;
    }

    public static class ViewHolder{
        ImageView image;
        TextView goodName;
//        TextView shopName;
//        TextView likeNum;
//        TextView browseNum;
//        TextView preferential;
        TextView price;
        Button sale;
        Button edit;
    }

    public interface OnGoodSaleClickListener{
        void onSaleClick(String good_id,int position);
    }

    public interface OnGoodEditClickListener{
        void onEditClick(String good_id);
    }
}
