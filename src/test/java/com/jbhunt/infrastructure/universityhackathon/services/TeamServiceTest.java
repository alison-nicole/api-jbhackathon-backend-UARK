package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.data.dto.TeamDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import com.jbhunt.infrastructure.universityhackathon.mocks.TeamDTOMock;
import com.jbhunt.infrastructure.universityhackathon.mocks.TeamMock;
import com.jbhunt.infrastructure.universityhackathon.repository.ParticipantRepository;
import com.jbhunt.infrastructure.universityhackathon.repository.TeamRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {
    @Mock
    TeamRepository mockTeamRepository;
    @Mock
    ParticipantRepository mockParticipantRepository;
    @Mock
    private HackathonEventService mockHackathonEventService;
    @InjectMocks
    TeamService teamService;

    @Test
    public void createTeamTest() {
        //arrange
        String teamName = "test team";

        Team team = Team.builder()
                .teamName(teamName)
                .build();
        String teamCode = "123456";
        int hackathonEventID = 1;

        team.setTeamName(teamName);
        team.setHackathonEventID(hackathonEventID);
        team.setTeamCode(teamCode);

        //mock hackathon event
        Date oldDate = new Date(System.currentTimeMillis());
        Date newDate = new Date(System.currentTimeMillis()+2000);
        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("Test");
        hackathonEvent.setHackathonEventStartDate(oldDate);
        hackathonEvent.setHackathonEventEndDate(newDate);

        List<HackathonEvent> mockResult = new ArrayList<>();
        mockResult.add(hackathonEvent);
        when(mockHackathonEventService.getUpcomingHackathonEvents(anyInt())).thenReturn(mockResult);

        when(mockTeamRepository.save(any())).thenReturn(team);

        //act
        Team actual = teamService.createTeam(teamName);

        //assert
        assertEquals(actual.getTeamName(), team.getTeamName());
        assertEquals(actual.getHackathonEventID(), team.getHackathonEventID());
        assertEquals(actual.getTeamCode(), team.getTeamCode());
    }

    @Test
    public void createTeamTeamColorTeamIconTest() {
        //arrange
        String teamName = "test team";

        Team team = Team.builder()
                .teamName(teamName)
                .teamOwnerID(12345)
                .open(true)
                .teamCode("123456")
                .teamColorCode("#ffffff")
                .teamIconCode("medal")
                .hackathonEventID(1)
                .build();

        //mock hackathon event
        Date oldDate = new Date(System.currentTimeMillis());
        Date newDate = new Date(System.currentTimeMillis()+2000);
        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("Test");
        hackathonEvent.setHackathonEventStartDate(oldDate);
        hackathonEvent.setHackathonEventEndDate(newDate);

        List<HackathonEvent> mockResult = new ArrayList<>();
        mockResult.add(hackathonEvent);
        when(mockHackathonEventService.getUpcomingHackathonEvents(anyInt())).thenReturn(mockResult);

        when(mockTeamRepository.save(any())).thenReturn(team);

        //act
        Team actual = teamService.createTeam(teamName, 12345, true, 2, 1, "#ffffff", "medal");

        //assert
        assertEquals(actual.getTeamName(), team.getTeamName());
        assertEquals(actual.getHackathonEventID(), team.getHackathonEventID());
        assertEquals(actual.getTeamCode(), team.getTeamCode());
        assertEquals(actual.getTeamColorCode(), team.getTeamColorCode());
        assertEquals(actual.getTeamIconCode(), team.getTeamIconCode());
        assertEquals(actual.getTeamOwnerID(), team.getTeamOwnerID());
        assertEquals(actual.isOpen(), team.isOpen());
    }

    @Test
    public void generateTeamCode() {
        //arrange

        //act
        String code = teamService.generateTeamCode();
        //assert
        Assert.assertNotNull(code);
    }

    @Test
    public void getAllTeamsTest() {
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();
        Team fakeTeam2 = TeamMock.getTeamMock();
        fakeTeam2.setTeamID(2);
        List<Team> fakeTeams = new ArrayList<>(Arrays.asList(fakeTeam, fakeTeam2));
        when(mockTeamRepository.findAll()).thenReturn(fakeTeams);

        //act
        List<Team> actual = teamService.getAllTeams();

        //assert
        assertEquals(2, actual.size());
        verify(mockTeamRepository).findAll();
        verifyNoMoreInteractions(mockTeamRepository);
    }

    @Test
    public void getTeamMembersTest() {
        //arrange
        Participant member = new Participant();
        List<Participant> memberList = new ArrayList<>();
        memberList.add(member);
        Team team = TeamMock.getTeamMock();
        team.setTeamID(123);
        when(mockTeamRepository.findTeamByTeamID(123)).thenReturn(Optional.of(team));
        when(mockParticipantRepository.findParticipantsByTeamID(123)).thenReturn(memberList);

        //act
        List<Participant> actual = teamService.getTeamMembers(123);

        //assert
        assertEquals(actual, memberList);
        verify(mockTeamRepository).findTeamByTeamID(anyInt());
        verify(mockParticipantRepository).findParticipantsByTeamID(anyInt());
    }

    @Test
    public void getParticipantCountTest() {
        //arrange
        Team team = TeamMock.getTeamMock();
        when(mockTeamRepository.findTeamByTeamID(1)).thenReturn(Optional.of(team));

        Participant participant = new Participant();
        Participant participant2 = new Participant();
        participant.setTeamID(1);
        participant2.setTeamID(1);
        List<Participant> partRepoList = new ArrayList<>(Arrays.asList(participant, participant2));
        when(mockParticipantRepository.findParticipantsByTeamID(1)).thenReturn(partRepoList);

        //act
        int actual = teamService.getTeamMemberCount(1);

        //assert
        assertEquals(2, actual);
        verify(mockTeamRepository).findTeamByTeamID(1);
        verify(mockParticipantRepository).findParticipantsByTeamID(1);
        verifyNoMoreInteractions(mockTeamRepository);
        verifyNoMoreInteractions(mockParticipantRepository);
    }

    @Test
    public void getTeamByCodePass(){
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();
        when(mockTeamRepository.findTeamByTeamCode("123456")).thenReturn(Optional.of(fakeTeam));

        //act
        Optional<Team> actual = mockTeamRepository.findTeamByTeamCode("123456");

        //assert
        assertEquals(Optional.of(fakeTeam), actual);
    }

    @Test
    public void getTeamByTeamID(){
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();
        when(mockTeamRepository.findTeamByTeamID(1)).thenReturn(Optional.of(fakeTeam));

        //act
        Optional<Team> actual = teamService.getTeamByID(1);

        //assert
        assertEquals(Optional.of(fakeTeam), actual);
    }

    @Test
    public void getTeamByCodeFail(){
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();

        //act
        Optional<Team> actual = mockTeamRepository.findTeamByTeamCode("abcdef");

        //assert
        Assert.assertNotEquals(Optional.of(fakeTeam), actual);
    }

    @Test
    public void updateTeamFail(){
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();
        TeamDTO fakeTeamDTO = TeamDTOMock.getTeamDTO();

        //act
        Team actual = teamService.updateTeam("abcdef", fakeTeamDTO);

        //assert
        Assert.assertNull(actual);
    }

    @Test
    public void findAllByOpenIsTrueTest() {
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();
        List<Team> teamList = new ArrayList<>();
        teamList.add(fakeTeam);
        when(mockTeamRepository.findAllByOpenIsTrue()).thenReturn(teamList);

        //act
        List<Team> actual = teamService.findAllByOpenIsTrue();

        //assert
        assertEquals(actual.get(0), fakeTeam);
        verify(mockTeamRepository).findAllByOpenIsTrue();
    }

    @Test
    public void getSmallestTeamIDTest() {
        //arrange
        Team team1 = TeamMock.getTeamMock();
        team1.setMemberCount(1);
        team1.setTeamID(1);
        Team team2 = TeamMock.getTeamMock();
        team2.setMemberCount(2);
        team2.setTeamID(2);
        List<Team> teamList = new ArrayList<>();
        teamList.add(team1);
        teamList.add(team2);
        when(mockTeamRepository.findAll()).thenReturn(teamList);

        //act
        Integer actual = teamService.getSmallestTeamID();

        //assert
        assertEquals(team1.getTeamID(), actual);
        verify(mockTeamRepository).findAll();
    }

    @Test
    public void testGetByTeamName(){
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();
        when(mockTeamRepository.findTeamByTeamNameIgnoreCase("Team")).thenReturn(Optional.of(fakeTeam));

        //act
        boolean exists = teamService.getByTeamName("Team");

        //assert
        assertTrue(exists);
    }

    @Test
    public void testGetByTeamNameNoTeam(){
        //arrange
        when(mockTeamRepository.findTeamByTeamNameIgnoreCase("Team")).thenReturn(Optional.empty());

        //act
        boolean exists = teamService.getByTeamName("Team");

        //assert
        verify(mockTeamRepository).findTeamByTeamNameIgnoreCase("Team");
        assertFalse(exists);
    }

    @Test
    public void testGetByTeamNameThrowsException(){
        //arrange
        when(mockTeamRepository.findTeamByTeamNameIgnoreCase("Team")).thenThrow(new RuntimeException());

        //act
        boolean exists = teamService.getByTeamName("Team");

        //assert
        assertFalse(exists);
    }

    @Test
    public void testGetAllTeamsForUpcomingHackathons(){
        //arrange
        HackathonEvent hackathon = new HackathonEvent();
        hackathon.setHackathonEventID(0);

        Team fakeTeam1 = TeamMock.getTeamMock();
        fakeTeam1.setHackathonEventID(0);

        List<Team> teamList = new ArrayList<>();
        teamList.add(fakeTeam1);

        when(mockHackathonEventService.getUpcomingHackathonEvents(1)).thenReturn(List.of(hackathon));
        when(mockTeamRepository.findAllByHackathonEventID(0)).thenReturn(teamList);
        when(mockTeamRepository.findAllByHackathonEventID(0)).thenReturn(teamList);
        when(mockTeamRepository.findAllByHackathonEventID(0)).thenReturn(teamList);

        //act
        List<Team> actual = teamService.getAllTeamsForUpcomingHackathons(1);

        //assert
        assertEquals(teamList, actual);
        verify(mockTeamRepository).findAllByHackathonEventID(0);
    }
}
