package com.jbhunt.infrastructure.universityhackathon.controllers;
import com.jbhunt.infrastructure.universityhackathon.data.dto.TeamDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import com.jbhunt.infrastructure.universityhackathon.mocks.TeamDTOMock;
import com.jbhunt.infrastructure.universityhackathon.mocks.TeamMock;
import com.jbhunt.infrastructure.universityhackathon.repository.TeamRepository;
import com.jbhunt.infrastructure.universityhackathon.services.TeamService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeamsControllerTest {
    @Mock
    private TeamService mockTeamService;
    @Mock
    private TeamRepository mockTeamRepository;
    @InjectMocks
    private TeamController teamController;

    @Test
    public void createTeamTest() throws Exception {
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

        when(mockTeamService.createTeam(teamName)).thenReturn(team);

        //act
        ResponseEntity<Team> result = teamController.createTeam(teamName);

        //assert
        Assert.assertEquals(team, result.getBody());
        verify(mockTeamService).createTeam(anyString());
    }


    @Test
    public void getAllTeamsTest() {
        //arrange
        Team fakeTeam1 = TeamMock.getTeamMock();
        Team fakeTeam2 = TeamMock.getTeamMock();
        fakeTeam2.setTeamID(2);
        List<Team> fakeTeams = new ArrayList<>(Arrays.asList(fakeTeam1, fakeTeam2));
        when(mockTeamService.getAllTeams()).thenReturn(fakeTeams);

        //act
        List<Team> actual = teamController.getAllTeams();

        //assert
        Assert.assertEquals(actual.size(), 2);
        Assert.assertEquals(fakeTeams, actual);
        verify(mockTeamService).getAllTeams();
    }

    @Test
    public void getNumberOfMembersTest() {
        //arrange
        when(mockTeamService.getTeamMemberCount(9)).thenReturn(5);

        //act
        int actual = teamController.getNumberOfMembers(9);

        //assert
        Assert.assertEquals(5, actual);
        verify(mockTeamService).getTeamMemberCount(anyInt());
    }

    @Test
    public void getTeamByCodeTest() {
        //arrange
        Team fakeTeam1 = TeamMock.getTeamMock();
        Team fakeTeam2 = TeamMock.getTeamMock();
        List<Team> fakeTeams1 = new ArrayList<>();
        fakeTeams1.add(fakeTeam1);
        fakeTeams1.add(fakeTeam2);
        when(mockTeamRepository.findTeamByTeamCode("123")).thenReturn(Optional.of(fakeTeam1));

        //act
        Optional<Team> actual = teamController.getTeamByCode("123");

        //assert
        Assert.assertEquals(Optional.of(fakeTeam1), actual);
        verify(mockTeamRepository).findTeamByTeamCode(anyString());
    }

    @Test
    public void updateTeamByCodePass(){
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();
        TeamDTO fakeTeamDTO = TeamDTOMock.getTeamDTO();
        when(mockTeamService.updateTeam("123456", fakeTeamDTO)).thenReturn(fakeTeam);

        //act
        ResponseEntity<Team> actual = teamController.updateTeamByCode(fakeTeamDTO, "123456");

        //assert
        Assert.assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        verify(mockTeamService).updateTeam(anyString(),any());
    }

    @Test
    public void getTeamByTeamIDTest() {
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();

        //act
        when(mockTeamService.getTeamByID(1)).thenReturn(Optional.of(fakeTeam));
        Optional<Team> actual = teamController.getTeamByTeamID(1);

        //assert
        Assert.assertEquals(Optional.of(fakeTeam), actual);
    }

    @Test
    public void getAllOpenTeamsTest() {
        //arrange
        Team fakeTeam = TeamMock.getTeamMock();
        List<Team> teamList = new ArrayList<>();
        teamList.add(fakeTeam);
        when(mockTeamService.findAllByOpenIsTrue()).thenReturn(teamList);

        //act
        List<Team> actual = teamController.getAllOpenTeams();

        //assert
        Assert.assertEquals(actual.get(0), fakeTeam);
        verify(mockTeamService).findAllByOpenIsTrue();
    }

    @Test
    public void testGetTeamByTeamName() {
        //arrange
        when(mockTeamService.getByTeamName("test team")).thenReturn(true);

        //act
        boolean exists = teamController.getTeamByTeamName("test team");

        //assert
        verify(mockTeamService).getByTeamName("test team");
        Assert.assertTrue(exists);
    }

    @Test
    public void testGetTeamByNameNoTeam(){
        //arrange
        when(mockTeamService.getByTeamName("test team")).thenReturn(false);

        //act
        boolean exists = teamController.getTeamByTeamName("test team");

        //assert
        verify(mockTeamService).getByTeamName("test team");
        Assert.assertFalse(exists);
    }
}
