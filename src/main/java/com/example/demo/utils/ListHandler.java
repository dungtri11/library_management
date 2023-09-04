package com.example.demo.utils;

import com.example.demo.common.Authority;

import java.util.List;

public class ListHandler {
    public static boolean contains(List<Authority> authorityList, Authority authority) {
        for (Authority s : authorityList) {
            if (s == authority) {
                return true;
            }
        }
        return false;
    }
}
