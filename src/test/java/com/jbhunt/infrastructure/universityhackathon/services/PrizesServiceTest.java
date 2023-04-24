package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.entity.Prizes;
import com.jbhunt.infrastructure.universityhackathon.repository.PrizesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PrizesServiceTest {
    @Mock
    PrizesRepository mockPrizesrepository;
    @Mock
    private HackathonEventService mockHackathonEventService;
    @InjectMocks
    PrizesService prizesService;

    @Test
    public void createPrizeTest(){
        //ARRANGE
        String prizeName = "test prize";
        Prizes prize = Prizes.builder()
                .prizeName(prizeName)
                .build();
        int hackathonEventID = 1;
        prize.setPrizeName(prizeName);
        prize.setHackathonEventID(hackathonEventID);

        //mock hackathon event
        Date oldDate = new Date(System.currentTimeMillis());
        Date newDate = new Date(System.currentTimeMillis()+2000);
        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("Test");
        hackathonEvent.setHackathonEventStartDate(oldDate);
        hackathonEvent.setHackathonEventEndDate(newDate);

        List<HackathonEvent> mockResult = new ArrayList<>();
        mockResult.add(hackathonEvent);

       when(mockHackathonEventService.getCurrentHackathon()).thenReturn(mockResult);
        when(mockHackathonEventService.getUpcomingHackathonEvents(anyInt())).thenReturn(mockResult);
        when(mockPrizesrepository.save(any())).thenReturn(prize);

        //ACT
        Prizes actual = prizesService.createPrize(prizeName);

        //ASSERT
        assertEquals(actual.getPrizeName(), prize.getPrizeName());
        assertEquals(actual.getHackathonEventID(),prize.getHackathonEventID());

    }
}
