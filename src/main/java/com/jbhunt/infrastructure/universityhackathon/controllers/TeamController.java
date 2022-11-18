package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.TeamDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import com.jbhunt.infrastructure.universityhackathon.repository.TeamRepository;
import com.jbhunt.infrastructure.universityhackathon.services.TeamService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.jbhunt.infrastructure.universityhackathon.constants.HackathonConstants.TEAM;

@CrossOrigin
@RestController
@RequestMapping(value = TEAM)
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    private final TeamRepository teamRepository;

    @ResponseBody
    @PostMapping("/create/{teamName}")
    public ResponseEntity<Team> createTeam(@PathVariable(name = "teamName") String teamName) {
        if (teamName.length() < 6 || teamName.length() > 25) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(teamService.createTeam(teamName), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public @ResponseBody List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/allForUpcomingHackathons/{days}")
    public @ResponseBody List<Team> getAllTeamsForUpcomingHackathons(@PathVariable(name = "days") int days) {
        return teamService.getAllTeamsForUpcomingHackathons(days);
    }

    @GetMapping("/members/{teamID}")
    public @ResponseBody List<Participant> getTeamMembers(@PathVariable(name="teamID") int teamID) {
        return teamService.getTeamMembers(teamID);
    }

    @GetMapping("/memberAmount/{teamID}")
    public @ResponseBody int getNumberOfMembers(@PathVariable(name="teamID") int teamID) {
        return teamService.getTeamMemberCount(teamID);
    }

    @GetMapping("/{teamCode}")
    public @ResponseBody
    Optional<Team> getTeamByCode(@PathVariable(name="teamCode") String teamCode){
        return teamRepository.findTeamByTeamCode(teamCode);
    }

    @PutMapping("/{teamCode}")
    public @ResponseBody
    ResponseEntity<Team> updateTeamByCode(@Valid @RequestBody TeamDTO currentTeam, @PathVariable(name="teamCode") String teamCode){
        return new ResponseEntity<>(teamService.updateTeam(teamCode, currentTeam), HttpStatus.OK);
    }

    @GetMapping("/getTeam/{teamID}")
    public @ResponseBody
    Optional<Team> getTeamByTeamID(@PathVariable(name="teamID") Integer teamID) {
        return teamService.getTeamByID(teamID);
    }

    @GetMapping("/getTeamName/{teamName}")
    public @ResponseBody
    boolean getTeamByTeamName(@PathVariable(name="teamName") String teamName) {
        return teamService.getByTeamName(teamName);
    }

    @GetMapping("/all/open")
    public @ResponseBody
    List<Team> getAllOpenTeams() {
        return teamService.findAllByOpenIsTrue();
    }
}
