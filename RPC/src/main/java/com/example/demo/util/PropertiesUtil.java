package com.example.demo.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return (String) properties.get(key);

    }
}
