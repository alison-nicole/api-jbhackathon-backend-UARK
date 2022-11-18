package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.HackathonFormDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.services.HackathonEventService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HackathonEventControllerTest {
    @Mock
    private HackathonEventService mockHackathonEventService;

    @InjectMocks
    private HackathonEventController hackathonEventController;

    @Test
    public void saveHackathonEventTest() {
        HackathonFormDTO hackathonFormDTO = new HackathonFormDTO();
        hackathonFormDTO.setHackathonEventName("Fall 2020");
        hackathonFormDTO.setHackathonTopicDescription("Efficiency");
        hackathonFormDTO.setHackathonEventEndDate(new Date(System.currentTimeMillis()));
        hackathonFormDTO.setHackathonEventStartDate(new Date(System.currentTimeMillis()));
        hackathonFormDTO.setHackathonEventEarlyRegistrationEndDate(new Date(System.currentTimeMillis()));

        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("Test");
        when(mockHackathonEventService.createHackathonEvent(any())).thenReturn(hackathonEvent);
        ResponseEntity<HackathonEvent> result = hackathonEventController.saveHackathon(hackathonFormDTO);
        Assert.assertEquals(hackathonEvent, result.getBody());
        verify(mockHackathonEventService).createHackathonEvent(any());
    }

    @Test
    public void getCurrentHackathonEventTest() {
        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventEndDate(new Date(System.currentTimeMillis()+200000));

        List<HackathonEvent> mockResult = new ArrayList<>();
        mockResult.add(hackathonEvent);
        when(mockHackathonEventService.getCurrentHackathon()).thenReturn(mockResult);

        ResponseEntity<List<HackathonEvent>> actual = hackathonEventController.getCurrentHackathon();

        Assert.assertEquals(mockResult, actual.getBody());
        verify(mockHackathonEventService).getCurrentHackathon();
    }
}
