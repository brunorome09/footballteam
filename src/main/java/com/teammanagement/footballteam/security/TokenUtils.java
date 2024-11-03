package com.teammanagement.footballteam.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import javax.crypto.SecretKey;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "v7VYVsM_X0e8wmYB6aEMMfngysLAnnJG6YBhhzjoi5M";
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2592000L;

    public static String createToken(String nombre) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        SecretKey secretKey = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());

        return Jwts.builder()
                .setSubject(nombre)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String name = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(name, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }
    }
}