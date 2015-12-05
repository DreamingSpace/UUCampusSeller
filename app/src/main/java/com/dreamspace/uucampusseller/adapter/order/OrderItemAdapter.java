package com.dreamspace.uucampusseller.adapter.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.base.BasisAdapter;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class OrderItemAdapter extends BasisAdapter<OrderItem, OrderItemAdapter.viewHolder> {

    private String phoneNum=null;

    public OrderItemAdapter(Context context){
        super(context,new ArrayList<OrderItem>(),viewHolder.class);
    }
    public OrderItemAdapter(List<OrderItem> mOrderItemList, Context context) {
        super(context, mOrderItemList, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, OrderItem entity) {
        holder.time.setText(" : "+entity.getTime());
        CommonUtils.showImageWithGlide(getmContext(), holder.image, entity.getGood().getImage());
        holder.name.setText(entity.getGood().getName());
        holder.price.setText(" : "+String.valueOf(entity.getTotal_original()/100));
        holder.number.setText(" : "+String.valueOf(entity.getQuantity()));
        holder.phone.setText(" : "+entity.getBuyer().getPhone_num());

        phoneNum=entity.getBuyer().getPhone_num();

        holder.connectRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent("android.intent.action.DIAL",
                        Uri.parse("tel:" + phoneNum));
                getmContext().startActivity(phoneIntent);
            }
        });
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.time = (TextView) convertView.findViewById(R.id.order_time_tv);
        holder.image = (ImageView) convertView.findViewById(R.id.order_image);
        holder.name = (TextView) convertView.findViewById(R.id.order_name_tv);
        holder.price = (TextView) convertView.findViewById(R.id.order_price_tv);
        holder.number = (TextView) convertView.findViewById(R.id.order_num_tv);
        holder.phone = (TextView) convertView.findViewById(R.id.order_phone_tv);
        holder.connectRl = (RelativeLayout) convertView.findViewById(R.id.order_connect_user_relative_layout);
    }

    @Override
    public int getItemLayout() {
        return R.layout.list_item_order;
    }

    public static class viewHolder {
        private TextView time;
        private ImageView image;
        private TextView name;
        private TextView price;
        private TextView number;
        private TextView phone;
        private RelativeLayout connectRl;
    }

}

