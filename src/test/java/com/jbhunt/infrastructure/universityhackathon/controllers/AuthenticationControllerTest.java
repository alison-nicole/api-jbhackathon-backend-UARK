package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.entity.Judge;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AnonymousUser;
import com.jbhunt.infrastructure.universityhackathon.services.JudgeService;
import com.jbhunt.infrastructure.universityhackathon.services.VerificationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {
    @Mock
    private VerificationService verificationService;

    @Mock
    private JudgeService judgeService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationController authenticationController;

    private final SecurityProperties testSecurityProperties = new SecurityProperties(false, "/", 60, "3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam");

    @Test
    public void testAuthenticate() {
        when(verificationService.authenticate(anyString(), any())).thenReturn(Optional.of(new AnonymousUser(testSecurityProperties)));

        ResponseEntity<Void> entity = authenticationController.authenticate("code", request, response);

        verify(verificationService, times(1)).authenticate(anyString(), any());
        verify(response, times(1)).addCookie(any());
        Assert.assertEquals(200, entity.getStatusCodeValue());
    }

    @Test
    public void testAuthenticateFailedVerification() {
        when(verificationService.authenticate(anyString(), any())).thenReturn(Optional.empty());

        ResponseEntity<Void> entity = authenticationController.authenticate("code", request, response);

        verify(verificationService, times(1)).authenticate(anyString(), any());
        verify(response, times(0)).addCookie(any());
        Assert.assertEquals(401, entity.getStatusCodeValue());
    }

    @Test
    public void testVerifyJudge(){
        Judge judge = new Judge();
        judge.setJudgeID(1);

        when(judgeService.getJudgeByEmail("email")).thenReturn(Optional.of(judge));
        when(verificationService.generateAndSend(anyString(), any(), anyInt())).thenReturn("123456");

        ResponseEntity<String> entity = authenticationController.verifyJudge("email", response);

        verify(judgeService, times(1)).getJudgeByEmail("email");
        verify(verificationService, times(1)).generateAndSend(anyString(), any(), anyInt());
        verify(response, times(1)).addCookie(any());
        Assert.assertEquals(201, entity.getStatusCodeValue());
    }

    @Test
    public void testVerifyJudgeNotFound(){
        when(judgeService.getJudgeByEmail("email")).thenReturn(Optional.empty());

        ResponseEntity<String> entity = authenticationController.verifyJudge("email", response);

        verify(judgeService, times(1)).getJudgeByEmail("email");
        verify(verificationService, times(0)).generateAndSend(anyString(), any(), anyInt());
        verify(response, times(0)).addCookie(any());
        Assert.assertEquals(400, entity.getStatusCodeValue());
    }
}
