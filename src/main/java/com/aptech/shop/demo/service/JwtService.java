package com.aptech.shop.demo.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.security.secret-key}")
    private String secretKey;
    private Key getSignKey(){
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey.getBytes()));
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+7*24*60*60*1000))
                .signWith(SignatureAlgorithm.HS256,getSignKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+(7+2)*24*60*60*1000))
                .signWith(SignatureAlgorithm.HS256,getSignKey())
                .compact();
    }

    public Claims extractClaim(String token){
        Claims claims = Jwts.parser().setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
    public String extractUsername(String token){
        Claims claims = extractClaim(token);
        String username = claims.getSubject();
        return username;
    }

    public boolean isTokenExpired(String token){
        Claims claims = extractClaim(token);
        Date date = claims.getExpiration();
        return date.before(new Date());
    }

    public boolean isTokenValidate(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
