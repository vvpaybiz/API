package com.test.pay;


import java.util.HashMap;
import java.util.Map;

/**
 * 查询报价
 */
public class Quote {
    public static void main(String[] args) {
        getUsdtPrice("CNY",660d);
    }

    public static String getUsdtPrice(String fiatCurrency,Double amount){
        //curl -X GET -H "content-type:application/json" -H "access_key:8A42FADDD2397F4841B1F85C2C13094CAB152151" -H "app_id:41043343" http://merchant-api.vvpay.me/api/recharge/convert/v1?timestamp=1609385362&p1=9357&p2=CNY&p3=695
        long timestamp = System.currentTimeMillis()/1000;
        String p1 = Constant.merchant_no;
        String p2 = fiatCurrency;
        Double p3 = amount;  //CNY的数量
        String pay_url = Constant.URL_ + "api/recharge/convert/v1?p1="+p1+"&p2=" + p2 + "&p3=" + p3 +"&timestamp="+timestamp;
        Map<String, String> heads = new HashMap<>();
        heads.put("content-type", "application/json");
        String params = p1+"&"+timestamp;
        String access_key = HMACSHA1.hamcsha1(params.getBytes(), Constant.key.getBytes());
        heads.put("access_key", access_key);
        heads.put("app_id", Constant.app_id);
        System.out.println("pay-------->"+pay_url);
        String return_url = HttpUtils.get(pay_url, heads);
        System.out.println(return_url);
        //{"code":200,"data":{"price":6.46},"message":"success"}
        return return_url;
    }
}
