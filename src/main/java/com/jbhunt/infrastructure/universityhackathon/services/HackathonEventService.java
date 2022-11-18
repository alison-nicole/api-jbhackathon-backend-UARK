package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.data.dto.HackathonFormDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.repository.HackathonEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HackathonEventService {

    private final HackathonEventRepository hackathonEventRepository;

    public HackathonEvent createHackathonEvent(HackathonFormDTO hackathonInfo) {
        var hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventName(hackathonInfo.getHackathonEventName());
        hackathonEvent.setHackathonTopicDescription(hackathonInfo.getHackathonTopicDescription());
        hackathonEvent.setHackathonEventStartDate(hackathonInfo.getHackathonEventStartDate());
        hackathonEvent.setHackathonEventEndDate(hackathonInfo.getHackathonEventEndDate());

        Calendar registrationEndCalendar = Calendar.getInstance();
        registrationEndCalendar.setTime(hackathonInfo.getHackathonEventEarlyRegistrationEndDate());
        hackathonEvent.setHackathonEventEarlyRegistrationEndDate(registrationEndCalendar);

        Optional<HackathonEvent> previousHackathon = hackathonEventRepository.findHackathonEventByHackathonEventName(hackathonEvent.getHackathonEventName());
        // Updates if hackathon event name already exists
        if(previousHackathon.isPresent()) {
            int existingHackathonID = previousHackathon.get().getHackathonEventID();
            hackathonEvent.setHackathonEventID(existingHackathonID);
        }

        return hackathonEventRepository.save(hackathonEvent);
    }

    public List<HackathonEvent> getCurrentHackathon() {
        Optional<List<HackathonEvent>> result = hackathonEventRepository.findHackathonEventsByHackathonEventEndDateAfterOrderByHackathonEventStartDate(new Date(System.currentTimeMillis()));
        if(result.isEmpty()) {
            return new ArrayList<>();
        }
        return result.get();
    }

    public List<HackathonEvent> getUpcomingHackathonEvents(int withinDays) {
        Calendar calendar = Calendar.getInstance();
        Optional<List<HackathonEvent>> result = hackathonEventRepository.findHackathonEventsByHackathonEventStartDateAfterOrderByHackathonEventStartDate(new Date(calendar.getTimeInMillis()));
        if(result.isEmpty()) {
            return new ArrayList<>();
        }

        List<HackathonEvent> hackathonEvents = result.get();
        List<HackathonEvent> withinRange = new ArrayList<>();

        for(HackathonEvent hackathonEvent : hackathonEvents) {
            Date hackathonStartDate = hackathonEvent.getHackathonEventStartDate();
            Calendar hackathonStartCalendar = Calendar.getInstance();
            hackathonStartCalendar.setTime(hackathonStartDate);

            if(hackathonStartCalendar.getTimeInMillis() - calendar.getTimeInMillis() <= (long)withinDays * 24 * 60 * 60 * 1000) {
                withinRange.add(hackathonEvent);
            } else {
                break;
            }
        }

        return withinRange;
    }
}
