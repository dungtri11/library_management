package com.example.demo.utils;

public class ApiHandler {
    public static String getApiPrefix(String api) {
        String[] urlParts = api.split("/");
        String lastPart = urlParts[urlParts.length-1];
        if (lastPart.startsWith("{")) {
            api = api.substring(0, api.length()-lastPart.length()-1);
        }
        return api;
    }
}
