package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.services.ParticipantService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantControllerTest {

    @Mock
    ParticipantService mockParticipantService;
    @InjectMocks
    private ParticipantController participantController;

    @Test
    public void checkForEmailTest() {
        when(mockParticipantService.checkEmail("")).thenReturn(true);

        boolean actual = participantController.checkForEmail("");

        Assert.assertTrue(actual);
        verify(mockParticipantService).checkEmail(anyString());
    }
}
