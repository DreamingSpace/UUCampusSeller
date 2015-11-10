package com.dreamspace.uucampusseller.api;


import com.dreamspace.uucampusseller.model.api.CheckUpdateRes;
import com.dreamspace.uucampusseller.model.api.CommitSuggestionRes;
import com.dreamspace.uucampusseller.model.api.ContentReq;
import com.dreamspace.uucampusseller.model.api.CreateShopReq;
import com.dreamspace.uucampusseller.model.api.CreateShopRes;
import com.dreamspace.uucampusseller.model.api.GetOrderStatusRes;
import com.dreamspace.uucampusseller.model.api.GetShopOrderDetailRes;
import com.dreamspace.uucampusseller.model.api.GetShopOrderListRes;
import com.dreamspace.uucampusseller.model.api.LoginReq;
import com.dreamspace.uucampusseller.model.api.LoginRes;
import com.dreamspace.uucampusseller.model.api.QnRes;
import com.dreamspace.uucampusseller.model.api.RegisterReq;
import com.dreamspace.uucampusseller.model.api.RegisterRes;
import com.dreamspace.uucampusseller.model.api.ResetReq;
import com.dreamspace.uucampusseller.model.api.SendVerifyReq;
import com.dreamspace.uucampusseller.model.api.ShopInfoRes;
import com.dreamspace.uucampusseller.model.api.ShopStatusRes;
import com.dreamspace.uucampusseller.model.api.UpdateShopInfoReq;
import com.dreamspace.uucampusseller.model.api.UserInfoRes;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public interface UUService {

    //创建七牛上传凭证
    @POST("/static/token/")
    void createQiNiuToken(Callback<QnRes> cb);

    //给指定手机发送短信验证码
    @POST("/auth/code/")
    void sendVerifyCode(@Body SendVerifyReq req, Callback<Response> cb);

    //用户
    //创建用户访问凭证(登陆)
    @POST("/auth/login/")
    void createAccessToken(@Body LoginReq req, Callback<LoginRes> cb);

    //用户创建
    @POST("/user/")
    void register(@Body RegisterReq req, Callback<RegisterRes> cb);

    //用户密码重置
    @PUT("/user/reset_password/")
    void resetPassword(@Body ResetReq req, Callback<Response> cb);

    //用户信息查看（自己）
    @GET("/user/")
    void getUserInfo(Callback<UserInfoRes> cb);

    //店铺
    //获取自己的店铺状态
    @GET("/shop/status/")
    void getShopStatus(Callback<ShopStatusRes> cb);
    //店铺创建
    @POST("/shop/")
    void createShop(@Body CreateShopReq req, Callback<CreateShopRes> cb);
    //店铺信息更新
    @PUT("/shop/")
    void updateShopInfo(@Body UpdateShopInfoReq req,Callback<Response> cb);
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

    //意见、报告、更新等
    //意见提交
    @POST("/suggestion/")
    void commitSuggestion(@Body ContentReq req,Callback<CommitSuggestionRes>cb);

    //检查更新
    @GET("/check_update/{version}")
    void checkUpdate(@Path("version")float version,Callback<CheckUpdateRes>cb);
}
