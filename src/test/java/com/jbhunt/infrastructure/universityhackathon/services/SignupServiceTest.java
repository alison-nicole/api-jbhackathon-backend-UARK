package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.constants.EmailTemplateConstants;
import com.jbhunt.infrastructure.universityhackathon.data.dto.SignUpFormDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.*;
import com.jbhunt.infrastructure.universityhackathon.mocks.ParticipantMock;
import com.jbhunt.infrastructure.universityhackathon.mocks.SignupFormDTOMock;
import com.jbhunt.infrastructure.universityhackathon.mocks.TeamMock;
import com.jbhunt.infrastructure.universityhackathon.repository.*;
import com.jbhunt.infrastructure.universityhackathon.repository.ParticipantRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class SignupServiceTest {

    @Mock
    private TeamRepository mockTeamRepository;

    @Mock
    private ParticipantRepository mockParticipantRepository;

    @Mock
    private HackathonEventService mockHackathonEventService;


    @Mock
    private TeamService mockTeamService;

    @Mock
    private EmailService mockEmailService;

    @InjectMocks
    private SignupService signUpService;

    @Test(expected = Exception.class)
    public void formMissingFields() {
        SignUpFormDTO signUpFormDTO = new SignUpFormDTO();

        signUpService.saveParticipant(signUpFormDTO);
    }

    public List<HackathonEvent> getHackathonEvent() {
        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);

        ArrayList<HackathonEvent> hackathonEventList = new ArrayList<>();
        hackathonEventList.add(hackathonEvent);
        return hackathonEventList;
    }

    public static Team getTeamMock() {
        Team team1 = new Team();
        team1.setTeamName("TestTeam");
        team1.setTeamCode("123456");
        team1.setTeamColorCode("#ffffff");
        team1.setTeamIconCode("medal");
        team1.setTeamID(1);
        team1.setTeamOwnerID(1);
        team1.setMemberCount(0);
        team1.setGraduateCount(0);
        return team1;
    }

    public static Team getExistingTeamMock() {
        Team team1 = new Team();
        team1.setTeamName("TestTeam");
        team1.setTeamCode("123456");
        team1.setTeamColorCode("#ffffff");
        team1.setTeamIconCode("medal");
        team1.setTeamID(1);
        team1.setTeamOwnerID(1);
        team1.setMemberCount(1);
        team1.setGraduateCount(0);
        return team1;
    }

    @Test
    public void saveValidFormWithoutTeam() {
        //ARRANGE
        var hackathonEvent = getHackathonEvent();
        var testParticipant = ParticipantMock.getTestParticipant();

        when(mockHackathonEventService.getCurrentHackathon()).thenReturn(hackathonEvent);
        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(ParticipantMock.getCreatedParticipant(testParticipant));

        //ACT
        Participant createdParticipant = signUpService.saveParticipant(SignupFormDTOMock.getValidSignUpFormDTOWithoutTeam());

        //ASSERT
        verify(mockHackathonEventService).getCurrentHackathon();
        verify(mockParticipantRepository, times(2)).save(any(Participant.class));

        Assert.assertNotNull(createdParticipant);
        Assert.assertEquals(testParticipant, createdParticipant);
        verifyNoMoreInteractions(mockParticipantRepository);
        verifyNoMoreInteractions(mockHackathonEventService);
    }

    @Test
    public void saveParticipantWithTechStackFromValidForm(){
        //ARRANGE
        var hackathonEvent = getHackathonEvent();
        var testParticipant = ParticipantMock.getTestParticipantWithTechStack();

        when(mockHackathonEventService.getCurrentHackathon()).thenReturn(hackathonEvent);
        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(ParticipantMock.getCreatedParticipant(testParticipant));

        //ACT
        Participant createdParticipant = signUpService.saveParticipant(SignupFormDTOMock.getValidSignUpWithTechStackFormDTOWithoutTeam());

        //ASSERT
        verify(mockHackathonEventService).getCurrentHackathon();
        verify(mockParticipantRepository, times(2)).save(any(Participant.class));

        Assert.assertNotNull(createdParticipant);
        Assert.assertEquals(testParticipant, createdParticipant);

        verifyNoMoreInteractions(mockParticipantRepository);
        verifyNoMoreInteractions(mockHackathonEventService);
    }



    @Test
    public void saveValidFormWithCreatedTeam() {
        //ARRANGE
        var hackathonEvent = getHackathonEvent();
        var testParticipant = ParticipantMock.getTestParticipant();
        testParticipant.setTeamID(1);
        var testTeam = getTeamMock();

        when(mockHackathonEventService.getCurrentHackathon()).thenReturn(hackathonEvent);
        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(ParticipantMock.getCreatedParticipant(testParticipant));
        when(mockTeamService.createTeam("TestTeam", 1, true, 0, 0, "#ffffff", "medal")).thenReturn(testTeam);
        when(mockTeamRepository.findTeamByTeamName("TestTeam")).thenReturn(Optional.empty());
        when(mockTeamRepository.save(any(Team.class))).thenReturn(testTeam);

        //ACT
        Participant createdParticipant = signUpService.saveParticipant(SignupFormDTOMock.getValidSignUpFormDTOCreatedTeam());

        //ASSERT
        verify(mockHackathonEventService).getCurrentHackathon();
        verify(mockParticipantRepository, times(2)).save(any(Participant.class));
        verify(mockTeamService).createTeam("TestTeam", 1, true, 0, 0, "#ffffff", "medal");
        verify(mockTeamRepository).findTeamByTeamName("TestTeam");
        verify(mockTeamRepository).save(any(Team.class));


        Assert.assertNotNull(createdParticipant);
        Assert.assertEquals(testParticipant, createdParticipant);
        Assert.assertEquals(testTeam.getTeamOwnerID(), createdParticipant.getParticipantID());
        Assert.assertEquals(1, createdParticipant.getTeamID().intValue());

        verifyNoMoreInteractions(mockParticipantRepository);
        verifyNoMoreInteractions(mockHackathonEventService);
        verifyNoMoreInteractions(mockTeamService);
    }

    @Test
    public void saveValidFormWithJoinTeam() {
        //ARRANGE
        var hackathonEvent = getHackathonEvent();
        var testParticipant = ParticipantMock.getTestParticipant();
        var testTeam = getExistingTeamMock();
        testParticipant.setParticipantID(2);

        when(mockHackathonEventService.getCurrentHackathon()).thenReturn(hackathonEvent);
        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(ParticipantMock.getCreatedSecondParticipant(testParticipant));
        when(mockTeamRepository.findTeamByTeamName("TestTeam")).thenReturn(Optional.of(testTeam));

        //ACT
        Participant createdParticipant = signUpService.saveParticipant(SignupFormDTOMock.getValidSignUpFormDTOJoinTeam());

        //ASSERT
        verify(mockHackathonEventService).getCurrentHackathon();
        verify(mockParticipantRepository, times(2)).save(any(Participant.class));
        verify(mockTeamRepository).findTeamByTeamName("TestTeam");
        verify(mockTeamRepository).save(any(Team.class));

        Assert.assertNotNull(createdParticipant);
        Assert.assertEquals(testParticipant, createdParticipant);
        Assert.assertNotEquals(testTeam.getTeamOwnerID(), createdParticipant.getParticipantID());
        Assert.assertEquals(1, createdParticipant.getTeamID().intValue());
        Assert.assertEquals(2, testTeam.getMemberCount().intValue());

        verifyNoMoreInteractions(mockParticipantRepository);
        verifyNoMoreInteractions(mockHackathonEventService);
    }

    @Test
    public void saveValidGraduateFormWithCreatedTeam() {
        //ARRANGE
        var hackathonEvent = getHackathonEvent();
        var testParticipant = ParticipantMock.getTestGraduateParticipant();
        testParticipant.setTeamID(1);
        var testTeam = getTeamMock();

        when(mockHackathonEventService.getCurrentHackathon()).thenReturn(hackathonEvent);
        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(ParticipantMock.getCreatedParticipant(testParticipant));
        when(mockTeamRepository.findTeamByTeamName("TestTeam")).thenReturn(Optional.empty());
        when(mockTeamService.createTeam("TestTeam", 1, true, 0, 0, "#ffffff", "medal")).thenReturn(testTeam);
        when(mockTeamRepository.save(any(Team.class))).thenReturn(testTeam);

        //ACT
        Participant createdParticipant = signUpService.saveParticipant(SignupFormDTOMock.getValidSignUpFormDTOCreatedTeam());

        //ASSERT
        verify(mockHackathonEventService).getCurrentHackathon();
        verify(mockParticipantRepository, times(2)).save(any(Participant.class));
        verify(mockTeamService).createTeam("TestTeam", 1, true, 0, 0, "#ffffff", "medal");
        verify(mockTeamRepository).findTeamByTeamName("TestTeam");
        verify(mockTeamRepository).save(any(Team.class));


        Assert.assertNotNull(createdParticipant);
        Assert.assertEquals(testParticipant, createdParticipant);
        Assert.assertEquals(testTeam.getTeamOwnerID(), createdParticipant.getParticipantID());
        Assert.assertEquals(1, createdParticipant.getTeamID().intValue());
        Assert.assertEquals(1, testTeam.getMemberCount().intValue());
        Assert.assertEquals(1, testTeam.getGraduateCount().intValue());

        verifyNoMoreInteractions(mockParticipantRepository);
        verifyNoMoreInteractions(mockHackathonEventService);
        verifyNoMoreInteractions(mockTeamService);
    }

    @Test
    public void saveValidGraduateFormWithJoinTeam() {
        //ARRANGE
        var hackathonEvent = getHackathonEvent();
        var testParticipant = ParticipantMock.getTestGraduateParticipant();
        var testTeam = getExistingTeamMock();
        testParticipant.setParticipantID(2);

        when(mockHackathonEventService.getCurrentHackathon()).thenReturn(hackathonEvent);
        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(ParticipantMock.getCreatedSecondParticipant(testParticipant));
        when(mockTeamRepository.findTeamByTeamName("TestTeam")).thenReturn(Optional.of(testTeam));
        when(mockTeamRepository.save(any(Team.class))).thenReturn(testTeam);

        //ACT
        Participant createdParticipant = signUpService.saveParticipant(SignupFormDTOMock.getValidSignUpFormDTOJoinTeam());

        //ASSERT
        verify(mockHackathonEventService).getCurrentHackathon();
        verify(mockParticipantRepository, times(2)).save(any(Participant.class));
        verify(mockTeamRepository).save(any(Team.class));

        Assert.assertNotNull(createdParticipant);
        Assert.assertEquals(testParticipant, createdParticipant);
        Assert.assertNotEquals(testTeam.getTeamOwnerID(), createdParticipant.getParticipantID());
        Assert.assertEquals(1, createdParticipant.getTeamID().intValue());
        Assert.assertEquals(2, testTeam.getMemberCount().intValue());
        Assert.assertEquals(1, testTeam.getGraduateCount().intValue());

        verifyNoMoreInteractions(mockParticipantRepository);
        verifyNoMoreInteractions(mockHackathonEventService);
    }
    @Test
    public void testManageParticipantsWithoutTeam() {
        var participantList = ParticipantMock.getListParticipantsWithoutTeam(4);
        var teamList = TeamMock.getTeamList(1);
        var testTeam = getTeamMock();

        when(mockParticipantRepository.findParticipantsByHackathonEventID(1)).thenReturn(participantList);
        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(participantList.get(0));
        when(mockTeamRepository.findAllByHackathonEventID(1)).thenReturn(teamList);
        when(mockTeamRepository.save(any(Team.class))).thenReturn(testTeam);

        signUpService.manageParticipantsWithoutTeam(1);

        verify(mockParticipantRepository).findParticipantsByHackathonEventID(1);
        verify(mockTeamRepository).findAllByHackathonEventID(1);
        verify(mockParticipantRepository, times(4)).save(any(Participant.class));
        verify(mockTeamRepository, times(4)).save(any(Team.class));

        verifyNoMoreInteractions(mockParticipantRepository);
        verifyNoMoreInteractions(mockTeamRepository);
    }

    @Test
    public void testCreateNeededTeams() {
        var testTeam = getTeamMock();
        var teamList = TeamMock.getTeamList(0);

        when(mockTeamRepository.findTeamByTeamName(any(String.class))).thenReturn(Optional.empty());
        when(mockTeamService.createTeam(any(String.class), eq(-1), eq(true), eq(0), eq(0), any(String.class), any(String.class))).thenReturn(testTeam);

        var newTeamList = signUpService.createNeededTeams(3, teamList);

        verify(mockTeamRepository, times(3)).findTeamByTeamName(any(String.class));
        verify(mockTeamService, times(3)).createTeam(any(String.class), eq(-1), eq(true), eq(0), eq(0), any(String.class), any(String.class));

        Assert.assertEquals(3, newTeamList.size());

        verifyNoMoreInteractions(mockParticipantRepository);
        verifyNoMoreInteractions(mockTeamRepository);
    }

    @Test
    public void testDistributeTeams() {
        var participantList = ParticipantMock.getListParticipantsWithoutTeam(12);
        var teamList = TeamMock.getTeamList(3);

        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(participantList.get(0));

        signUpService.distribute(participantList, teamList);

        verify(mockParticipantRepository).save(any(Participant.class));

        verifyNoMoreInteractions(mockParticipantRepository);
    }

    @Test
    public void testDistributeTeamsFillTeam() {
        var participantList = ParticipantMock.getListParticipantsWithoutTeam(1);
        var teamList = TeamMock.getTeamList(1);
        teamList.get(0).setMemberCount(5);

        when(mockParticipantRepository.save(any(Participant.class))).thenReturn(participantList.get(0));

        signUpService.distribute(participantList, teamList);

        verify(mockParticipantRepository).save(any(Participant.class));

        Assert.assertTrue(teamList.isEmpty());

        verifyNoMoreInteractions(mockParticipantRepository);
    }

    @Test
    public void testSendRegistrationConfirmationEmail() {
        //ARRANGE
        var testParticipant = ParticipantMock.getTestParticipant();
        testParticipant.setTeamID(1);
        ArgumentCaptor<HashMap<String, String>> templateData = ArgumentCaptor.forClass(HashMap.class);
        ArgumentCaptor<String> recipientEmail = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> templateID = ArgumentCaptor.forClass(String.class);

        when(mockTeamRepository.findTeamByTeamID(1)).thenReturn(Optional.of(getTeamMock()));
        doNothing().when(mockEmailService).sendEmail(recipientEmail.capture(), templateID.capture(), templateData.capture());

        //ACT
        signUpService.sendRegistrationConfirmationEmail(testParticipant);

        //ASSERT
        verify(mockTeamRepository).findTeamByTeamID(1);

        Assert.assertTrue(templateData.getValue().containsKey("first_name"));
        Assert.assertTrue(templateData.getValue().containsKey("team_code"));
        Assert.assertEquals("First", templateData.getValue().get("first_name"));
        Assert.assertEquals("123456", templateData.getValue().get("team_code"));
        Assert.assertEquals("testemail@test.com", recipientEmail.getValue());
        Assert.assertEquals(EmailTemplateConstants.REGISTRATION_CONFIRM_TEAMCODE, templateID.getValue());

        verifyNoMoreInteractions(mockTeamService);
    }

    @Test
    public void testSendRegistrationConfirmationEmailNoTeamID() {
        //ARRANGE
        var testParticipant = ParticipantMock.getTestParticipant();
        ArgumentCaptor<HashMap<String, String>> templateData = ArgumentCaptor.forClass(HashMap.class);
        ArgumentCaptor<String> recipientEmail = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> templateID = ArgumentCaptor.forClass(String.class);

        doNothing().when(mockEmailService).sendEmail(recipientEmail.capture(), templateID.capture(), templateData.capture());

        //ACT
        signUpService.sendRegistrationConfirmationEmail(testParticipant);

        //ASSERT
        verify(mockTeamRepository, never()).findTeamByTeamID(any(Integer.class));

        Assert.assertTrue(templateData.getValue().containsKey("first_name"));
        Assert.assertEquals("First", templateData.getValue().get("first_name"));
        Assert.assertEquals("testemail@test.com", recipientEmail.getValue());
        Assert.assertEquals(EmailTemplateConstants.REGISTRATION_CONFIRM_NO_TEAMCODE, templateID.getValue());

        verifyNoMoreInteractions(mockTeamService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateRequestInvalidTeamName() {
        SignUpFormDTO request = new SignUpFormDTO();
        request.setTeamName("12345");

        signUpService.validateParticipantRequest(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateRequestInvalidFirstName(){
        SignUpFormDTO request = new SignUpFormDTO();
        request.setFirstName("");

        signUpService.validateParticipantRequest(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateRequestInvalidLastName(){
        SignUpFormDTO request = new SignUpFormDTO();
        request.setFirstName("First");
        request.setLastName("");

        signUpService.validateParticipantRequest(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateRequestInvalidEmail(){
        SignUpFormDTO request = new SignUpFormDTO();
        request.setFirstName("First");
        request.setLastName("Last");
        request.setSchoolEmailAddress("testemail");

        signUpService.validateParticipantRequest(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateRequestTooLongEmail(){
        SignUpFormDTO request = new SignUpFormDTO();
        request.setFirstName("First");
        request.setLastName("Last");
        request.setSchoolEmailAddress("test01234567890123456789012345678901234567890@email.com");

        signUpService.validateParticipantRequest(request);
    }
}
