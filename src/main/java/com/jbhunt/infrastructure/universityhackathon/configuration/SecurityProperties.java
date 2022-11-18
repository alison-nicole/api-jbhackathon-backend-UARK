package com.jbhunt.infrastructure.universityhackathon.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class SecurityProperties {
    @Getter
    @Value("${security.cookies.secure}")
    private boolean secureCookies;

    @Getter
    @Value("${security.cookies.path}")
    private String cookiePath;

    @Getter
    @Value("${security.jwt.lifetime}")
    private int jwtLifetime;

    @Getter
    @Value("${security.jwt.secret}")
    private String jwtSecret;
}
