package com.example.demo.jwt;

import com.example.demo.utils.Base64Handler;
import com.example.demo.utils.ClassHandler;
import com.example.demo.utils.HmacHandler;
import com.example.demo.utils.ObjectHandler;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Jwt {
    private String specification;

    public Jwt() {

    }
    public Jwt(Object header, Object payload, String secret) {
        String data =
                Base64Handler.base64UrlWithOutPaddingEncode(ObjectHandler.jsonStringify(header)) + "." +
                Base64Handler.base64UrlWithOutPaddingEncode(ObjectHandler.jsonStringify(payload));
        this.specification = data + "." + HmacHandler.hash("HmacSHA256", data, secret);
    }
    public String getHeaderSpecification() {
        return specification.split("\\.")[0];
    }
    public String getPayloadSpecification() {
        return specification.split("\\.")[1];
    }
    public String getSignature() {
        return specification.split("\\.")[2];
    }
    public String getHeaderAsString() {
        return Base64Handler.base64UrlDecode(getHeaderSpecification());
    }
    public String getPayloadAsString() {
        return Base64Handler.base64UrlDecode(getPayloadSpecification());
    }
    public String getDecodeSignature() {
        return Base64Handler.base64Decode(getSignature());
    }
}
