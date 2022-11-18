package com.jbhunt.infrastructure.universityhackathon.security.filter;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.constants.SecurityConstants;
import com.jbhunt.infrastructure.universityhackathon.exceptions.InvalidUserException;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AnonymousUser;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AuthorizedUser;
import com.jbhunt.infrastructure.universityhackathon.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenCookieFilter extends OncePerRequestFilter{
    private final SecurityProperties securityProperties;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> searchResult = findTokenCookie(request.getCookies());

        Cookie userCookie;
        if(searchResult.isPresent())
            userCookie = searchResult.get();
        else{
            userCookie = createAnonymousUserCookie();
            response.addCookie(userCookie);
        }

        try {
            AuthorizedUser user = userFromCookie(userCookie);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getGrantedAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (InvalidUserException e){
            log.info("User was invalid. Reason: " + e.getMessage());
        }
        finally {
            filterChain.doFilter(request, response);
        }
    }

    private AuthorizedUser userFromCookie(Cookie cookie) throws InvalidUserException {
        String token = cookie.getValue();

        AuthorizedUser user;
        try {
            user = JwtTokenUtil.getUser(securityProperties, token);
        }
        catch (Exception e){
            throw new InvalidUserException(e.getMessage());
        }

        if(user == null) throw new InvalidUserException("Could not parse token into AuthorizedUser instance");
        if(!user.validateToken(cookie.getValue())) throw new InvalidUserException("Invalid token");

        return user;
    }

    private Optional<Cookie> findTokenCookie(Cookie[] in){
        if (in != null) {
            for (Cookie cookie : in) {
                if (cookie.getName().equals(SecurityConstants.TOKEN_COOKIE_NAME)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    private Cookie createAnonymousUserCookie(){
        AnonymousUser anonymousUser = new AnonymousUser(securityProperties);
        return anonymousUser.generateCookie();
    }
}
