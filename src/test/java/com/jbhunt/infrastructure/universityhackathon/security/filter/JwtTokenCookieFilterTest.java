package com.jbhunt.infrastructure.universityhackathon.security.filter;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.constants.SecurityConstants;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AnonymousUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenCookieFilterTest{
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @Mock
    private SecurityProperties mockSecurityProperties;
    private SecurityProperties testSecurityProperties = new SecurityProperties(false, "/", 60, "3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam");

    @InjectMocks
    private JwtTokenCookieFilter filter;

    @Before
    public void setup() {
        when(mockSecurityProperties.getJwtSecret()).thenReturn(testSecurityProperties.getJwtSecret());
        when(mockSecurityProperties.isSecureCookies()).thenReturn(testSecurityProperties.isSecureCookies());
        when(mockSecurityProperties.getCookiePath()).thenReturn(testSecurityProperties.getCookiePath());
        when(mockSecurityProperties.getJwtLifetime()).thenReturn(testSecurityProperties.getJwtLifetime());
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testFilterNoCookie() {
        when(request.getCookies()).thenReturn(null);

        try {
            filter.doFilterInternal(request, response, chain);
        } catch (Exception e) {
            Assert.fail();
        }

        //An anonymous user should've been created and granted authority
        verify(response, times(1)).addCookie(any(Cookie.class));
        Assert.assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
    }

    @Test
    public void testFilterWithCookie() {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(SecurityConstants.TOKEN_COOKIE_NAME, new AnonymousUser(testSecurityProperties).generateToken());
        when(request.getCookies()).thenReturn(cookies);

        try {
            filter.doFilterInternal(request, response, chain);
        } catch (Exception e) {
            Assert.fail();
        }

        //User should've been authenticated
        verify(response, times(0)).addCookie(any(Cookie.class));
        Assert.assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
    }

    @Test
    public void testFilterWithInvalidToken() {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(SecurityConstants.TOKEN_COOKIE_NAME, "invalid");
        when(request.getCookies()).thenReturn(cookies);

        try {
            filter.doFilterInternal(request, response, chain);
        } catch (Exception e) {
            Assert.fail();
        }

        //Since the token was named correctly but was invalid, access should be denied (null authentication)
        Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
