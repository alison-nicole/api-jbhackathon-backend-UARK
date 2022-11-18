package com.jbhunt.infrastructure.universityhackathon.security.user.base;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;


public class IdentifiedUser extends AnonymousUser {

    @Getter
    @Setter
    private String userId;

    public IdentifiedUser(SecurityProperties securityProperties) {
        super(securityProperties);
    }

    public IdentifiedUser(SecurityProperties securityProperties, String userId){
        super(securityProperties);
        this.userId = userId;
    }
    @Override
    public boolean validateToken(String token) {
        super.validateToken(token);
        String storedId = JwtTokenUtil.getClaim(token, Claims::getSubject, securityProperties.getJwtSecret());
        return storedId.equals(userId);
    }
    @Override
    public AuthorizedUser fromToken(String token){
        userId = JwtTokenUtil.getClaim(token, Claims::getSubject, securityProperties.getJwtSecret());
        return this;
    }

    @Override
    public JwtBuilder toTokenBuilder() {
        return Jwts.builder().setSubject(userId);
    }
}
