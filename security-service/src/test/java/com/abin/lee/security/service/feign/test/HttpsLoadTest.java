package com.abin.lee.security.service.feign.test;


import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.abin.lee.security.common.feign.FeignClient;
import com.abin.lee.security.common.json.JsonUtil;
import com.abin.lee.security.service.rsa.RSAUtil;
import com.google.common.collect.Maps;
import feign.*;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class HttpsLoadTest {
    //    private static final String httpURL = "http://localhost:7200/load/platform";
    private static final String httpURL = "http://172.16.2.133:9000";
//    private static final String httpURL = "http://172.16.2.133:9000/load/platform";
//    private static final String httpURL = "https://172.16.2.133/load/platform";


    @Test
    public void testUploadClaim() throws Exception {
//        Feign.Builder builder = Feign.builder().logLevel(Logger.Level.FULL).contract(new JAXRSContract())
//                .logger(new Slf4jLogger()).client(new OkHttpClient()).encoder(new GsonEncoder())
//                .decoder(new GsonDecoder());
//        HttpsLoadClient client = builder.target(HttpsLoadClient.class, httpURL);

        Feign.Builder builder = FeignClient.createDefaultHttpBuilder();
        HttpsLoadClient client = builder.target(HttpsLoadClient.class, httpURL);


        Map<String, String> request = Maps.newHashMap();
        request.put("reportedId", UUID.randomUUID().toString());
        request.put("service", "loanInfoDelete");
        request.put("serviceVersion", "1.0");
        request.put("partner", "YOUXIN");
        request.put("businessLine", "youxin1");
        request.put("contractNo", "YOUXINAAABB");
        request.put("contractName", "YOUXINAAACC");
        for (Iterator<Map.Entry<String, String>> iterator = request.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> nav = (Map.Entry<String, String>) iterator.next();
            request.put(nav.getKey(), nav.getValue());
        }

        String content = RSAUtil.encrypt(JsonUtil.toJson(request));
        String sign = RSAUtil.signWithMD5(JsonUtil.toJson(request));

        System.out.println("content=" + content + " , sign="+sign);
        String result = client.uploadClaim(content, sign);
        System.out.println("result=" + result);

    }

    interface HttpsLoadClient {
        @RequestLine("POST /load/platform")
        String uploadClaim(@Param("content") String content, @Param("sign") String sign);

    }


}


