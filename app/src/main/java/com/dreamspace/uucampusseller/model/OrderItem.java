package com.dreamspace.uucampusseller.model;

/**
 * Created by wufan on 2015/10/21.
 */
public class OrderItem {

    private int status;
    private GoodItem good;
    private String time;
    private Buyer buyer;
    private float total_price;
    private String _id;
    private String change_time;
    private int quantity;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GoodItem getGood() {
        return good;
    }

    public void setGood(GoodItem good) {
        this.good = good;
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

    public String getChange_time() {
        return change_time;
    }

    public void setChange_time(String change_time) {
        this.change_time = change_time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
