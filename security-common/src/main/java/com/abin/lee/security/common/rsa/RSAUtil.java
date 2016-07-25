package com.abin.lee.security.common.rsa;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
* Created by abin
* Be Created in 2016/6/29.
*/
public class RSAUtil {

    private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    /**
     * 加解密算法
     */
    private static final String ENCRYPT_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHM = "MD5withRSA";
    private static final String CHARACTER_SET = "UTF-8";
    private static final String PRIVATE_KEY_PATH = Config.getInstance().getString("sign.private.key");
    private static final String PUBLIC_KEY_PATH = Config.getInstance().getString("sign.public.key");
    /**
     * PKCS8文件初始化为PrivateKey.
     *
     * @return
     * @throws Exception
     */
    public static PrivateKey initPrivateKey() throws Exception {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(PRIVATE_KEY_PATH));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine())!=null) {
                if(line.charAt(0)=='-'){
                    continue;
                }
                stringBuilder.append(line).append("\r");
            }

            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] keyByte = base64Decoder.decodeBuffer(stringBuilder.toString());

            KeyFactory kf = KeyFactory.getInstance(ENCRYPT_ALGORITHM);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyByte);

            return kf.generatePrivate(keySpec);
        } finally {
            if(bufferedReader != null) {
                IOUtils.closeQuietly(bufferedReader);
            }
        }
    }

    /**
     * 公钥初始化
     *
     * @return
     */
    public static PublicKey initPublicKey() throws Exception{

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(PUBLIC_KEY_PATH));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine())!=null) {
                if(line.charAt(0)=='-'){
                    continue;
                }
                stringBuilder.append(line).append("\r");
            }

            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] keyByte = base64Decoder.decodeBuffer(stringBuilder.toString());

            KeyFactory kf = KeyFactory.getInstance(ENCRYPT_ALGORITHM);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyByte);

            return kf.generatePublic(keySpec);
        } finally {
            if(bufferedReader != null) {
                IOUtils.closeQuietly(bufferedReader);
            }
        }
    }

    /**
     * 签名算法: MD5withRSA
     *
     * @param content
     * @param privateKey
     * @return
     * @throws Exception 签名异常直接抛出
     */
    public static String signWithMD5(String content) throws Exception{
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(initPrivateKey());
        signature.update(content.getBytes(CHARACTER_SET));

        byte[] signByte = signature.sign();

        BASE64Encoder base64Encoder = new BASE64Encoder();

        return base64Encoder.encodeBuffer(signByte);
    }

    /**
     * 验签:MD5whtiRSA ,异常吞掉,返回验签失败
     *
     * @param content
     * @param sign
     * @param charset
     * @param publicKey
     * @return
     */
    public static boolean verifyWithMD5(String content, String sign) {
        try {
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initVerify(initPublicKey());
            signature.update(content.getBytes(CHARACTER_SET));

            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] keyByte = base64Decoder.decodeBuffer(sign);

            return signature.verify(keyByte);
        } catch (Exception e) {
            logger.error("验签出现异常",e);
            return false;
        }
    }


    /**
     * 加密
     *
     * @param content 待加密内容
     * @param charset 字符集
     * @param key  密钥
     * @param keyLength 密钥位数
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, initPublicKey());

        byte[] data = content.getBytes(CHARACTER_SET);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        int maxEncryptBlock = calcMaxEncryptBlock(2048);
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxEncryptBlock) {
                cache = cipher.doFinal(data, offSet, maxEncryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxEncryptBlock;
        }

        BASE64Encoder base64Encoder = new BASE64Encoder();

        return base64Encoder.encodeBuffer(out.toByteArray());
    }

    /**
     * 解密
     *
     * @param encryptedContent 待解密内容
     * @param charset 字符集
     * @param key 密钥
     * @param keyLength 密钥位数
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedContent) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, initPrivateKey());

        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] encryptedData = base64Decoder.decodeBuffer(encryptedContent);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        int maxDecryptBlock = calcMaxDecryptBlock(2048);
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxDecryptBlock) {
                cache = cipher.doFinal(encryptedData, offSet, maxDecryptBlock);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxDecryptBlock;
        }

        return new String(out.toByteArray(), CHARACTER_SET);
    }


    //计算加密分块最大长度
    private static int calcMaxEncryptBlock(int keyLength) {
        return keyLength/8 - 11;
    }
    //计算解密分块最大长度
    private static int calcMaxDecryptBlock(int keyLength) {
        return keyLength/8;
    }
}

