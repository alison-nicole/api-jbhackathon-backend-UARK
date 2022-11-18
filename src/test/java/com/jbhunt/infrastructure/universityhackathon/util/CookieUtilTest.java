package com.jbhunt.infrastructure.universityhackathon.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.Cookie;

@RunWith(MockitoJUnitRunner.class)
public class CookieUtilTest {
    @Test
    public void testCreateSecureCookie(){
        Cookie cookie = CookieUtil.createSecureCookie("TestName", "TestValue", "TestPath", 60);

        Assert.assertEquals("TestName", cookie.getName());
        Assert.assertEquals("TestValue", cookie.getValue());
        Assert.assertEquals(60, cookie.getMaxAge());
        Assert.assertTrue(cookie.isHttpOnly());
        Assert.assertTrue(cookie.getSecure());
    }
}
