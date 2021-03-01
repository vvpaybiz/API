package com.test.pay;

/**
 * @Author wanglele
 * @Date 2020/12/30 14:41
 * @Version 1.0
 */
public class Constant {
    public final static String URL_ = "http://merchant-api.vvpay.me/"; // 域名
    public final static String key = "73A8946D4ED1AD34ADA3F503601D3833"; // HMAC_SHA1加密算法的key(密钥) <由 VVPay 分配,在app-API配置(pc商户后台--账户中心--API安全--密钥) 中获取>
    public final static String app_id = "41043343"; // 商户帐号ID(APPID) <由 VVPay 分配,在app-API配置(pc在商户后台--账户中心--商户信息) 中获取>
    public final static String merchant_no = "9357"; //商户号(UID) <由 VVPay 分配,在app-API配置(pc商户后台--账户中心--商户信息) 中获取>
}
