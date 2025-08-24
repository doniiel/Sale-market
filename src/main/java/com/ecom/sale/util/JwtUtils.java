package com.ecom.sale.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.access.secret}")
    private String accessSecretKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpirationInMs;

    @Value("${jwt.refresh.secret}")
    private String refreshSecretKey;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationInMs;

    private Key getAccessSigningKey() {
        return Keys.hmacShaKeyFor(accessSecretKey.getBytes());
    }

    private Key getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, accessTokenExpirationInMs, getAccessSigningKey());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return generateToken(claims, userDetails, refreshTokenExpirationInMs, getRefreshSigningKey());
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration, Key signingKey) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getSubject, isRefreshToken);
    }

    public Date extractExpiration(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getExpiration, isRefreshToken);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isRefreshToken) {
        final Claims claims = extractAllClaims(token, isRefreshToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, boolean isRefreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(isRefreshToken ? getRefreshSigningKey() : getAccessSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token, boolean isRefreshToken) {
        return extractExpiration(token, isRefreshToken).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails, boolean isRefreshToken) {
        final String username = extractUsername(token, isRefreshToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, isRefreshToken);
    }
}