package com.jbhunt.infrastructure.universityhackathon.repository;

import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HackathonEventRepository extends JpaRepository<HackathonEvent, Integer> {
    Optional<HackathonEvent> findHackathonEventByHackathonEventName(String name);
    Optional<List<HackathonEvent>> findHackathonEventsByHackathonEventEndDateAfterOrderByHackathonEventStartDate(Date endDate);
    Optional<List<HackathonEvent>> findHackathonEventsByHackathonEventStartDateAfterOrderByHackathonEventStartDate(Date startDate);
}
