package com.jbhunt.infrastructure.universityhackathon.repository;

import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository  extends JpaRepository<Participant, Integer> {
    //this method can be used to return the members of the same team
    List<Participant> findParticipantsByTeamID(int teamID);
    List<Participant> findParticipantsByHackathonEventID(int hackathonEventID);
    Optional<Participant> findParticipantBySchoolEmailAddress(String email);
}
