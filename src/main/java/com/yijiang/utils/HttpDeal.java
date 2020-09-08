package com.yijiang.utils;

//import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaoyi1 on 2016/8/3.
 */
public class HttpDeal {
    public static final Logger log = LoggerFactory.getLogger(HttpDeal.class);
    public HttpDeal() {}

    public static String get(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet get = new HttpGet(url);

        //请求结果
        CloseableHttpResponse response = null;
        String content = "";
        try {
            //执行get方法
            get.addHeader("Accept-Language", "zh-cn,zh;q=0.5");
            //get.addHeader("Host","www.yourdomain.com");
            get.addHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            get.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            response = httpclient.execute(get);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_NOT_IMPLEMENTED || statusCode == HttpStatus.SC_BAD_REQUEST) {
                log.error("The Get Method Is Not Implemented By This URI");
                System.err.println("The Get Method Is Not Implemented By This URI");
            }
            if (statusCode == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
                log.info(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 处理post请求.
     *
     * @param url    请求路径
     * @param params 参数
     * @return json
     */
    public static String post(String url, Map<String, String> params) {
        //实例化httpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //实例化post方法
        HttpPost httpPost = new HttpPost(url);
        //处理参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        //结果
        CloseableHttpResponse response = null;
        String content = "";
        try {
            //提交的参数
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
            //将参数给post方法
            httpPost.setEntity(uefEntity);
            //执行post方法
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
//                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    /*post*/
    public  static String postHost(String url, String proxy_host_addr, int proxy_port, Map<String, String> params) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setStaleConnectionCheckEnabled(true)
                .build();

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig)
                .build();
        RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setProxy(new HttpHost(proxy_host_addr, proxy_port))
                .build();
        //实例化post方法
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        //处理参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        //结果
        CloseableHttpResponse response = null;
        String content = "";
        try {
            //提交的参数
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
            //将参数给post方法
            httpPost.setEntity(uefEntity);
            //执行post方法
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
//                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String getWithCookies(String url, List<Cookie> cookieList){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        StringBuilder cooKieStr = new StringBuilder();
        for (Cookie cookie : cookieList) {
            cooKieStr.append(cookie.getName()+ "=" +cookie.getValue()+ ";");
        }
        get.setHeader( "Cookie", cooKieStr.toString() );

        //请求结果
        CloseableHttpResponse response = null;
        String content = "";
        try {
            //执行get方法
            response = httpclient.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_NOT_IMPLEMENTED || statusCode == HttpStatus.SC_BAD_REQUEST) {
                log.error("The Get Method Is Not Implemented By This URI");
                System.err.println("The Get Method Is Not Implemented By This URI");
            }
            if (statusCode == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
//                log.info(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    public  static String postWithCookies(String url,Map<String, String> params, List<Cookie> cookieList) {
        //实例化httpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //实例化post方法
        HttpPost httpPost = new HttpPost(url);

        StringBuilder cooKieStr = new StringBuilder();
        for (Cookie cookie : cookieList) {
            cooKieStr.append(cookie.getName()+ "=" +cookie.getValue()+ ";");
        }
        httpPost.setHeader( "Cookie", cooKieStr.toString() );
        //处理参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        //结果
        CloseableHttpResponse response = null;
        String content = "";
        try {
            //提交的参数
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
            //将参数给post方法
            httpPost.setEntity(uefEntity);
            //执行post方法
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
//                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void main22(String[] args) {
        List<Cookie> cookieList = new ArrayList<>();
        Cookie c1 = new Cookie("erp1.jd.com","F232737A9A76D68F0B6E128347BDDB3BD7D6CB7ECB84B1A20A1AA5522A05692ECF50322EE25BAE445AB7DE375A01F9C0CD27" +
                "EE00715EFFF501F8C6A11680F18672CB6351CE24E42877450C57B2F50956");
        Cookie c2 = new Cookie("sso.jd.com","7ec6b06209c042219c3430eaac18fd14");

        cookieList.add(c1);
        cookieList.add(c2);
        String str = getWithCookies("http://test.jd.com/", cookieList);
        System.out.println(str.contains("杰夫服务平台"));
    }


    /*public static JSONObject postJason(String url, JSONObject json){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = res.getEntity();
                String result = EntityUtils.toString(entity);// 返回json格式：
                response = JSONObject.parseObject(result);
                Header[] headersss = res.getHeaders("Set-Cookie");
                for (Header header1 : headersss) {
                    if (header1.getName().equals("Set-Cookie")) {
                        System.out.println("*********" + header1.getName());
                        String headerValue =  header1.getValue();
                        System.out.println("*********" + headerValue);
                        String needs = headerValue.substring(0, headerValue.indexOf(";"));
                        System.out.println("*********" + needs);
                        String[] strs = needs.split("=");
                        response.put(strs[0],strs[1]);

                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static void main1111(String[] args) {

        JSONObject response = null;
        JSONObject params = new JSONObject();
        params.put("username","xiaoyi29");
        params.put("password","QWEqwe124???");
        String url = "http://jira.jd.com/rest/auth/1/session";
        response = postJason(url, params);
        System.out.println("************" + response.toJSONString());


        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://jira.jd.com/rest/api/2/search?jql=assignee=xiaoyi29");
        get.setHeader("Cookie", "JSESSIONID="+response.getString("JSESSIONID")+";atlassian.xsrf.token="+response.getString("atlassian.xsrf.token"));
        get.setHeader("Host","jira.jd.com");
        //请求结果
        CloseableHttpResponse response1 = null;
        String content = "";
        try {
            //执行get方法
            response1 = httpclient.execute(get);
            int statusCode = response1.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_NOT_IMPLEMENTED || statusCode == HttpStatus.SC_BAD_REQUEST) {
                log.error("The Get Method Is Not Implemented By This URI");
                System.err.println("The Get Method Is Not Implemented By This URI");
            }
            if (statusCode == 200) {
                content = EntityUtils.toString(response1.getEntity(), "utf-8");
                log.info(content);
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}








