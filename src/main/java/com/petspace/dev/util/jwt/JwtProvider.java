package com.petspace.dev.util.jwt;

import com.petspace.dev.util.jwt.filter.PrincipalDetails;
import com.petspace.dev.util.jwt.filter.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final PrincipalDetailsService principalDetailsService;

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliSeconds;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMilliSeconds;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    public String createAccessToken(String payload) {
        return createToken(payload,accessTokenValidityInMilliSeconds);
    }

    public String createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return createToken(generatedString, refreshTokenValidityInMilliSeconds);
    }

    public String createToken(String payload, long expireLength) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(getPayload(token));
        log.info("getAuthentication, email={}", principalDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
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
            return e.getClaims().getSubject(); // todo 토큰 만료
        } catch (JwtException e){
            throw new JwtException("유효하지 않는 토큰입니다."); // todo 유효x 토큰
        }
    }

    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false; // todo 토큰 만료
        }
    }
}
