package com.jbhunt.infrastructure.universityhackathon.configuration;

import com.jbhunt.infrastructure.universityhackathon.enums.Authority;
import com.jbhunt.infrastructure.universityhackathon.security.filter.JwtTokenCookieFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final JwtTokenCookieFilter jwtTokenCookieFilter;

    public SecurityConfiguration(SecurityProperties securityProperties) {
        this.jwtTokenCookieFilter = new JwtTokenCookieFilter(securityProperties);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http
                .cors()
                .and()
                .csrf()
                .disable();

        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http = http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException)
                        -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()))
                .and();

        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/verify/**").permitAll()
                .antMatchers("/judge/save").hasAuthority(Authority.JUDGE_CREATE.name())
                .antMatchers("/judging/submit").hasAuthority(Authority.JUDGING_SUBMIT.name())
                .antMatchers("/judging/all-scored-teams").hasAuthority(Authority.JUDGING_GET_ALL_TEAMS.name())
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenCookieFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
