package com.dreamspace.uucampusseller.common;

import com.dreamspace.uucampusseller.model.api.ShopInfoRes;
import com.dreamspace.uucampusseller.model.api.ShopStatusRes;
import com.dreamspace.uucampusseller.model.api.UserInfoRes;

/**
 * Created by Lx on 2015/10/13.
 */
public class SharedData {

    public static final String[] orderTabs = new String []{
            "未付款",
            "未消费",
            "已完成",
            "退款"
    };
    public static final String goodsClassify[] = new String[] { "旅游", "语言",
            "驾校", "服装", "个人小店"};

    //登录后获取的用户信息保存的实体类
    public static UserInfoRes user;
    public static ShopStatusRes shopStatus;
    public static ShopInfoRes shopInfo;
    //支付宝账号和支付宝绑定状态
    //绑定状态：-1（未提交），0（审核中），1（审核通过）
    public static String alipay;
    public static int status = -1;
}
