package com.petspace.dev.service.auth;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.service.auth.jwt.Token;
import com.petspace.dev.util.exception.JwtNotAvailableException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Component
@Getter
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final PrincipalDetailsService principalDetailsService;

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenExpiredIn;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenExpiredIn;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    public Token createToken(String payload) {
        return Token.builder()
                .accessToken(createAccessToken(payload))
                .refreshToken(createRefreshToken())
                .refreshTokenExpiredIn(getRefreshTokenExpiredIn())
                .build();
    }

    public String createAccessToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpiredIn);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String randomString = new String(array, StandardCharsets.UTF_8);

        Claims claims = Jwts.claims().setSubject(randomString);
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenExpiredIn);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            log.info("expiredDate={}", claimsJws.getBody().getExpiration());
            log.info("expired?={}", claimsJws.getBody().getExpiration().before(new Date()));
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            throw new JwtNotAvailableException(UNSUPPORTED_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            throw new JwtNotAvailableException(MALFORMED_TOKEN_TYPE);
        } catch (SignatureException e) {
            throw new JwtNotAvailableException(INVALID_SIGNATURE_JWT);
        } catch (IllegalArgumentException e) {
            throw new JwtNotAvailableException(INVALID_TOKEN_TYPE);
        }
    }

    public Authentication getAuthentication(String token) {
        PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(getPayload(token));
        log.info("getAuthentication, email={}", principalDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }

    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }
}
