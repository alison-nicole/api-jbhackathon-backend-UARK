package com.jbhunt.infrastructure.universityhackathon.security.user.base;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.constants.SecurityConstants;
import com.jbhunt.infrastructure.universityhackathon.enums.Authority;
import com.jbhunt.infrastructure.universityhackathon.util.CookieUtil;
import com.jbhunt.infrastructure.universityhackathon.util.JwtTokenUtil;
import io.jsonwebtoken.JwtBuilder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AuthorizedUser {
    @Getter
    protected ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    protected final SecurityProperties securityProperties;

    //All implementations of this class must have a constructor that takes a ONLY a SecurityProperties object
    //Other constructors can exist, but at least one must take only a SecurityProperties object
    protected AuthorizedUser(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        init();
    }

    protected abstract void init();

    protected void grantFromStrings(Collection<String> strings){
        grantedAuthorities.addAll(
                strings.stream()
                        .map((SimpleGrantedAuthority::new))
                        .filter(auth -> !grantedAuthorities.contains(auth))
                        .collect(Collectors.toList())
        );
    }

    protected void grant(Authority ... authorities){
        grantedAuthorities.addAll(
                Arrays.stream(authorities)
                        .map(auth -> new SimpleGrantedAuthority(auth.name()))
                        .filter(auth -> !grantedAuthorities.contains(auth))
                        .collect(Collectors.toList())
        );
    }

    protected void revoke(Authority ... authorities){
        grantedAuthorities.removeAll(
                Arrays.stream(authorities)
                        .map(auth -> new SimpleGrantedAuthority(auth.name()))
                        .filter(auth -> grantedAuthorities.contains(auth))
                        .collect(Collectors.toList())
        );
    }

    protected void revokeAll(){
        grantedAuthorities = new ArrayList<>();
    }

    public String generateToken(){
        return JwtTokenUtil.generate(this, securityProperties.getJwtLifetime(), securityProperties.getJwtSecret());
    }

    public Cookie generateCookie(){
        String token = JwtTokenUtil.generate(this, securityProperties.getJwtLifetime(), securityProperties.getJwtSecret());

        if (securityProperties.isSecureCookies())
            return CookieUtil.createSecureCookie(SecurityConstants.TOKEN_COOKIE_NAME, token, securityProperties.getCookiePath(), securityProperties.getJwtLifetime());
        else
            return CookieUtil.createCookie(SecurityConstants.TOKEN_COOKIE_NAME, token, securityProperties.getCookiePath(), securityProperties.getJwtLifetime());
    }

    public boolean validateToken(String token){
        return !JwtTokenUtil.isTokenExpired(token, securityProperties.getJwtSecret());
    }

    public abstract AuthorizedUser fromToken(String token);

    public abstract JwtBuilder toTokenBuilder();
}

