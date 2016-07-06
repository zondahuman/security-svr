package com.abin.lee.security.service.rsa.test;


import com.abin.lee.security.common.json.JsonUtil;
import com.abin.lee.security.common.util.HttpClientUtil;
import com.abin.lee.security.service.rsa.RSAUtil;
import com.google.common.collect.Maps;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
*
*/
public class LoanInfoDeleteTest {
    private static final String httpURL = "http://localhost:7200/load/platformTest";
//    private static final String httpURL = "http://localhost:7200/load/platform";
//    private static final String httpURL = "http://172.16.2.133:9000/load/platform";
//    private static final String httpURL = "https://172.16.2.133/load/platform";
//    private static final String httpURL = "https://172.16.2.133:443/load/platform";

    @Test
    public void testLoanInfoDelete() throws IOException {
        CloseableHttpClient httpclient = HttpClientUtil.getHttpsClient();
        try {
            HttpPost httpPost = new HttpPost(httpURL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Map<String, String> request = Maps.newHashMap();
            request.put("reportedId", UUID.randomUUID().toString());
            request.put("service", "loanInfoDelete");
            request.put("serviceVersion", "1.0");
            request.put("partner", "YOUXIN");
            request.put("businessLine", "youxin1");
            request.put("contractNo", "YOUXINAAABB");
//            request.put("contractName", "YOUXINAAACC");
            request.put("contractName", "");
            for (Iterator<Map.Entry<String, String>> iterator = request.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> nav = (Map.Entry<String, String>) iterator.next();
                request.put(nav.getKey(), nav.getValue());
            }

//            String content = RSAUtil.encrypt(JsonUtil.toJson(request));
//            String sign = RSAUtil.signWithMD5(JsonUtil.toJson(request));

            String content = JsonUtil.toJson(request);
            String sign = JsonUtil.toJson(request);

            nvps.add(new BasicNameValuePair("content", content));
            nvps.add(new BasicNameValuePair("sign", sign));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            System.out.println("Executing request: " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpPost);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.close();
        }
    }

}


