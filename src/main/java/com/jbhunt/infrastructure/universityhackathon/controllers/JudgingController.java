package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgingDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import com.jbhunt.infrastructure.universityhackathon.entity.TeamScore;
import com.jbhunt.infrastructure.universityhackathon.services.EmailService;
import com.jbhunt.infrastructure.universityhackathon.services.JudgingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.jbhunt.infrastructure.universityhackathon.constants.HackathonConstants.JUDGING;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = JUDGING)
public class JudgingController {

     private final JudgingService judgingService;
     private final EmailService emailService;

     @PostMapping("/submit")
     public @ResponseBody ResponseEntity<TeamScore> saveParticipant(@Valid @RequestBody JudgingDTO scoreRequest) {
          return new ResponseEntity<>(judgingService.submitScore(scoreRequest), HttpStatus.CREATED);
     }

     @GetMapping("/all-scored-teams")
     public @ResponseBody List<Team> getAllScoredTeams() {
          return judgingService.getAllScoredTeams();
     }

     @PostMapping("/send-judge-form")
     public @ResponseBody ResponseEntity<String> sendJudgeForm(@Valid @RequestBody String base64EncodedImage) {
         judgingService.sendFeedbackEmail("joseph.a.taylor@jbhunt.com", "Joseph's Team", base64EncodedImage);
         return new ResponseEntity<>("Email sent", HttpStatus.OK);
     }

     @PostMapping("/send-judge-form/{teamID}")
     public @ResponseBody ResponseEntity<String> sendJudgeForm(@PathVariable int teamID, @RequestBody String base64EncodedImage) {
          try {
               judgingService.sendTeamFeedback(teamID, base64EncodedImage);
          }
          catch (Exception e) {
               return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
          }

          return new ResponseEntity<>("Email sent", HttpStatus.OK);
     }
}

