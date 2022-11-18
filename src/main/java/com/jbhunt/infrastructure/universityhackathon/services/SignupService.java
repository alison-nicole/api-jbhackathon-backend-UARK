package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.constants.EmailTemplateConstants;
import com.jbhunt.infrastructure.universityhackathon.data.dto.SignUpFormDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import com.jbhunt.infrastructure.universityhackathon.repository.ParticipantRepository;
import com.jbhunt.infrastructure.universityhackathon.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {

    private final TeamRepository teamRepository;

    private final ParticipantRepository participantRepository;

    private final HackathonEventService hackathonEventService;

    private final TeamService teamService;

    private final EmailService emailService;

    public Participant saveParticipant(SignUpFormDTO participantRequest) {

        trim(participantRequest);
        validateParticipantRequest(participantRequest);

        var participant = createParticipantFromForm(participantRequest);
        participant = participantRepository.save(participant);
        if (!participantRequest.getTeamName().isEmpty()) {
            Optional<Team> teamOptional = teamRepository.findTeamByTeamName(participantRequest.getTeamName());
            if(teamOptional.isPresent()) {
                saveParticipantToTeam(participant, teamOptional.get());
            } else {
                Team team = teamService.createTeam(participantRequest.getTeamName(),
                       participant.getParticipantID(), participantRequest.getTeamOpen(),
                        0, 0, participantRequest.getTeamColorCode(), participantRequest.getTeamIconCode());

                saveParticipantToTeam(participant, team);
            }
        }
        participant = participantRepository.save(participant);

        sendRegistrationConfirmationEmail(participant);

        return participant;
    }

    public void manageParticipantsWithoutTeam(int hackathonEventID) {
        var listParticipants = participantRepository.findParticipantsByHackathonEventID(hackathonEventID);
        listParticipants = listParticipants.stream().filter(participant -> participant.getTeamID() == null).collect(Collectors.toList());

        log.info("Distributing {} participants {hackathonEventId = {}}", listParticipants.size(), hackathonEventID);
        if(listParticipants.isEmpty()) return;

        var openTeams = new ArrayList<>(teamRepository.findAllByHackathonEventID(hackathonEventID));
        openTeams = (ArrayList<Team>) openTeams.stream().filter(Team::isOpen).collect(Collectors.toList());

        var totalTakenSpotsOnOpenTeams = 0;
        for(Team team: openTeams) {
            totalTakenSpotsOnOpenTeams += team.getMemberCount();
        }

        int openSpots = (openTeams.size() * 6) - totalTakenSpotsOnOpenTeams;
        createNeededTeams((int) Math.ceil(((double) listParticipants.size() - openSpots) / 6), openTeams);
        boolean done = false;
        while(!done) {
            done = distribute(listParticipants, openTeams);
        }
    }

    Participant createParticipantFromForm(SignUpFormDTO participantRequest) {
        var participant = new Participant();
        participant.setFirstName(participantRequest.getFirstName());
        participant.setLastName(participantRequest.getLastName());
        participant.setSchoolEmailAddress(participantRequest.getSchoolEmailAddress());
        participant.setGraduate(participantRequest.getIsGradStudent());
        //remove these values in future
        participant.setEffectiveTimestamp(null);
        participant.setExpirationTimestamp(null);

        participant.setAccommodations(participantRequest.getAccommodations());
        participant.setHackathonEventID(hackathonEventService.getCurrentHackathon().get(0).getHackathonEventID());
        return participant;
    }

    void saveParticipantToTeam(Participant participant, Team team) {
        participant.setTeamID(team.getTeamID());
        team.setMemberCount(team.getMemberCount() + 1);
        team.setGraduateCount(team.getGraduateCount() + (Boolean.TRUE.equals(participant.getGraduate()) ? 1 : 0));
        if (team.getMemberCount() == 1) team.setTeamOwnerID(participant.getParticipantID());
        else if (team.getMemberCount() == 6) team.setOpen(false);
        teamRepository.save(team);
    }


    List<Team> createNeededTeams(int numNewTeamsNeeded, List<Team> teamList) {
        String[] animalNames = {"Dolphins", "Rats", "Whales", "Elephants", "Walruses", "Cats", "Dogs", "Lions", "Tigers",
                "Bears", "Sharks", "Otters", "Impalas", "Pigs", "Gerbils", "Alligators", "Crocodiles", "Barracudas",
                "Bats", "Beavers", "Boars", "Capybaras", "Chickens", "Eagles", "Flamingos", "Giraffes", "Gorillas", "Horses", "Llamas"};

        for(int i = 0; i < numNewTeamsNeeded; i++) {
            String teamName = animalNames[(i % animalNames.length)];
            int number = 10;
            int iteration = 0;
            while(teamRepository.findTeamByTeamName(teamName).isPresent()) {
                iteration++;
                var newName = new StringBuilder();
                newName.append(teamName);
                newName.append(number * iteration);
                teamName = newName.toString();
            }
            teamList.add(teamService.createTeam(teamName, -1, true, 0, 0, "#606060", "assets/svg/team-icons/mouse.svg"));
        }
        return teamList;
    }

    boolean distribute(List<Participant> listParticipants, List<Team> listTeams) {
        var participant = listParticipants.get(0);
        var smallestTeam = listTeams.stream().min(Comparator.comparing(Team::getMemberCount));
        smallestTeam.ifPresent(team -> {
            saveParticipantToTeam(participant, team);
            if (team.getMemberCount() == 6) listTeams.remove(team);
        });
        participantRepository.save(participant);
        listParticipants.remove(0);
        return (listParticipants.isEmpty());
    }

    void sendRegistrationConfirmationEmail(Participant participant) {
        if(participant.getTeamID() != null) {
            try {
                teamRepository.findTeamByTeamID(participant.getTeamID()).ifPresent( team -> {
                    var templateData = new HashMap<String, String>();
                    templateData.put(EmailTemplateConstants.FIRST_NAME, participant.getFirstName());
                    templateData.put(EmailTemplateConstants.TEAM_CODE, team.getTeamCode());
                    emailService.sendEmail(participant.getSchoolEmailAddress(), EmailTemplateConstants.REGISTRATION_CONFIRM_TEAMCODE, templateData);
                });
            } catch (Exception e) {
                log.error("Error sending email to {}", participant.getSchoolEmailAddress());
            }
        }
        else{
            try {
                var templateData = new HashMap<String, String>();
                templateData.put(EmailTemplateConstants.FIRST_NAME, participant.getFirstName());
                emailService.sendEmail(participant.getSchoolEmailAddress(), EmailTemplateConstants.REGISTRATION_CONFIRM_NO_TEAMCODE, templateData);
            } catch (Exception e) {
                log.error("Error sending email to {}", participant.getSchoolEmailAddress());
            }
        }
    }

    void validateParticipantRequest(SignUpFormDTO participantRequest){

        //If team name is null or empty, then we'll create a team for this participant
        //Otherwise, the team name must fit the requirements
        if(participantRequest.getTeamName() != null && !participantRequest.getTeamName().isEmpty() &&
                isLengthOutOfBounds(participantRequest.getTeamName(), 6, 25)){
            throw new IllegalArgumentException("Team name must be between 6 and 25 characters");
        }

        if(isLengthOutOfBounds(participantRequest.getFirstName(), 1, 37)){
            throw new IllegalArgumentException("First name must be between 1 and 37 characters");
        }

        if(isLengthOutOfBounds(participantRequest.getLastName(), 1, 37)){
            throw new IllegalArgumentException("Last name must be between 1 and 37 characters");
        }

        if(isLengthOutOfBounds(participantRequest.getSchoolEmailAddress(), 1, 50)){
            throw new IllegalArgumentException("Email must be between 1 and 50 characters");
        }

        if(isLengthOutOfBounds(participantRequest.getSchoolEmailAddress(), 1, 50) || isInvalidEmailAddress(participantRequest.getSchoolEmailAddress())){
            throw new IllegalArgumentException("Email must be within 1-50 characters and must be in a valid format");
        }

        if(participantRequest.getAccommodations() != null && isLengthOutOfBounds(participantRequest.getAccommodations(), 0, 1000)){
            throw new IllegalArgumentException("Accommodations must be between 0 and 1000 characters");
        }
    }

    boolean isInvalidEmailAddress(String email){
        boolean valid = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            valid = false;
        }

        return !valid;
    }

    boolean isLengthOutOfBounds(String str, int minLength, int maxLength){
        int length = countNonWhitespaceCharacters(str);
        return length < minLength || length > maxLength;
    }

    int countNonWhitespaceCharacters(String str) {
        return str.replaceAll("\\s", "").length();
    }

    void trim(SignUpFormDTO participantRequest) {
        participantRequest.setTeamName(participantRequest.getTeamName().trim());
        participantRequest.setFirstName(participantRequest.getFirstName().trim());
        participantRequest.setLastName(participantRequest.getLastName().trim());
    }
}

