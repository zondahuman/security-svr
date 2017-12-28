package com.abin.lee.security.service.rsa.test;


import com.abin.lee.security.service.rsa.KeyNegotiation;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;

/**
 * Created by abin on 2017/12/28 21:13.
 * security-svr
 * com.abin.lee.security.service.rsa.test
 */
public class KeyNegotiationTest {
    //甲方公钥
    private static byte[] publicKey1;
    //甲方私钥
    private static byte[] privateKey1;
    //甲方本地密钥
    private static byte[] key1;
    //乙方公钥
    private static byte[] publicKey2;
    //乙方私钥
    private static byte[] privateKey2;
    //乙方本地密钥
    private static byte[] key2;

    /**
     * 初始化密钥
     *
     * @throws Exception
     */
    public static final void initKey() throws Exception {
        //生成甲方密钥对
        Map<String, Object> keyMap1 = KeyNegotiation.initKey();
        publicKey1 = KeyNegotiation.getPublicKey(keyMap1);
        privateKey1 = KeyNegotiation.getPrivateKey(keyMap1);
        System.out.println("甲方公钥:\n" + Base64.encodeBase64String(publicKey1));
        System.out.println("甲方私钥:\n" + Base64.encodeBase64String(privateKey1));
        //由甲方公钥产生本地密钥对
        Map<String, Object> keyMap2 = KeyNegotiation.initKey(publicKey1);
        publicKey2 = KeyNegotiation.getPublicKey(keyMap2);
        privateKey2 = KeyNegotiation.getPrivateKey(keyMap2);
        System.out.println("乙方公钥:\n" + Base64.encodeBase64String(publicKey2));
        System.out.println("乙方私钥:\n" + Base64.encodeBase64String(privateKey2));
        key1 = KeyNegotiation.getSecretKey(publicKey2, privateKey1);
        System.out.println("甲方本地密钥:\n" + Base64.encodeBase64String(key1));
        key2 = KeyNegotiation.getSecretKey(publicKey1, privateKey2);
        System.out.println("乙方本地密钥:\n" + Base64.encodeBase64String(key2));
    }

    /**
     * 主方法
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        initKey();
        System.out.println();
        System.out.println("===甲方向乙方发送加密数据===");
        String input1 = "求知若饥，虚心若愚。";
        System.out.println("原文:\n" + input1);
        System.out.println("---使用甲方本地密钥对数据进行加密---");
        //使用甲方本地密钥对数据加密
        byte[] encode1 = KeyNegotiation.encrypt(input1.getBytes(), key1);
        System.out.println("加密:\n" + Base64.encodeBase64String(encode1));
        System.out.println("---使用乙方本地密钥对数据库进行解密---");
        //使用乙方本地密钥对数据进行解密
        byte[] decode1 = KeyNegotiation.decrypt(encode1, key2);
        String output1 = new String(decode1);
        System.out.println("解密:\n" + output1);

        System.out.println("/~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~..~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~/");
        initKey();
        System.out.println("===乙方向甲方发送加密数据===");
        String input2 = "好好学习，天天向上。";
        System.out.println("原文:\n" + input2);
        System.out.println("---使用乙方本地密钥对数据进行加密---");
        //使用乙方本地密钥对数据进行加密
        byte[] encode2 = KeyNegotiation.encrypt(input2.getBytes(), key2);
        System.out.println("加密:\n" + Base64.encodeBase64String(encode2));
        System.out.println("---使用甲方本地密钥对数据进行解密---");
        //使用甲方本地密钥对数据进行解密
        byte[] decode2 = KeyNegotiation.decrypt(encode2, key1);
        String output2 = new String(decode2);
        System.out.println("解密:\n" + output2);
    }
}