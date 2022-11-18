package com.jbhunt.infrastructure.universityhackathon.services;
import com.jbhunt.infrastructure.universityhackathon.constants.EmailTemplateConstants;
import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgingDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.*;
import com.jbhunt.infrastructure.universityhackathon.mocks.*;
import com.jbhunt.infrastructure.universityhackathon.repository.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JudgingServiceTest {

    @Mock
    private TeamRepository mockTeamRepository;
    @Mock
    private JudgeRepository mockJudgeRepository;
    @Mock
    private TeamScoreRepository mockTeamScoreRepository;

    @Mock
    private EmailService mockEmailService;

    @Mock
    private ParticipantService mockParticipantService;

    @Spy
    @InjectMocks
    private JudgingService judgingService;

    @Test
    public void submitValidScoreTest() {
        //Arrange
        JudgingDTO judgingDTO = JudgingDTOMock.getValidJudgingDTO();

        when(mockJudgeRepository.findJudgeByFirstNameAndLastName("Bob", "Ross")).thenReturn(Optional.of(JudgeMock.getJudgeMock()));
        when(mockTeamRepository.findTeamByTeamName("Best Team")).thenReturn(Optional.of(TeamMock.getTeamMock()));
        doReturn(false).when(judgingService).isFinishedWithJudging("Best Team");
        when(mockTeamScoreRepository.save(any(TeamScore.class))).thenReturn(TeamScoreMock.getTeamScore());

        //Act
        TeamScore teamScore = judgingService.submitScore(judgingDTO);

        //Assert
        verify(mockJudgeRepository).findJudgeByFirstNameAndLastName("Bob", "Ross");
        verify(mockTeamRepository).findTeamByTeamName("Best Team");
        verify(judgingService).isFinishedWithJudging("Best Team");
        verify(mockTeamScoreRepository).save(any(TeamScore.class));

        Assert.assertNotNull(teamScore);
        Assert.assertFalse(judgingService.isFinishedWithJudging("Best Team"));
        Assert.assertEquals(50, teamScore.getScore());
        Assert.assertEquals(Integer.valueOf(4), teamScore.getJudgeID());
        Assert.assertEquals(Integer.valueOf(1), teamScore.getTeamID());
        Assert.assertNotNull(teamScore.getScoreID());
        Assert.assertEquals("Great Job", teamScore.getFeedback());

        verifyNoMoreInteractions(mockTeamRepository);
        verifyNoMoreInteractions(mockJudgeRepository);
        verifyNoMoreInteractions(mockTeamScoreRepository);
    }

    @Test
    public void submitScoreWithInvalidJudgeTest() {
        //Arrange
        JudgingDTO judgingDTO = JudgingDTOMock.getInvalidJudgeJudgingDTO();
        when(mockJudgeRepository.findJudgeByFirstNameAndLastName("Invalid", "Invalid")).thenReturn(Optional.empty());

        //Act
        assertThrows(IllegalArgumentException.class, () -> judgingService.submitScore(judgingDTO));

        //Assert
        verify(mockJudgeRepository).findJudgeByFirstNameAndLastName("Invalid", "Invalid");

        verifyNoMoreInteractions(mockJudgeRepository);
    }

    @Test
    public void submitScoreWithInvalidTeamTest() {
        //Arrange
        JudgingDTO judgingDTO = JudgingDTOMock.getValidJudgingDTO();

        when(mockJudgeRepository.findJudgeByFirstNameAndLastName("Bob", "Ross")).thenReturn(Optional.of(JudgeMock.getJudgeMock()));
        when(mockTeamRepository.findTeamByTeamName("Best Team")).thenReturn(Optional.empty());

        //Act
        assertThrows(IllegalArgumentException.class, () -> judgingService.submitScore(judgingDTO));

        //Assert
        verify(mockJudgeRepository).findJudgeByFirstNameAndLastName("Bob", "Ross");
        verify(mockTeamRepository).findTeamByTeamName("Best Team");

        verifyNoMoreInteractions(mockTeamRepository);
        verifyNoMoreInteractions(mockJudgeRepository);
    }

    @Test
    public void submitFinalScoreToTeam() {
        //Arrange
        JudgingDTO judgingDTO = JudgingDTOMock.getValidJudgingDTO();

        when(mockJudgeRepository.findJudgeByFirstNameAndLastName("Bob", "Ross")).thenReturn(Optional.of(JudgeMock.getJudgeMock()));
        when(mockTeamRepository.findTeamByTeamName("Best Team")).thenReturn(Optional.of(TeamMock.getTeamMock()));
        doReturn(true).when(judgingService).isFinishedWithJudging("Best Team");
        doReturn(TeamMock.getTeamWithScoreMock()).when(judgingService).calculateFinalScore("Best Team");
        when(mockTeamScoreRepository.save(any(TeamScore.class))).thenReturn(TeamScoreMock.getTeamScore());

        //Act
        TeamScore teamScore = judgingService.submitScore(judgingDTO);

        //Assert
        verify(mockJudgeRepository).findJudgeByFirstNameAndLastName("Bob", "Ross");
        verify(mockTeamRepository).findTeamByTeamName("Best Team");
        verify(judgingService).isFinishedWithJudging("Best Team");
        verify(judgingService).calculateFinalScore("Best Team");
        verify(mockTeamScoreRepository).save(any(TeamScore.class));

        Assert.assertNotNull(teamScore);
        Assert.assertEquals(50, teamScore.getScore());
        Assert.assertEquals(Integer.valueOf(4), teamScore.getJudgeID());
        Assert.assertEquals(Integer.valueOf(1), teamScore.getTeamID());
        Assert.assertNotNull(teamScore.getScoreID());
        Assert.assertEquals("Great Job", teamScore.getFeedback());

        verifyNoMoreInteractions(mockTeamRepository);
        verifyNoMoreInteractions(mockJudgeRepository);
        verifyNoMoreInteractions(mockTeamScoreRepository);
    }

    @Test
    public void isFinishedJudgingFalseTest() {
        //Arrange
        List<TeamScore> mockTeamScoreList = new ArrayList<>();
        mockTeamScoreList.add(TeamScoreMock.getTeamScore());

        when(mockTeamRepository.findTeamByTeamName("Best Team")).thenReturn(Optional.of(TeamMock.getTeamMock()));
        when(mockJudgeRepository.findAll()).thenReturn(JudgeMock.getListOfJudges());
        when(mockTeamScoreRepository.getTeamScoresByTeamID(1)).thenReturn(mockTeamScoreList);

        //Act
        boolean isFinishedJudging = judgingService.isFinishedWithJudging("Best Team");

        //Assert
        verify(mockTeamRepository).findTeamByTeamName("Best Team");
        verify(mockJudgeRepository).findAll();
        verify(mockTeamScoreRepository).getTeamScoresByTeamID(1);

        Assert.assertFalse(isFinishedJudging);

        verifyNoMoreInteractions(mockTeamRepository);
        verifyNoMoreInteractions(mockJudgeRepository);
        verifyNoMoreInteractions(mockTeamScoreRepository);
    }

    @Test
    public void isFinishedJudgingTrueTest() {
        //Arrange
        when(mockTeamRepository.findTeamByTeamName("Best Team")).thenReturn(Optional.of(TeamMock.getTeamMock()));
        when(mockJudgeRepository.findAll()).thenReturn(JudgeMock.getListOfJudges());
        when(mockTeamScoreRepository.getTeamScoresByTeamID(1)).thenReturn(TeamScoreMock.getListTeamScores());

        //Act
        boolean isFinishedJudging = judgingService.isFinishedWithJudging("Best Team");

        //Assert
        verify(mockTeamRepository).findTeamByTeamName("Best Team");
        verify(mockJudgeRepository).findAll();
        verify(mockTeamScoreRepository).getTeamScoresByTeamID(1);

        Assert.assertTrue(isFinishedJudging);

        verifyNoMoreInteractions(mockTeamRepository);
        verifyNoMoreInteractions(mockJudgeRepository);
        verifyNoMoreInteractions(mockTeamScoreRepository);
    }

    @Test
    public void calculateFinalScoreTest() {
        //Arrange
        when(mockTeamRepository.findTeamByTeamName("Best Team")).thenReturn(Optional.of(TeamMock.getTeamMock()));
        when(mockTeamRepository.save(any(Team.class))).thenReturn(TeamMock.getTeamWithScoreMock());
        when(mockTeamScoreRepository.getTeamScoresByTeamID(1)).thenReturn(TeamScoreMock.getListTeamScores());

        //Act
        Team team = judgingService.calculateFinalScore("Best Team");

        //Assert
        verify(mockTeamRepository).findTeamByTeamName("Best Team");
        verify(mockTeamScoreRepository).getTeamScoresByTeamID(1);
        verify(mockTeamRepository).save(any(Team.class));

        Assert.assertNotNull(team);
        Assert.assertEquals("TEST 1", team.getTeamName());
        Assert.assertEquals("123456", team.getTeamCode());
        Assert.assertEquals("#ffffff", team.getTeamColorCode());
        Assert.assertEquals("medal", team.getTeamIconCode());
        Assert.assertEquals(Integer.valueOf(1), team.getTeamID());
        Assert.assertEquals(Integer.valueOf(700), team.getScore());

        verifyNoMoreInteractions(mockTeamRepository);
        verifyNoMoreInteractions(mockTeamScoreRepository);
    }

    @Test
    public void getAllScoreTeamsTest() {
        //Arrange
        when(mockTeamRepository.findAll()).thenReturn(TeamMock.getTeamListWithScoredTeam());

        //Act
        List<Team> list = judgingService.getAllScoredTeams();

        //Assert
        verify(mockTeamRepository).findAll();

        Assert.assertEquals(1, list.size());
        Assert.assertEquals("TEST 1", list.get(0).getTeamName());
        Assert.assertEquals(Integer.valueOf(1), list.get(0).getTeamID());

        verifyNoMoreInteractions(mockTeamRepository);
    }

    @Test
    public void getAllScoreTeamsNoneTest() {
        //Arrange
        when(mockTeamRepository.findAll()).thenReturn(TeamMock.getTeamList());

        //Act
        List<Team> list = judgingService.getAllScoredTeams();

        //Assert
        verify(mockTeamRepository).findAll();

        Assert.assertEquals(0, list.size());

        verifyNoMoreInteractions(mockTeamRepository);
    }

    @Test
    public void sendFeedbackEmailTest() {
        //Arrange
        String testEmail = "test@email";
        String testTeamName = "Test Team";
        String testBase64EncodedImage = "Test Image";

        HashMap<String, String> testTemplateData = new HashMap<>();
        testTemplateData.put(EmailTemplateConstants.TEAM_NAME, testTeamName);
        testTemplateData.put(EmailTemplateConstants.IMAGE_SOURCE, testBase64EncodedImage);

        //Act
        judgingService.sendFeedbackEmail(testEmail, testTeamName, testBase64EncodedImage);

        //Assert
        verify(mockEmailService).sendEmail(testEmail, EmailTemplateConstants.JUDGE_FEEDBACK_FORM, testTemplateData);
    }

    @Test
    public void sendTeamFeedbackTest() {
        //Arrange
        int testTeamID = 1;
        String testBase64EncodedImage = getActualBase64EncodedImage();

        Team testTeam = new Team();
        testTeam.setTeamID(testTeamID);
        testTeam.setTeamName("Test Team");

        Participant testParticipant = new Participant();
        testParticipant.setTeamID(testTeamID);
        testParticipant.setSchoolEmailAddress("test@email");

        when(mockTeamRepository.findById(testTeamID)).thenReturn(Optional.of(testTeam));
        when(mockParticipantService.getParticipantsByTeamID(testTeamID)).thenReturn(List.of(testParticipant));

        //Act
        judgingService.sendTeamFeedback(testTeamID, testBase64EncodedImage);

        //Assert
        verify(mockEmailService).sendEmail(eq(testParticipant.getSchoolEmailAddress()), eq(EmailTemplateConstants.JUDGE_FEEDBACK_FORM), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void sendTeamFeedbackBadImageTest() {
        judgingService.sendTeamFeedback(1, "not_an_image");
    }

    @Test(expected = IllegalArgumentException.class)
    public void sendTeamFeedbackNullTeamTest() {
        //Arrange
        int testTeamID = 1;
        String testBase64EncodedImage = getActualBase64EncodedImage();

        when(mockTeamRepository.findById(testTeamID)).thenReturn(Optional.empty());

        //Act
        judgingService.sendTeamFeedback(testTeamID, testBase64EncodedImage);
    }

    @Test
    public void sendTeamFeedbackNoParticipantsTest() {
        //Arrange
        int testTeamID = 1;
        String testBase64EncodedImage = getActualBase64EncodedImage();

        Team testTeam = new Team();
        testTeam.setTeamID(testTeamID);
        testTeam.setTeamName("Test Team");

        when(mockTeamRepository.findById(testTeamID)).thenReturn(Optional.of(testTeam));
        when(mockParticipantService.getParticipantsByTeamID(testTeamID)).thenReturn(List.of());

        //Act
        judgingService.sendTeamFeedback(testTeamID, testBase64EncodedImage);

        //Assert
        verify(mockEmailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    public void verifyEncodedStringIsImageTest() {
        String goodImage = getActualBase64EncodedImage();
        String badImage = "data:image/png;base64, not_an_image";

        Assert.assertTrue(judgingService.verifyEncodedStringIsImage(goodImage));
        Assert.assertFalse(judgingService.verifyEncodedStringIsImage(badImage));
    }

    private String getActualBase64EncodedImage(){
        return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAH0AAAB9CAMAAAC4XpwXAAAAElBMVEX8/vz///+YmJicnJxbW1sYGBgvGLOMAAAAYklEQVRoge3bQQEAIAzEsLGBf8vg4nikBqKgdTrW7OqVa55eqVbT6XQ6nU6n0+l0Op1Op9PpdDqdTqfT6XQ6nU6n0+l0Op1Op9PpdDqdTqfT6XQ6nU6n0+lf6xN9gfcEP+gLzoUF2DcUEJgAAAAASUVORK5CYII=";
    }
}
