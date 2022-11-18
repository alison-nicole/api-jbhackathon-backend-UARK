package com.jbhunt.infrastructure.universityhackathon.repository;

import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    boolean existsTeamByTeamName(String teamName);
    Optional<Team> findTeamByTeamID(Integer teamID);
    Optional<Team> findTeamByTeamNameIgnoreCase(String teamName);
    Optional<Team> findTeamByTeamName(String teamName);
    Optional<Team> findTeamByTeamCode(String teamName);
    List<Team> findAllByOpenIsTrue();
    List<Team> findAllByHackathonEventID(Integer hackathonEventID);
}
