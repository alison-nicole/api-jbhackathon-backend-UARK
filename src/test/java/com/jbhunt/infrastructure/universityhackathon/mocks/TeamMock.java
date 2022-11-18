package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.entity.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamMock {
    public static Team getTeamMock() {
        Team team1 = new Team();
        team1.setTeamName("TEST 1");
        team1.setTeamCode("123456");
        team1.setTeamColorCode("#ffffff");
        team1.setTeamIconCode("medal");
        team1.setTeamID(1);
        return team1;
    }

    public static Team getTeamWithScoreMock() {
        Team team1 = new Team();
        team1.setTeamName("TEST 1");
        team1.setTeamCode("123456");
        team1.setTeamColorCode("#ffffff");
        team1.setTeamIconCode("medal");
        team1.setTeamID(1);
        team1.setScore(700);
        return team1;
    }

    public static List<Team> getTeamList() {
        List<Team> teamList = new ArrayList<>();

        Team team1 = new Team();
        team1.setTeamName("TEST 1");
        team1.setTeamCode("123456");
        team1.setTeamColorCode("#ffffff");
        team1.setTeamIconCode("medal");
        team1.setTeamID(1);

        Team team2 = new Team();
        team2.setTeamName("TEST 2");
        team2.setTeamCode("111111");
        team2.setTeamColorCode("#000000");
        team2.setTeamIconCode("motorcycle");
        team2.setTeamID(2);

        teamList.add(team1);
        teamList.add(team2);
        return teamList;
    }

    public static List<Team> getTeamList(int numberTeams) {
        List<Team> teamList = new ArrayList<>();
        for(int i = 0; i < numberTeams; i++) {
            var team = getTeamMock();
            team.setTeamID(i);
            team.setTeamName("Team " + i);
            team.setMemberCount((i + 1) % 6);
            team.setGraduateCount((i+1) % 2);
            team.setOpen(true);
            teamList.add(team);
        }
        return teamList;
    }

    public static List<Team> getTeamListWithScoredTeam() {
        List<Team> teamList = new ArrayList<>();
        Team team1 = new Team();
        team1.setTeamName("TEST 1");
        team1.setTeamCode("123456");
        team1.setTeamColorCode("#ffffff");
        team1.setTeamIconCode("medal");
        team1.setTeamID(1);
        team1.setScore(700);

        Team team2 = new Team();
        team2.setTeamName("TEST 2");
        team2.setTeamCode("111111");
        team2.setTeamColorCode("#000000");
        team2.setTeamIconCode("motorcycle");
        team2.setTeamID(2);

        teamList.add(team1);
        teamList.add(team2);
        return teamList;
    }
}
