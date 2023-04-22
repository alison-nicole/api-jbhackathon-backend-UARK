package com.jbhunt.infrastructure.universityhackathon.repository;

import com.jbhunt.infrastructure.universityhackathon.entity.Prizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PrizesRepository extends JpaRepository<Prizes, Integer> {

    boolean existsPrizesByPrizeName(String prizeName);

    Optional<Prizes> findPrizeByPrizeName(String prizeName);
    Optional<Prizes> findPrizeByPrizeNameIgnoreCase(String prizeName);

}