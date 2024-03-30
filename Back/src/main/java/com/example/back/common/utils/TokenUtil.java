package com.example.back.common.utils;

import com.example.back.domain.auth.member.Member;

import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    public static Map<String, String> createTokenMap(Member member) {
        HashMap<String, String> map = new HashMap<>();

        map.put("role", String.valueOf(member.getRole()));
        map.put("nickname", String.valueOf(member.getNickname()));

        return map;
    }
}
