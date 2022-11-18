package com.jbhunt.infrastructure.universityhackathon.util;

import lombok.experimental.UtilityClass;
import javax.servlet.http.Cookie;

@UtilityClass
public class CookieUtil {
    public static Cookie createCookie(String name, String value, String path, int lifetime){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(lifetime);

        return cookie;
    }

    public static Cookie createSecureCookie(String name, String value, String path, int lifetime){
        Cookie cookie = createCookie(name, value, path, lifetime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        return cookie;
    }
}
