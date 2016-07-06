package com.abin.lee.security.common.rsa;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {


    private Config() {
    }

    public static Config getInstance() {
        return ConfigHolder.instane;
    }

    private static class ConfigHolder{
        private static Config instane = new Config();
    }

    private static Properties configProp;// 系统配置

    static{
        // 初始化系统配置参数
        InputStream in = Config.class.getClassLoader().getResourceAsStream("properties/security.properties");
        // 创建Properties实例
        configProp = new Properties();
        // 将Properties和流关联
        try {
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public int getInt(String key) {
        return Integer.parseInt(configProp.getProperty(key));
    }

    public String getString(String key) {
        return configProp.getProperty(key);
    }
}