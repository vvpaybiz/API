package com.test.pay;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * 入金
 */
@WebServlet(name = "Recharge")
public class Recharge extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String p1=request.getParameter("p1");
        long timestamp = System.currentTimeMillis()/1000;
        if (p1.trim().equals("")) {
        }
        String p2 = Constant.merchant_no;
        String p3 = "PAY"+timestamp;
        String p4 = request.getParameter("p4");
        String params = p1+"&"+p2+"&"+p3+"&"+timestamp;
        String access_key = HMACSHA1.hamcsha1(params.getBytes(), Constant.key.getBytes());

        String pay_url = Constant.URL_ + "/api/recharge/check/v2?p1="+p1+"&p2="+p2+"&p3="+p3+"&p4="+p4+"&timestamp="+timestamp;
        Map<String, String> heads = new HashMap<>();
        heads.put("content-type", "application/json");
        heads.put("access_key", access_key);
        heads.put("app_id", Constant.app_id);
        System.out.println("pay-------->"+pay_url+"access_key="+access_key);
        String return_url = HttpUtils.get(pay_url, heads);
        System.out.println("result------------>" + return_url);
        //{"code":200,"data":{"url":"http://pay-api.vvpay.me/mobile/buy?orderNo=USDT1609233285404590147&amount=100.0&usdtAmount=100.0&exchangeRate=1.0&merchantName=VVPay+Demo&closeCurrency=CNY"},"message":"success"}
        JSONObject jsonObject = JSONObject.parseObject(return_url);
//        writeResponse(response,jsonObject);
        if (jsonObject.getInteger("code")==200){
            try {
                response.sendRedirect(jsonObject.getJSONObject("data").getString("url")); // 入金界面URL
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            writeResponse(response,jsonObject);
        }
    }

    /**
     * 响应
     * @param response
     * @param data
     */
    public void writeResponse(HttpServletResponse response, JSONObject data) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
            pw.println(data.toJSONString());
            pw.flush();
            pw.close();
        } catch (Exception e) {
        }
    }
}
