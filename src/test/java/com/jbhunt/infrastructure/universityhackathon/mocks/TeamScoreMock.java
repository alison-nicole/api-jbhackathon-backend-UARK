package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.entity.TeamScore;

import java.util.ArrayList;
import java.util.List;

public class TeamScoreMock {

    public static TeamScore getTeamScore() {
        TeamScore teamScore = new TeamScore();
        teamScore.setScore(50);
        teamScore.setScoreID(1);
        teamScore.setJudgeID(4);
        teamScore.setTeamID(1);
        teamScore.setFeedback("Great Job");
        return teamScore;
    }

    public static List<TeamScore> getListTeamScores() {
        List<TeamScore> list = new ArrayList<>();
        TeamScore teamScore1 = getTeamScore();
        TeamScore teamScore2 = getTeamScore();
        teamScore2.setScore(100);
        teamScore2.setScoreID(2);
        teamScore2.setJudgeID(5);
        teamScore2.setFeedback("Awesome!");
        list.add(teamScore1);
        list.add(teamScore2);
        return list;
    }
}
