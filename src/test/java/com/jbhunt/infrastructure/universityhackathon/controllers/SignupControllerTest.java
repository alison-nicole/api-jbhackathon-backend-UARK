package com.jbhunt.infrastructure.universityhackathon.controllers;
import com.jbhunt.infrastructure.universityhackathon.data.dto.SignUpFormDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.services.SignupService;
import com.jbhunt.infrastructure.universityhackathon.services.VerificationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SignupControllerTest {

    @Mock
    private SignupService mockSignupService;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private SignupController signupController;

    @Test
    public void testSaveParticipant() {
        Participant fake = new Participant();
        fake.setFirstName("First");
        SignUpFormDTO signUpFormDTO = new SignUpFormDTO();
        when(mockSignupService.saveParticipant(signUpFormDTO)).thenReturn(fake);

        ResponseEntity<Participant> actual = signupController.saveParticipant(signUpFormDTO);

        Assert.assertThat(actual.getStatusCode(), is(HttpStatus.CREATED));
        Assert.assertThat(actual.getBody().getFirstName(), is("First"));
        verify(mockSignupService).saveParticipant(any());
    }

    @Test
    public void testVerifyEmail(){
        when(verificationService.generateAndSend(anyString(), any(), anyInt())).thenReturn("123456");

        int actual = signupController.verifyEmail("email");

        verify(verificationService).generateAndSend(anyString(), any(), anyInt());
        Assert.assertEquals(123456, actual);
    }
}
