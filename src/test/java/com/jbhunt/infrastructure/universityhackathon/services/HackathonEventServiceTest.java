package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.data.dto.HackathonFormDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.repository.HackathonEventRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HackathonEventServiceTest {
    @Mock
    private HackathonEventRepository mockHackathonEventRepository;
    @InjectMocks
    private HackathonEventService hackathonEventService;

    @Test
    public void createHackathonEventTest() {
        Date oldDate = new Date(System.currentTimeMillis());
        Date newDate = new Date(System.currentTimeMillis()+2000);

        HackathonFormDTO hackathonFormDTO = new HackathonFormDTO();
        hackathonFormDTO.setHackathonEventName("Fall 2020");
        hackathonFormDTO.setHackathonTopicDescription("Efficiency");
        hackathonFormDTO.setHackathonEventEndDate(newDate);
        hackathonFormDTO.setHackathonEventStartDate(newDate);
        hackathonFormDTO.setHackathonEventEarlyRegistrationEndDate(newDate);

        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("Test");
        hackathonEvent.setHackathonEventStartDate(oldDate);
        hackathonEvent.setHackathonEventEndDate(oldDate);

        when(mockHackathonEventRepository.findHackathonEventByHackathonEventName(anyString())).thenReturn(Optional.of(hackathonEvent));
        when(mockHackathonEventRepository.save(any())).thenReturn(hackathonEvent);

        HackathonEvent actual = hackathonEventService.createHackathonEvent(hackathonFormDTO);
        Assert.assertEquals(actual.getHackathonEventName(), hackathonEvent.getHackathonEventName());
        Assert.assertEquals(actual.getHackathonEventID(), hackathonEvent.getHackathonEventID());
        Assert.assertEquals(actual.getHackathonEventEndDate(), hackathonEvent.getHackathonEventEndDate());
        verify(mockHackathonEventRepository).findHackathonEventByHackathonEventName(anyString());
        verify(mockHackathonEventRepository).save(any());
        verifyNoMoreInteractions(mockHackathonEventRepository);
    }

    @Test
    public void getCurrentHackathonTest() {
        Date selectedEndDate = new Date(System.currentTimeMillis()+4000);

        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("Test");
        hackathonEvent.setHackathonEventStartDate(selectedEndDate);
        hackathonEvent.setHackathonEventEndDate(selectedEndDate);

        List<HackathonEvent> mockResult = new ArrayList<>();
        mockResult.add(hackathonEvent);
        when(mockHackathonEventRepository.findHackathonEventsByHackathonEventEndDateAfterOrderByHackathonEventStartDate(any())).thenReturn(Optional.of(mockResult));
        List<HackathonEvent> actual = hackathonEventService.getCurrentHackathon();
        Assert.assertEquals(actual.get(0).getHackathonEventName(), hackathonEvent.getHackathonEventName());
        Assert.assertEquals(actual.get(0).getHackathonEventID(), hackathonEvent.getHackathonEventID());
        Assert.assertEquals(actual.get(0).getHackathonEventEndDate(), hackathonEvent.getHackathonEventEndDate());
        verify(mockHackathonEventRepository).findHackathonEventsByHackathonEventEndDateAfterOrderByHackathonEventStartDate(any());
        verifyNoMoreInteractions(mockHackathonEventRepository);
    }

    @Test
    public void getUpcomingHackathonEventsTest(){
        List<HackathonEvent> mockResult = List.of(
                createHackathonEvent(0, getCalendarForFuture(7)),
                createHackathonEvent(1, getCalendarForFuture(14)),
                createHackathonEvent(2, getCalendarForFuture(60)),
                createHackathonEvent(3, getCalendarForFuture(120)),
                createHackathonEvent(4, getCalendarForFuture(150))
        );

        when(mockHackathonEventRepository.findHackathonEventsByHackathonEventStartDateAfterOrderByHackathonEventStartDate(any())).thenReturn(Optional.of(mockResult));

        List<HackathonEvent> actual = hackathonEventService.getUpcomingHackathonEvents(90);

        Assert.assertEquals(3, actual.size());
        Assert.assertEquals(0, (int) actual.get(0).getHackathonEventID());
        Assert.assertEquals(1, (int) actual.get(1).getHackathonEventID());
        Assert.assertEquals(2, (int) actual.get(2).getHackathonEventID());
    }

    private Calendar getCalendarForFuture(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar;
    }

    private HackathonEvent createHackathonEvent(int id, Calendar start){

        Date startDate = new Date(start.getTimeInMillis());

        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(id);
        hackathonEvent.setHackathonEventName("Test"+id);
        hackathonEvent.setHackathonEventStartDate(startDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, 7);
        hackathonEvent.setHackathonEventEndDate(new Date(calendar.getTimeInMillis()));

        return hackathonEvent;
    }
}
