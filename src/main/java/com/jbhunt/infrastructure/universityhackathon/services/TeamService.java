package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.data.dto.TeamDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import com.jbhunt.infrastructure.universityhackathon.repository.ParticipantRepository;
import com.jbhunt.infrastructure.universityhackathon.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final ParticipantRepository participantRepository;

    private final HackathonEventService hackathonEventService;

    public Team createTeam(String teamName){
        var team = Team.builder()
                .teamName(teamName)
                .build();

        //get hackathonEventID
        int hackathonEventID = -1;
       List<HackathonEvent> current = hackathonEventService.getCurrentHackathon();
        if(!current.isEmpty()) {
            hackathonEventID = current.get(0).getHackathonEventID();

            // Basic info
            team.setTeamName(teamName);
            team.setHackathonEventID(hackathonEventID);
            team.setTeamCode(generateTeamCode());

            return teamRepository.save(team);
        } else {
            log.error("HACKATHON DOESN'T EXIST");
            throw new NullPointerException("NO HACKATHON EVENT DATA");
        }
    }

    public Team createTeam(String teamName, Integer teamOwnerID, Boolean open, Integer memberCount, Integer graduateCount, String teamColorCode, String teamIconCode){
        var team = Team.builder()
                .teamName(teamName)
                .teamOwnerID(teamOwnerID)
                .open(open)
                .memberCount(memberCount)
                .graduateCount(graduateCount)
                .teamColorCode(teamColorCode)
                .teamIconCode(teamIconCode)
                .build();

        //get hackathonEventID
        int hackathonEventID = -1;
        List<HackathonEvent> current = hackathonEventService.getCurrentHackathon();
        if(!current.isEmpty()) {
            hackathonEventID = current.get(0).getHackathonEventID();

            // Basic info
            team.setHackathonEventID(hackathonEventID);
            team.setTeamCode(generateTeamCode());


            return teamRepository.save(team);
        } else {
            log.error("HACKATHON DOESN'T EXIST");
            throw new NullPointerException("NO HACKATHON EVENT DATA");
        }
    }

    public String generateTeamCode() {
        var teamCode = "";
        var generateTeamCode = true;

        while(generateTeamCode) {
            var randNum = new Random().nextInt(999999);
            teamCode = String.valueOf(randNum);
            if(teamRepository.findTeamByTeamCode(teamCode).isEmpty())
                generateTeamCode = false;
        }
        return teamCode;
    }
    /**
     * method
     * Generate Teams Strength
     * @param: a list of objects of type pariticipant containing the members in the team
     * @return: a numeric value of type integer or double that represents the strength of the team
     * */
    public Integer generateTeamStrength(List<Participant> participantsOnTeam){
        var teamStrength = 0;
        var membersStrengthSum = 0;
        //get the total sum of the strength of all participants in the team
        for (Participant participant : participantsOnTeam){
            membersStrengthSum += participant.getScore();
        }

        teamStrength = membersStrengthSum/participantsOnTeam.size();

        return teamStrength;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Participant> getTeamMembers(int teamID) {
        List<Participant> participantsOnTeam = new ArrayList<>();
        Optional<Team> team = teamRepository.findTeamByTeamID(teamID);
        if (team.isPresent()) {
            participantsOnTeam = participantRepository.findParticipantsByTeamID(team.get().getTeamID());
        }
        return participantsOnTeam;
    }

    public int getTeamMemberCount(int teamID) {
        List<Participant> participantsOnTeam = new ArrayList<>();
        Optional<Team> team = teamRepository.findTeamByTeamID(teamID);
        if(team.isPresent()) {
            participantsOnTeam = participantRepository.findParticipantsByTeamID(team.get().getTeamID());
        }
        return participantsOnTeam.size();
    }

    public Optional<Team> getTeamByID(Integer teamID) {
        return teamRepository.findTeamByTeamID(teamID);
    }

    public List<Team> findAllByOpenIsTrue() {
        return teamRepository.findAllByOpenIsTrue();
    }

    public int getSmallestTeamID() {
        ArrayList<Integer> teamListIDs = new ArrayList<>();
        List<Team> teamList = teamRepository.findAll();
        teamList.forEach(elem -> teamListIDs.add(elem.getTeamID()));
        int smallestID = teamListIDs.get(0);
        for(int i: teamListIDs) {
            if(getTeamMemberCount(i) < getTeamMemberCount(smallestID)) {
                smallestID = i;
            }
        }
        return smallestID;
    }

    public Team updateTeam(String teamCode, TeamDTO currentTeam) {
        Optional<Team> teamOptional = teamRepository.findTeamByTeamCode(teamCode);

        if(teamOptional.isPresent()){
            var team = teamOptional.get();

            return teamRepository.save(team);
        }

        return null;
    }

    public boolean getByTeamName(String teamName) {
        try {
            Optional<Team> team = teamRepository.findTeamByTeamNameIgnoreCase(teamName);
            return team.isPresent();
        }
        catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public List<Team> getAllTeamsByHackathonEventID(int hackathonEventID) {
        return teamRepository.findAllByHackathonEventID(hackathonEventID);
    }

    public List<Team> getAllTeamsForUpcomingHackathons(int withinDays) {
        List<HackathonEvent> current = hackathonEventService.getUpcomingHackathonEvents(withinDays);
        if (current.isEmpty()) {
            return new ArrayList<>();
        }

        List<Team> foundTeams = new ArrayList<>();
        for (HackathonEvent hackathonEvent : current) {
            foundTeams.addAll(getAllTeamsByHackathonEventID(hackathonEvent.getHackathonEventID()));
        }

        return foundTeams;
    }
}
