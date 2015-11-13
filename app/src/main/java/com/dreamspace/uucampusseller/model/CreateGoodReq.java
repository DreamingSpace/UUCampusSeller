package com.dreamspace.uucampusseller.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lx on 2015/10/29.
 */
public class CreateGoodReq implements Parcelable{
    private String shop_id;
    private String name;
    private String image;
    private String label;
    private String group;
    private String original_price;
    private String price;
    private String discount;
    private String description;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateGoodReq(){}
    public CreateGoodReq(Parcel in){
        shop_id = in.readString();
        name = in.readString();
        image = in.readString();
        label = in.readString();
        group = in.readString();
        original_price = in.readString();
        price = in.readString();
        discount = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shop_id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(label);
        dest.writeString(group);
        dest.writeString(original_price);
        dest.writeString(price);
        dest.writeString(discount);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<CreateGoodReq> CREATOR = new Creator<CreateGoodReq>() {
        @Override
        public CreateGoodReq createFromParcel(Parcel source) {
            return new CreateGoodReq(source);
        }

        @Override
        public CreateGoodReq[] newArray(int size) {
            return new CreateGoodReq[size];
        }
    };
}
