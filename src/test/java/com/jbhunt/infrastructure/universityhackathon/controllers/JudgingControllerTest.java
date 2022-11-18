package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgingDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import com.jbhunt.infrastructure.universityhackathon.entity.TeamScore;
import com.jbhunt.infrastructure.universityhackathon.mocks.JudgingDTOMock;
import com.jbhunt.infrastructure.universityhackathon.mocks.TeamMock;
import com.jbhunt.infrastructure.universityhackathon.mocks.TeamScoreMock;
import com.jbhunt.infrastructure.universityhackathon.services.JudgingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JudgingControllerTest {
    @Mock
    private JudgingService mockJudgingService;

    @InjectMocks
    private JudgingController judgingController;

    @Test
    public void saveParticipantTest() {
        //Arrange
        JudgingDTO judgingDTO = JudgingDTOMock.getValidJudgingDTO();

        when(mockJudgingService.submitScore(any(JudgingDTO.class))).thenReturn(TeamScoreMock.getTeamScore());
        //Act
        ResponseEntity<TeamScore> teamScoreResponseEntity = judgingController.saveParticipant(judgingDTO);

        //Assert
        verify(mockJudgingService).submitScore(any(JudgingDTO.class));

        Assert.assertEquals(HttpStatus.CREATED, teamScoreResponseEntity.getStatusCode());
        Assert.assertEquals(50, Objects.requireNonNull(teamScoreResponseEntity.getBody()).getScore());

        verifyNoMoreInteractions(mockJudgingService);
    }

    @Test
    public void getAllScoredTeams() {
        //Arrange
        List<Team> mockTeamList = new ArrayList<>();
        mockTeamList.add(TeamMock.getTeamWithScoreMock());
        when(mockJudgingService.getAllScoredTeams()).thenReturn(mockTeamList);
        //Act
        List<Team> teamListResponse = judgingController.getAllScoredTeams();

        //Assert
        verify(mockJudgingService).getAllScoredTeams();

        Assert.assertNotNull(teamListResponse);
        Assert.assertEquals(1, teamListResponse.size());

        verifyNoMoreInteractions(mockJudgingService);
    }

    @Test
    public void sendJudgeFormTest() {
        //Arrange
        int teamID = 1;
        String base64EncodedImage = "base64EncodedImage";

        //Act
        ResponseEntity<String> response = judgingController.sendJudgeForm(teamID, base64EncodedImage);

        //Assert
        verify(mockJudgingService).sendTeamFeedback(teamID, base64EncodedImage);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Email sent", response.getBody());

        verifyNoMoreInteractions(mockJudgingService);
    }
}
