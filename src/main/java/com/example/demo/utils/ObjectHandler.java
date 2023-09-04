package com.example.demo.utils;

import com.example.demo.jwt.Payload;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectHandler {
    public static String jsonStringify(Object o)  {

        String res = "{";
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                res += "\"" + field.getName() + "\"" + ":"
                        + "\"" + field.get(o) + "\"" + ",";
            }
            res = res.substring(0, res.length()-1) + "}";
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return res;
    }

    public static Map<String, String> parseObjectString(String os) {
        String[] parts = os.split("[,{}]");
        Map<String, String> attributes = new HashMap<>();
        for(String s : parts) {
            String[] attribute = s.split(":");
            if (attribute.length > 1) {
                attributes.put(attribute[0].substring(1, attribute[0].length()-1),
                        attribute[1].substring(1, attribute[1].length()-1));
            }
        }
        return attributes;
    }
}
