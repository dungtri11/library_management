package com.example.demo.utils;

import com.example.demo.annotation.Authorities;
import com.example.demo.common.Authority;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationHandler {
    public static List<Authority> authoritiesAnnotationHandler() {
        List<Authority> authorities = new ArrayList<>();
        for (Class aClass : ClassHandler.findAllClassesUsingClassLoader("onlineshop.example.beeshop.controller")) {
            for (Method method : aClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Authorities.class)) {
                    authorities = Arrays.asList(method.getAnnotation(Authorities.class).value());
                }
            }
        }
        return authorities;
    }

    public static Method getApiWithURI(String uri) {
        for (Class aClass : ClassHandler.findAllClassesUsingClassLoader("com.example.demo.controller")) {
            for (Method method : aClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    for (String api : requestMapping.value()) {
                        String pre = ApiHandler.getApiPrefix(api);
                        System.out.println(api);
                        if (uri.startsWith(pre)) {
                            return method;
                        }
                    }
                }
            }
        }
        return null;
    }
}
