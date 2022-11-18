package com.jbhunt.infrastructure.universityhackathon.services;
import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.repository.ParticipantRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantServiceTest {
    @Mock
    ParticipantRepository mockParticipantRepository;
    @InjectMocks
    ParticipantService participantService;

    @Test
    public void checkEmailTestSuccess() {
        Participant participant = new Participant();
        when(mockParticipantRepository.findParticipantBySchoolEmailAddress("email")).thenReturn(Optional.of(participant));

        boolean actual = participantService.checkEmail("email");
        Assert.assertTrue(actual);
        verify(mockParticipantRepository).findParticipantBySchoolEmailAddress("email");
        verifyNoMoreInteractions(mockParticipantRepository);
    }
    @Test
    public void checkEmailTestFailure() {
        Participant participant = new Participant();
        when(mockParticipantRepository.findParticipantBySchoolEmailAddress("email")).thenReturn(Optional.empty());

        boolean actual = participantService.checkEmail("email");
        Assert.assertFalse(actual);
        verify(mockParticipantRepository).findParticipantBySchoolEmailAddress("email");
        verifyNoMoreInteractions(mockParticipantRepository);
    }

    @Test
    public void checkEmailTestException() {
        when(mockParticipantRepository.findParticipantBySchoolEmailAddress("email")).thenThrow(new RuntimeException());

        boolean actual = participantService.checkEmail("email");
        Assert.assertFalse(actual);
        verify(mockParticipantRepository).findParticipantBySchoolEmailAddress("email");
        verifyNoMoreInteractions(mockParticipantRepository);
    }

    @Test
    public void getParticipantsByTeamTestSuccess() {
        Participant participant = new Participant();
        when(mockParticipantRepository.findParticipantsByTeamID(1)).thenReturn(List.of(participant));

        List<Participant> actual = participantService.getParticipantsByTeamID(1);
        Assert.assertEquals(List.of(participant), actual);
        verify(mockParticipantRepository).findParticipantsByTeamID(1);
        verifyNoMoreInteractions(mockParticipantRepository);
    }
}
