package com.jbhunt.infrastructure.universityhackathon.util;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AuthorizedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Slf4j
@UtilityClass
public class JwtTokenUtil {

    public static Date getExpirationDate(String token, String signingSecret){
        return getClaim(token, Claims::getExpiration, signingSecret);
    }

    public static <T> T getClaim(String token, Function<Claims, T> claimsResolver, String signingSecret){
        final Claims claims = getAllClaims(token, signingSecret);
        return claimsResolver.apply(claims);
    }

    public static String generate(AuthorizedUser authorizedUser, int tokenLifetime, String signingSecret){
        byte[] secretBytes = signingSecret.getBytes();
        Key signingKey = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> claims = new HashMap<>();
        claims.put("UserClass", authorizedUser.getClass());

        return authorizedUser.toTokenBuilder()
                .addClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifetime * 1000L))
                .signWith(signingKey, SignatureAlgorithm.HS512).compact();
    }

    public static AuthorizedUser getUser(SecurityProperties securityProperties, String token){
        final Claims claims = getAllClaims(token, securityProperties.getJwtSecret());
        String className = claims.get("UserClass", String.class);
        try {
            return ((AuthorizedUser)Class
                    .forName(className)
                    .getConstructor(SecurityProperties.class)
                    .newInstance(securityProperties))
                    .fromToken(token);
        }
        catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Claims getAllClaims(String token, String signingSecret) {
        byte[] secretBytes = signingSecret.getBytes();
        Key signingKey = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    public static boolean isTokenExpired(String token, String signingSecret) {
        return getExpirationDate(token, signingSecret).before(new Date());
    }
}
