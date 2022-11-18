package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.constants.EmailTemplateConstants;
import com.jbhunt.infrastructure.universityhackathon.constants.SecurityConstants;
import com.jbhunt.infrastructure.universityhackathon.enums.CodeType;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AuthorizedUser;
import com.jbhunt.infrastructure.universityhackathon.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {
    private final SecurityProperties securityProperties;
    private final EmailService emailService;
    private String storedSalt;
    private AuthorizedUser storedUser;
    private static final char[] validAlphabetical = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz".toCharArray();
    private static final char[] validNums = "0123456789".toCharArray();

    public String generateCode(CodeType codeType, int length){
        SecureRandom random = new SecureRandom();
        switch (codeType) {
            case NUMERIC: return RandomStringUtils.random(length, 0, 0, false, true, validNums, random);
            case ALPHABETICAL: return RandomStringUtils.random(length, 0, 0, true, false, validAlphabetical, random);
            case ALPHA_NUMERIC: return RandomStringUtils.random(length, 0, 0, true, true, ArrayUtils.addAll(validAlphabetical, validNums), random);
            default: return "";
        }
    }

    public String generateAndSend(String email, CodeType codeType, int length){
        String code = generateCode(codeType, length);
        sendCode(email, code);
        return code;
    }

    public Cookie createVerificationCookie(AuthorizedUser user, String code){
        if(storedUser != null) log.warn("Overwriting verification cookie");
        storedUser = user;
        log.info("Verification cookie created");

        storedSalt = generateCode(CodeType.ALPHA_NUMERIC, 8);

        if(securityProperties.isSecureCookies()) return CookieUtil.createSecureCookie(SecurityConstants.VERIFICATION_COOKIE_NAME, encryptSHA256(code, storedSalt), securityProperties.getCookiePath(),60);
        else{
            log.warn("Verification cookies MUST be secure in a production setting. Unsecure verification cookies are for development purposes ONLY!");
            return CookieUtil.createCookie(SecurityConstants.VERIFICATION_COOKIE_NAME, encryptSHA256(code, storedSalt), securityProperties.getCookiePath(), 60);
        }
    }

    public Optional<AuthorizedUser> authenticate(String expected, Cookie[] requestCookies){
        if (storedUser == null || requestCookies == null) return Optional.empty();

        for (Cookie cookie : requestCookies){
            if (cookie.getName().equals(SecurityConstants.VERIFICATION_COOKIE_NAME)){
                if(cookie.getValue().equals(encryptSHA256(expected, storedSalt))){
                    log.info("Authenticated user " + storedUser.getClass());
                    AuthorizedUser authenticated = storedUser;
                    storedUser = null;
                    return Optional.of(authenticated);
                }
                log.info("Incorrect verification code");
                return Optional.empty();
            }
        }

        log.info("Could not find verification cookie");
        if(securityProperties.isSecureCookies()) log.info("Secure cookies may only be read on secure connections. Ensure your connection is secure, as this may be the issue");
        return Optional.empty();
    }

    private static String encryptSHA256(String toHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(toHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public void sendCode(String email, String code){
        var templateData = new HashMap<String, String>();
        templateData.put(EmailTemplateConstants.CODE, code);
        emailService.sendEmail(email, EmailTemplateConstants.VERIFICATION_CODE, templateData);
    }
}

