
package com.abin.lee.security.common.feign;

import feign.Feign;
import feign.Logger;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

/**
 * 
 * Function: Feign Client<br/>
 *
 * @author lxgao
 * @version
 * @since JDK 1.8
 */
public class FeignClient {

    /**
     * 
     * 创建一个默认的Rest客户端<br/>
     *
     * @author lxgao
     * @since JDK 1.8
     */
    public static Feign.Builder createDefaultHttpBuilder() {
        return Feign.builder().logLevel(Logger.Level.FULL).logger(new Slf4jLogger(FeignClient.class))
                .client(new OkHttpClient()).encoder(new FastJsonEncoder()).decoder(new FastJsonDecoder());
    }

    /**
     * 
     * 创建一个默认的Rest客户端<br/>
     *
     * @author lxgao
     * @since JDK 1.8
     */
    public static Feign.Builder createDefaultRestBuilder() {
        return Feign.builder().logLevel(Logger.Level.FULL).logger(new Slf4jLogger(FeignClient.class))
                .client(new OkHttpClient()).encoder(new FastJsonEncoder()).decoder(new FastJsonDecoder())
                .contract(new JAXRSContract());
    }
}
