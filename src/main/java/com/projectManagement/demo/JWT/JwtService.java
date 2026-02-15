package com.projectManagement.demo.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtService {


    @Value("${JWT_KEY}")
    String key;

    public Key generateKey(){
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String username, Map<String, Object> claims){
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(String username) {
        return generateToken(username, new HashMap<>());
    }


    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token){
        Claims claims= extractClaims(token);
        return  claims.getSubject();
    }

    public Date extractExpiration(String token){
        return extractClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Long extractEmpId(String token){
        return extractClaims(token).get("empId", Long.class);
    }


    public String addClaimAndGenerateNewToken(String token , String key , Object value){
        Claims claims = extractClaims(token);
        claims.put(key, value);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }




}

