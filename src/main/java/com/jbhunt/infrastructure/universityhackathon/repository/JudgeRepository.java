package com.jbhunt.infrastructure.universityhackathon.repository;

import com.jbhunt.infrastructure.universityhackathon.entity.Judge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JudgeRepository extends JpaRepository<Judge, Integer> {
    Optional<Judge> findJudgeByFirstNameAndLastName(String firstName, String lastName);
    Optional<Judge> findJudgeByEmail(String email);
}
