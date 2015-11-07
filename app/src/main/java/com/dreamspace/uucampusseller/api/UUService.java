package com.dreamspace.uucampusseller.api;


import com.dreamspace.uucampusseller.model.api.CreateShopReq;
import com.dreamspace.uucampusseller.model.api.CreateShopRes;
import com.dreamspace.uucampusseller.model.api.GetOrderStatusRes;
import com.dreamspace.uucampusseller.model.api.GetShopOrderDetailRes;
import com.dreamspace.uucampusseller.model.api.GetShopOrderListRes;
import com.dreamspace.uucampusseller.model.api.QnRes;
import com.dreamspace.uucampusseller.model.api.ShopInfoRes;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public interface UUService {

    //创建七牛上传凭证
    @POST("/static/token/")
    void createQiNiuToken(Callback<QnRes> cb);

    //店铺
    //店铺创建
    @POST("/shop/")
    void createShop(@Body CreateShopReq req, Callback<CreateShopRes> cb);
    //店铺信息查看
    @GET("/shop/{shop_id}")
    void getShopInfo(@Path("shop_id") String shop_id, Callback<ShopInfoRes> cb);


    //订单
    //商家每个状态的订单状态和订单数量
    @GET("/shop/orders/status/")
    void getOrderStatus(Callback<GetOrderStatusRes> cb);

    //商家获取订单列表
    @GET("/shop/orders/")
    void getShopOrderList(@Query("page")int page,@Query("status")int status,Callback<GetShopOrderListRes>cb);

    //商家获取订单详情
    @GET("/shop/order/{order_id}/")
    void getShopOrderDetail(@Path("order_id")String order_id,Callback<GetShopOrderDetailRes>cb);


}
