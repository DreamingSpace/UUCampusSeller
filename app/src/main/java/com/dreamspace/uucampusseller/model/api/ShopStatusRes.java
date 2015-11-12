package com.dreamspace.uucampusseller.model.api;

/**
 * Created by money on 2015/11/9.
 */
public class ShopStatusRes {
    private String shop_id;
    private String status;
    private String category;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }
}
