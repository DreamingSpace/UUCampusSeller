package com.dreamspace.uucampusseller.model.api;

import com.dreamspace.uucampusseller.model.Buyer;
import com.dreamspace.uucampusseller.model.GoodItem;

/**
 * Created by wufan on 2015/11/5.
 */
public class GetShopOrderDetailRes {

    private int status;
    private String remark;
    private GoodItem good;
    private String use_card;
    private String time;
    private Buyer buyer;
    private float total_price;
    private String _id;
    private int quantity;
    private float total_discount;
    private float total_original;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public GoodItem getGood() {
        return good;
    }

    public void setGood(GoodItem good) {
        this.good = good;
    }

    public String getUse_card() {
        return use_card;
    }

    public void setUse_card(String use_card) {
        this.use_card = use_card;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(float total_discount) {
        this.total_discount = total_discount;
    }

    public float getTotal_original() {
        return total_original;
    }

    public void setTotal_original(float total_original) {
        this.total_original = total_original;
    }
}
