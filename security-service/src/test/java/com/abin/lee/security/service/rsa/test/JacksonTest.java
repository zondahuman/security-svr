package com.abin.lee.security.service.rsa.test;

import com.abin.lee.security.common.json.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abin
 * Be Created in 2016/8/11.
 */
public class JacksonTest {
    public static void main(String[] args) {
        String json = "{\n" +
                "\t\"latency\": 392,\n" +
                "\t\"data\": {\n" +
                "\t\t\"itemMap\": {\n" +
                "\t\t\t\"阿托品滴眼剂\": {\n" +
                "\t\t\t\t\"bestMatchedName\": \"阿托品滴眼剂\",\n" +
                "\t\t\t\t\"deductible\": {\n" +
                "\t\t\t\t\t\"deductibleType\": \"MEDICATIONS\",\n" +
                "\t\t\t\t\t\"name\": \"阿托品滴眼剂\",\n" +
                "\t\t\t\t\t\"deductablePercentage\": 0.0,\n" +
                "\t\t\t\t\t\"regionCode\": \"110000\",\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"备注\": \"仅限顺义区医院使用\",\n" +
                "\t\t\t\t\t\t\"active\": \"1\",\n" +
                "\t\t\t\t\t\t\"收费等级\": \"甲类\",\n" +
                "\t\t\t\t\t\t\"收费类别\": \"西药\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"deductableLevel\": \"甲类\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"tenentCode\": \"\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\t\n";


        Map<String, Object> params = JsonUtil.decodeJson(json, new TypeReference<Map<String, Object>>() {
        });

        Object data = params.get("data");



    }
}
