package com.example.ssm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

public class JWTUtil {

    public static String getToken(String key, HashMap map){
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256, key).setClaims(map).
                setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        String compact = jwtBuilder.compact();
        return  compact;
    }
    public static Claims parse(String signkey,String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signkey).parseClaimsJws(jwt).getBody();
        return claims;

    }


}
