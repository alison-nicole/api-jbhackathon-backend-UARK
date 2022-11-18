package com.jbhunt.infrastructure.universityhackathon.repository;

import com.jbhunt.infrastructure.universityhackathon.entity.ParticipantSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantSchoolRepository extends JpaRepository<ParticipantSchool, String> {
    ParticipantSchool findParticipantSchoolByParticipantSchoolName(String participantSchoolName);
}
