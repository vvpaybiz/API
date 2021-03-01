package com.test.pay;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http 请求工具类
 *
 * @author june
 */
public class HttpUtils {

    /**
     * 默认传输编码
     */
    public static String ENCODING = "UTF8";

    /**
     * GET的方式请求
     *
     * @param url
     * @return
     * @createDate: Apr 2, 2011 5:58:03 PM
     */
    public static String get(String url, Map<String, String> headers) {
        String data = null;
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                data = EntityUtils.toString(entity);
                httpGet.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String postJson(String URL,Map<String, Object> paras, Map<String, String> headers) {

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(URL);
        post.setHeader("Content-Type", "application/json");
        if (headers != null) {
            for (Entry<String, String> entry : headers.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        String result = "";

        try {

            StringEntity s = new StringEntity(JSONObject.toJSONString(paras), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(s);

            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            // 获取响应输入流
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();

            result = strber.toString();
            System.out.println(result);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                System.out.println("请求服务器成功，做相应处理");

            } else {

                System.out.println("请求服务端失败");

            }


        } catch (Exception e) {
            System.out.println("请求异常");
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Http Post 封装
     *
     * @param url   请求的url地址
     * @param paras 参数集合
     * @return String 返回结果:字符串
     */
    public static String post(String url, Map<String, Object> paras, Map<String, String> headers) {
        //初始化返回结果
        String resultStr = null;
        try {
            DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(2, true);
            //创建安全的httpClient
            CloseableHttpClient httpClient = HttpClients.custom().setRetryHandler(retryHandler).build();
            //根据参数集合构造表单列表
            List<NameValuePair> formParas = new ArrayList<>();
            if (paras != null) {
                for (Entry<String, Object> entry : paras.entrySet()) {
                    String value = String.valueOf(entry.getValue());
                    formParas.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            //对表单进行编码格式化
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParas, ENCODING);
            //初始化post
            HttpPost httpPost = new HttpPost(url);
            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            //设置post内容
            httpPost.setEntity(entity);
            resultStr = getResponse(httpClient, httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    private static String getResponse(CloseableHttpClient httpClient, HttpPost httpPost) {
        String resultStr = null;
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            try {
                //执行请求
                HttpEntity httpEntity = httpResponse.getEntity();
                //获取请求结果
                if (httpEntity != null) {
                    //resultStr = ConvertStreamToString(httpEntity.getContent(), "UTF-8");
                }
                EntityUtils.consume(httpEntity);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpClient.close();
                httpResponse.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * @param url
     * @param xml
     * @return
     */
    public static String postXml(String url, String xml) {
        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(2, true);
        //创建安全的httpClient
        CloseableHttpClient httpClient = HttpClients.custom().setRetryHandler(retryHandler).build();
        HttpPost httpPost = new HttpPost(url);
        StringEntity myEntity = new StringEntity(xml, ENCODING);
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(myEntity);
        String resultStr = getResponse(httpClient, httpPost);
        return resultStr;
    }


    public static String getUrl(String url) {
        String content = "";
        HttpUriRequest req = RequestBuilder.get(url).build();
        try {
            HttpResponse resp = HttpClients.createDefault().execute(req);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = resp.getEntity();
                content = EntityUtils.toString(entity);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}