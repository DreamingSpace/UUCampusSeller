package com.dreamspace.uucampusseller.adapter.base.order;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.base.BasisAdapter;
import com.dreamspace.uucampusseller.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class OrderItemAdapter extends BasisAdapter<OrderItem, OrderItemAdapter.viewHolder> {

    public OrderItemAdapter(Context context){
        super(context,new ArrayList<OrderItem>(),viewHolder.class);
    }
    public OrderItemAdapter(List<OrderItem> mFreeGoodsInfoList, Context context) {
        super(context, mFreeGoodsInfoList, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, OrderItem entity) {
//        holder.mTextView.setText(entity.getCourseName());
//        holder.mTextView.setText("Hello Tab!");
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
//        holder.mTextView = (TextView) convertView.findViewById(R.id.course_name_tv);
    }

    @Override
    public int getItemLayout() {
        return R.layout.list_item_order;
    }

    public static class viewHolder {

        public TextView mTextView;
    }

}

