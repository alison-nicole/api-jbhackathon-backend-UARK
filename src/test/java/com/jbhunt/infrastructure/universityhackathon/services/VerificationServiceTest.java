package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.constants.EmailTemplateConstants;
import com.jbhunt.infrastructure.universityhackathon.constants.SecurityConstants;
import com.jbhunt.infrastructure.universityhackathon.enums.CodeType;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AnonymousUser;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AuthorizedUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class VerificationServiceTest {
    @Mock
    private SecurityProperties mockSecurityProperties;

    @Mock
    private EmailService mockEmailService;

    private final SecurityProperties testSecurityProperties = new SecurityProperties(false, "/", 60, "3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam");

    @InjectMocks
    VerificationService verificationService;

    @Test
    public void testGenerateNumericCode(){
        String code = verificationService.generateCode(CodeType.NUMERIC, 8);
        Assert.assertTrue(Pattern.matches("[0-9]+", code));
        Assert.assertEquals(8, code.length());
    }

    @Test
    public void testGenerateAlphabeticalCode(){
        String code = verificationService.generateCode(CodeType.ALPHABETICAL, 8);
        Assert.assertTrue(Pattern.matches("[a-zA-Z]+", code));
        Assert.assertEquals(8, code.length());
    }

    @Test
    public void testGenerateAlphaNumericCode(){
        String code = verificationService.generateCode(CodeType.NUMERIC, 8);
        Assert.assertTrue(Pattern.matches("[a-zA-Z0-9]+", code));
        Assert.assertEquals(8, code.length());
    }

    @Test
    public void testCreateVerificationCookie(){
        AnonymousUser user = new AnonymousUser(testSecurityProperties);
        String code = verificationService.generateCode(CodeType.NUMERIC, 8);

        Cookie cookie = verificationService.createVerificationCookie(user, code);

        Assert.assertEquals(SecurityConstants.VERIFICATION_COOKIE_NAME, cookie.getName());
        Assert.assertNotEquals(code, cookie.getValue()); //The cookie's value should be encrypted, not plaintext
    }

    @Test
    public void testSimpleAuthentication(){
        AnonymousUser user = new AnonymousUser(testSecurityProperties);
        String code = verificationService.generateCode(CodeType.NUMERIC, 8);

        Cookie[] cookieArr = {verificationService.createVerificationCookie(user, code)};
        Optional<AuthorizedUser> authenticated = verificationService.authenticate(code, cookieArr);

        Assert.assertTrue(authenticated.isPresent());
        Assert.assertEquals(user, authenticated.get());
    }

    @Test
    public void testAuthenticationWrongCode(){
        AnonymousUser user = new AnonymousUser(testSecurityProperties);
        String code = verificationService.generateCode(CodeType.NUMERIC, 8);

        Cookie[] cookieArr = {verificationService.createVerificationCookie(user, code)};
        Optional<AuthorizedUser> authenticated = verificationService.authenticate("wrong", cookieArr);

        Assert.assertTrue(authenticated.isEmpty());
    }

    @Test
    public void testAuthenticationNoVerificationCookie(){
        AnonymousUser user = new AnonymousUser(testSecurityProperties);
        String code = verificationService.generateCode(CodeType.NUMERIC, 8);

        verificationService.createVerificationCookie(user, code);
        Optional<AuthorizedUser> authenticated = verificationService.authenticate(code, new Cookie[0]);

        Assert.assertTrue(authenticated.isEmpty());
    }

    @Test
    public void testSendCode() {
        ArgumentCaptor<HashMap<String, String>> templateData = ArgumentCaptor.forClass(HashMap.class);
        ArgumentCaptor<String> recipientEmail = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> templateID = ArgumentCaptor.forClass(String.class);

        doNothing().when(mockEmailService).sendEmail(recipientEmail.capture(), templateID.capture(), templateData.capture());

        //ACT
        verificationService.sendCode("testemail@test.com", "123456");

        //ASSERT
        Assert.assertTrue(templateData.getValue().containsKey("code"));
        Assert.assertEquals("123456", templateData.getValue().get("code"));
        Assert.assertEquals("testemail@test.com", recipientEmail.getValue());
        Assert.assertEquals(EmailTemplateConstants.VERIFICATION_CODE, templateID.getValue());
    }

}
