package com.test.pay;


import java.util.HashMap;
import java.util.Map;

/**
 * 查询入金订单状态
 */
public class OrderQuery {


    public static void main(String[] args) {
        orderQuery("PAY1600328923");
    }

    public static String orderQuery(String merchantOrderNo){
        long timestamp = System.currentTimeMillis()/1000;
        String pay_url = Constant.URL_ + "api/recharge/order/query?timestamp="+timestamp;
        Map<String, String> heads = new HashMap<>();
        heads.put("content-type", "application/json");
        String params = Constant.merchant_no  + "&" + timestamp;
        String access_key = HMACSHA1.hamcsha1(params.getBytes(), Constant.key.getBytes());
        heads.put("access_key", access_key);
        heads.put("app_id", Constant.app_id);
        Map<String, Object> param = new HashMap<>();
        param.put("merchantNo",Constant.merchant_no);//商户号(必传)
        param.put("merchantOrderNo",merchantOrderNo);//商户订单号(必传)

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            pay_url = pay_url + "&" +entry.getKey() + "=" + entry.getValue();
        }
        System.out.println(pay_url);
        String return_url = HttpUtils.get(pay_url, heads);
        //{"code":200,"data":{"amount":"1000.0","orderNo":"USDT1600328924378686589","poundage":"34.0","sign":"E108F5D8BBD51FE061B568101D83C189C20DCA82","currency":"USDT(ERC20)","state":"1","merchantOrderNo":"PAY1600328923","merchantNo":"5066","timestamp":"1609225317"},"message":"success"}
        System.out.println(return_url);
        return return_url;
    }
}
