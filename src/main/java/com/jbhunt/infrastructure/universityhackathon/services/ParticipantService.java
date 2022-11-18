package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public boolean checkEmail(String email) {
        try {
            Optional<Participant> participant = participantRepository.findParticipantBySchoolEmailAddress(email);
            return participant.isPresent();
        }
        catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public List<Participant> getParticipantsByTeamID(int teamID) {
        try {
            return participantRepository.findParticipantsByTeamID(teamID);
        }
        catch(Exception e){
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }
}
