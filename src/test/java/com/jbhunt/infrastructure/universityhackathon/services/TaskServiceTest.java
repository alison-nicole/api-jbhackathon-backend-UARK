package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
    @Mock
    private HackathonEventService hackathonEventService;
    @Mock
    private SignupService signupService;
    @InjectMocks
    private TaskService taskService;

    @Test
    public void testScheduleDistributionTasks() {
        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("TestEvent");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 50);
        hackathonEvent.setHackathonEventEarlyRegistrationEndDate(calendar);

        when(hackathonEventService.getUpcomingHackathonEvents(anyInt())).thenReturn(List.of(hackathonEvent));

        taskService.scheduleDistributionTasks();

        Assert.assertTrue(taskService.getScheduledTasks().containsKey("TestEvent_1_DistributionTask"));

        Awaitility.await()
                .atMost(100, java.util.concurrent.TimeUnit.MILLISECONDS)
                .pollDelay(50, java.util.concurrent.TimeUnit.MILLISECONDS)
                .until(() -> true);

        verify(signupService).manageParticipantsWithoutTeam(1);
        Assert.assertFalse(taskService.getScheduledTasks().containsKey("TestEvent_1"));
    }

    @Test
    public void testScheduleDistributionTasksNullDate() {
        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("TestEvent");

        when(hackathonEventService.getUpcomingHackathonEvents(anyInt())).thenReturn(List.of(hackathonEvent));

        taskService.scheduleDistributionTasks();

        Assert.assertFalse(taskService.getScheduledTasks().containsKey("TestEvent_1_DistributionTask"));
        verifyNoInteractions(signupService);
    }

    @Test
    public void testScheduleDistributionTasksPastDate() {
        HackathonEvent hackathonEvent = new HackathonEvent();
        hackathonEvent.setHackathonEventID(1);
        hackathonEvent.setHackathonEventName("TestEvent");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, -100);
        hackathonEvent.setHackathonEventEarlyRegistrationEndDate(calendar);

        when(hackathonEventService.getUpcomingHackathonEvents(anyInt())).thenReturn(List.of(hackathonEvent));

        taskService.scheduleDistributionTasks();

        Assert.assertFalse(taskService.getScheduledTasks().containsKey("TestEvent_1_DistributionTask"));
        verifyNoInteractions(signupService);
    }
}
