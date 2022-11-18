package com.jbhunt.infrastructure.universityhackathon.security.user.base;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.enums.Authority;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

public class AnonymousUser extends AuthorizedUser {

    public AnonymousUser(SecurityProperties securityProperties) {
        super(securityProperties);
    }

    @Override
    protected void init() {
        grant(Authority.JUDGE_GET, Authority.JUDGE_CREATE);
    }

    @Override
    public AuthorizedUser fromToken(String token) {
        return this;
    }

    @Override
    public JwtBuilder toTokenBuilder() {
        return Jwts.builder();
    }
}
