package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.constants.EmailTemplateConstants;
import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgingDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.*;
import com.jbhunt.infrastructure.universityhackathon.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JudgingService {

    private final TeamRepository teamRepository;

    private final JudgeRepository judgeRepository;

    private final TeamScoreRepository teamScoreRepository;

    private final ParticipantService participantService;

    private final TeamService teamService;

    private final EmailService emailService;

    public TeamScore submitScore(JudgingDTO scoreRequest) throws IllegalArgumentException {
        Optional<Judge> judge = judgeRepository.findJudgeByFirstNameAndLastName(scoreRequest.getJudgeFirstName(), scoreRequest.getJudgeLastName());

        if(judge.isEmpty()) {
            throw new IllegalArgumentException(
                    "No judge was found with first name " + scoreRequest.getJudgeFirstName() + " and last name " + scoreRequest.getJudgeLastName() + ".");
        }

        Optional<Team> team = teamRepository.findTeamByTeamName(scoreRequest.getTeamName());
        if(team.isEmpty()) {
            throw new IllegalArgumentException(
                    "No team was found with name " + scoreRequest.getTeamName() + ".");
        }
        TeamScore teamScore = TeamScore.builder()
                .teamID(team.get().getTeamID())
                .judgeID(judge.get().getJudgeID())
                .score(scoreRequest.getTeamScore())
                .feedback(scoreRequest.getFeedback())
                .build();

        TeamScore savedTeamScore = teamScoreRepository.save(teamScore);

        if(isFinishedWithJudging(scoreRequest.getTeamName()))
            calculateFinalScore(scoreRequest.getTeamName());

        return savedTeamScore;
    }

    public boolean isFinishedWithJudging(String teamName) {
        Optional<Team> team = teamRepository.findTeamByTeamName(teamName);
        List<Judge> finishedJudges = new ArrayList<>();
        List<Judge> allJudges = judgeRepository.findAll();
        team.ifPresent(value -> teamScoreRepository.getTeamScoresByTeamID(value.getTeamID()).forEach( teamScore -> allJudges.forEach(judge -> {
            if (teamScore.getJudgeID().equals(judge.getJudgeID()))
                finishedJudges.add(judge);
        })));
        return finishedJudges.containsAll(allJudges);
    }

    public Team calculateFinalScore(String teamName) {
        Optional<Team> team = teamRepository.findTeamByTeamName(teamName);
        int average = 0;
        int scoreCount = 0;
        if(team.isPresent()) {
            for(TeamScore teamScore : teamScoreRepository.getTeamScoresByTeamID(team.get().getTeamID())) {
                scoreCount++;
                average += teamScore.getScore();
            }
            if(scoreCount != 0)
                team.get().setScore(average / scoreCount);

            return teamRepository.save(team.get());
        }
        return null;
    }

    public List<Team> getAllScoredTeams() {
        return teamRepository.findAll().stream().filter(team -> team.getScore() != null).collect(Collectors.toList());
    }

    public void sendFeedbackEmail(String email, String teamName, String base64EncodedFeedbackImage) {
        HashMap<String, String> templateData = new HashMap<>();
        templateData.put(EmailTemplateConstants.TEAM_NAME, teamName);
        templateData.put(EmailTemplateConstants.IMAGE_SOURCE, base64EncodedFeedbackImage);

        emailService.sendEmail(email, EmailTemplateConstants.JUDGE_FEEDBACK_FORM, templateData);
    }

    public void sendTeamFeedback(Integer teamId, String base64EncodedFeedbackImage){
        if(!verifyEncodedStringIsImage(base64EncodedFeedbackImage)) {
            throw new IllegalArgumentException("Encoded string is not a valid image");
        }

        Team team = teamRepository.findById(teamId).orElse(null);
        if(team == null) {
            throw new IllegalArgumentException("Team with id " + teamId + " does not exist");
        }

        List<Participant> participants = participantService.getParticipantsByTeamID(teamId);
        if(participants == null || participants.isEmpty()) {
            return;
        }

        for(Participant participant : participants) {
            sendFeedbackEmail(participant.getSchoolEmailAddress(), team.getTeamName(), base64EncodedFeedbackImage);
        }
    }

    public boolean verifyEncodedStringIsImage(String base64EncodedImage){
        String[] imageParts = base64EncodedImage.split(",");

        if(imageParts.length != 2) return false;
        try {

            String imageString = imageParts[1];
            byte[] imageBytes = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

            ImageIO.read(byteArrayInputStream);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
