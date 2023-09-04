package com.example.demo.utils;

import java.util.Base64;

public class Base64Handler {
    public static String base64UrlWithOutPaddingEncode(String raw) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes());
    }
    public static String base64UrlDecode(String encrypt) {
        return new String(Base64.getUrlDecoder().decode(encrypt));
    }
    public static String base64WithOutPaddingEncode(String raw) {
        return Base64.getEncoder().withoutPadding().encodeToString(raw.getBytes())
                .replaceAll("/", "_")
                .replaceAll("\\+", "-");
    }

    public static String base64WithOutPaddingEncode(byte[] raw) {
        return Base64.getEncoder().withoutPadding().encodeToString(raw)
                .replaceAll("/", "_")
                .replaceAll("\\+", "-");
    }

    public static String base64Decode(String encrypt) {
        return new String(Base64.getDecoder()
                .decode(encrypt.replaceAll("_","/").replaceAll("-", "+")));
    }
}
