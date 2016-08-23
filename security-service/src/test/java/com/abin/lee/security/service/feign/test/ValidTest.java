package com.abin.lee.security.service.feign.test;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import feign.RequestLine;
import org.junit.Assert;
import org.junit.Test;


import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
/**
 *
 */
public class ValidTest {
    private static final String httpURL = "http://172.16.2.133:9000";
//    private static final String httpURL = "http://172.16.2.133:9000/load/valid";
//    private static final String httpURL = "https://172.16.2.133/load/valid";


    @Test
    public void testValid() throws Exception {
        Feign.Builder builder = Feign.builder().logLevel(Logger.Level.FULL).contract(new JAXRSContract())
                .logger(new Slf4jLogger()).client(new OkHttpClient()).encoder(new GsonEncoder())
                .decoder(new GsonDecoder());
        HttpsLoadClient client = builder.target(HttpsLoadClient.class, httpURL);

//        Feign.Builder builder = FeignClient.createDefaultHttpBuilder();
//        HttpsLoadClient client = builder.target(HttpsLoadClient.class, httpURL);

        String param = "abin";
        System.out.println("param=" + param );
        String result = client.uploadClaim(param);
        System.out.println("result=" + result);

    }

    interface HttpsLoadClient {
        @RequestLine("GET /load/valid?param={param}")
//        String uploadClaim(String param);
        String uploadClaim(@FormParam("param")String param);
//        String uploadClaim(@Param("param") String param);

    }


}


