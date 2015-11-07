package com.dreamspace.uucampusseller.model.api;

import com.dreamspace.uucampusseller.model.OrderItem;

import java.util.List;

/**
 * Created by wufan on 2015/11/5.
 */
public class GetShopOrderListRes {

    private List<OrderItem> orders;

    public List<OrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }
}
