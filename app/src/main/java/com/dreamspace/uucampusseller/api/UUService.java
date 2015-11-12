package com.dreamspace.uucampusseller.api;


import com.dreamspace.uucampusseller.model.CommonStatusRes;
import com.dreamspace.uucampusseller.model.CreateGoodReq;
import com.dreamspace.uucampusseller.model.CreateGoodRes;
import com.dreamspace.uucampusseller.model.GetGroupsRes;
import com.dreamspace.uucampusseller.model.GetSelfGoodsRes;
import com.dreamspace.uucampusseller.model.GoodDetailRes;
import com.dreamspace.uucampusseller.model.GroupCreateReq;
import com.dreamspace.uucampusseller.model.GroupCreateRes;
import com.dreamspace.uucampusseller.model.GroupDeleteReq;
import com.dreamspace.uucampusseller.model.GroupDeleteRes;
import com.dreamspace.uucampusseller.model.LabelRes;
import com.dreamspace.uucampusseller.model.QnRes;
import com.dreamspace.uucampusseller.model.SearchGoodsRes;
import com.dreamspace.uucampusseller.model.UpdateGoodReq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.RestMethod;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public interface UUService {
    //DELETE方法默认不支持含body，自定义DELETE方法
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @RestMethod(value = "DELETE" , hasBody = true)
    @interface DELETE{
        String value();
    }

    //创建七牛上传凭证
    @POST("/static/token/")
    void createQiNiuToken(Callback<QnRes> cb);

    @POST("/goods/")
    void createGood(@Body CreateGoodReq goodReq, Callback<CreateGoodRes> cb);

    @GET("/goods/{goods_id}")
    void getGoodDetail(@Path("goods_id")String goods_id,Callback<GoodDetailRes> cb);

    @GET("/label/")
    void getLabels(@Query("category")String category,Callback<LabelRes> cb);

    @GET("/shop/{shop_id}/group/")
    void getGroups(@Path("shop_id")String shop_id,Callback<GetGroupsRes> cb);

    @PUT("/shop/{shop_id}/group/")
    void deleteGroup(@Path("shop_id") String shop_id,@Body GroupDeleteReq groupDeleteReq,Callback<GroupDeleteRes> cb);

    @POST("/shop/{shop_id}/group/")
    void createGroup(@Path("shop_id") String shop_id,@Body GroupCreateReq groupCreateReq,Callback<GroupCreateRes> cb);

    @GET("/goods/search/")
    void searchGoods(@Query("keyword")String keyword,@Query("order")String order,@Query("category")String category,
                     @Query("label")String label,@Query("group")String group,@Query("shop_id")String shop_id,
                     @Query("page")int page,@Query("location")String location,Callback<SearchGoodsRes> cb);

    @GET("/goods/list/")
    void getSelfGoods(@Query("page")int page,@Query("is_active")int is_active,@Query("group")String group,Callback<GetSelfGoodsRes> cb);

    @PUT("/goods/{goods_id}")
    void updateGoodInfo(@Path("goods_id")String good_id,@Body UpdateGoodReq goodReq,Callback<CommonStatusRes> cb);

    @DELETE("/goods/{goods_id}")
    void deleteGood(@Path("goods_id")String good_id,Callback<CommonStatusRes> cb);
}
