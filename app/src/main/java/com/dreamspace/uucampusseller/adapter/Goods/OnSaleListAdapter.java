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
public class OnSaleListAdapter extends BasisAdapter<SelfGoodItem,OnSaleListAdapter.ViewHolder> {
    private Context mContext;
    private OnGoodPullOffClickListener onGoodPullOffClickListener;
    private OnGoodEditClickListener onGoodEditClickListener;

    public OnSaleListAdapter(Context mContext, List<SelfGoodItem> mEntities, Class<ViewHolder> classType) {
        super(mContext, mEntities, classType);
        this.mContext = mContext;
    }

    @Override
    protected void setDataIntoView(ViewHolder holder, final SelfGoodItem entity) {
        holder.goodName.setText(entity.getName());
        CommonUtils.showImageWithGlide(mContext, holder.image, entity.getImage());
        holder.price.setText(mContext.getString(R.string.RMB) + entity.getPrice());
        holder.pullOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onGoodPullOffClickListener != null){
                    System.out.println("id:" + entity.getGoods_id());
                    onGoodPullOffClickListener.onPullOffClick(entity.getGoods_id(),getmEntities().indexOf(entity));
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
        holder.image = (ImageView) convertView.findViewById(R.id.sm_sale_good_image_iv);
        holder.goodName = (TextView) convertView.findViewById(R.id.sm_sale_good_name_tv);
//        holder.shopName = (TextView) convertView.findViewById(R.id.sm_sale_good_shop_name_tv);
//        holder.browseNum = (TextView) convertView.findViewById(R.id.sm_sale_good_browse_tv);
//        holder.preferential = (TextView) convertView.findViewById(R.id.sm_sale_good_preferential_tv);
        holder.price = (TextView) convertView.findViewById(R.id.sm_sale_good_price_tv);
        holder.pullOff = (Button) convertView.findViewById(R.id.pulloff_btn);
        holder.edit = (Button) convertView.findViewById(R.id.edit_btn);
    }

    @Override
    public int getItemLayout() {
        return R.layout.list_item_goods_sale;
    }

    public void setOnGoodPullOffClickListener(OnGoodPullOffClickListener onGoodPullOffClickListener) {
        this.onGoodPullOffClickListener = onGoodPullOffClickListener;
    }

    public void setOnGoodEditClickListener(OnGoodEditClickListener onGoodEditClickListener) {
        this.onGoodEditClickListener = onGoodEditClickListener;
    }

    public static class ViewHolder{
        ImageView image;
        TextView goodName;
//        TextView shopName;
//        TextView browseNum;
//        TextView preferential;
        TextView price;
        Button pullOff;
        Button edit;
    }

    public interface OnGoodPullOffClickListener{
        void onPullOffClick(String good_id,int position);
    }

    public interface OnGoodEditClickListener{
        void onEditClick(String good_id);
    }
}
