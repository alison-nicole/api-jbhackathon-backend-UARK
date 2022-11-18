package com.jbhunt.infrastructure.universityhackathon.repository;

import com.jbhunt.infrastructure.universityhackathon.entity.TeamScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamScoreRepository extends JpaRepository<TeamScore, Integer> {
    List<TeamScore> getTeamScoresByTeamID(Integer teamID);
}
