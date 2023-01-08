package com.petspace.dev.config.oauth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

@Slf4j
public class JwtProvider {

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliSeconds;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMilliSeconds;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    public String createAccessToken(String payload) {
        log.info("createToken In accessToken = {}", accessTokenValidityInMilliSeconds);
        log.info("createToken In secretKey = {}", secretKey);
        return createToken(payload,refreshTokenValidityInMilliSeconds);
    }

    public String createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return createToken(generatedString, refreshTokenValidityInMilliSeconds);
    }

    public String createToken(String payload, long expireLength) {
        Claims claims = Jwts.claims().setSubject(payload);
        log.info("claims = {}", claims);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        log.info("validity = {}", validity);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e){
            throw new JwtException("유효하지 않는 토큰입니다.");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}
