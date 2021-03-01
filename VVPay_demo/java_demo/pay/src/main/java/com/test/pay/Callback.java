package com.test.pay;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 入金异步回调
 */
@WebServlet(name = "Callback")
public class Callback extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String amount =request.getParameter("amount");
        String exchangeRate =request.getParameter("exchangeRate");
        String poundage =request.getParameter("poundage");
        String merchantNo =request.getParameter("merchantNo");
        String merchantOrderNo =request.getParameter("merchantOrderNo");
        String orderNo =request.getParameter("orderNo");
        String sign =request.getParameter("sign");
        long timestamp =Long.valueOf(request.getParameter("timestamp"));
        System.out.println ("asyncreturn(param)-------->"+"amount:"+amount+"  merchantNo:"+merchantNo+"  merchantOrderNo:"+merchantOrderNo+" orderNo:"+orderNo+"    timestamp:"+timestamp+"    sign:"+sign);
        String signStr = amount+"&"+exchangeRate+"&"+poundage+"&"+merchantNo+"&"+merchantOrderNo+"&"+orderNo+"&"+timestamp;
        String signValue = HMACSHA1.hamcsha1(signStr.getBytes(), Constant.key.getBytes());
        System.out.println("asyncreturn(sign)-------->"+"signStr:"+signStr+"     signValue:"+signValue);
        // 如果验证签名成功，这里处理商户自己的业务

        //返回 {"code":200}
        JSONObject result = new JSONObject();
        result.put("code",200);
        writeResponse(response,result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

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
