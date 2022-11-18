package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.data.objects.tasks.RunOnceTask;
import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.exceptions.TaskSchedulingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final HackathonEventService hackathonEventService;
    private final SignupService signupService;
    private final HashMap<String, RunOnceTask> scheduledTasks = new HashMap<>();

    public void scheduleDistributionTasks(){
        List<HackathonEvent> currentHackathonEvents = hackathonEventService.getUpcomingHackathonEvents(30);
        for (HackathonEvent hackathonEvent : currentHackathonEvents) {
            String eventDescriptor = createEventDescriptor(hackathonEvent);

            Calendar distributionDate = hackathonEvent.getHackathonEventEarlyRegistrationEndDate();
            if(distributionDate == null){
                log.info("Hackathon Event \"{}\" has no distribution date and so participant distribution cannot be scheduled", eventDescriptor);
                continue;
            }

            //Create and schedule the task in a try/catch in case the event deadline has already passed (runtime exception will be thrown if so)
            try {
                RunOnceTask scheduled = createDistributionTaskFor(hackathonEvent)
                        .onInterrupt(() -> log.info("\"" + eventDescriptor + "\" Distribution task was unexpectedly interrupted."))
                        .schedule(distributionDate);

                addScheduledTask(scheduled);

                log.info("Scheduled \"{}\" distribution task for {}", eventDescriptor, distributionDate.getTime());
            }
            catch (Exception e){
                log.error("Error scheduling distribution task for hackathon event \"{}\"", eventDescriptor, e);
            }
        }
    }

    public Map<String, RunOnceTask> getScheduledTasks() {
        return scheduledTasks;
    }

    private void addScheduledTask(RunOnceTask task){
        if(scheduledTasks.containsKey(task.getName())) throw new TaskSchedulingException("Task with name \"" + task.getName() + "\" is already scheduled");
        if(!task.isScheduled()) throw new IllegalStateException("Task \"" + task.getName() + "\" is not scheduled, cannot add to map");
        scheduledTasks.put(task.getName(), task);
    }

    private RunOnceTask createDistributionTaskFor(HackathonEvent hackathonEvent){
        String eventDescriptor = createEventDescriptor(hackathonEvent);
        String distributionTaskName = createTypedTaskName(eventDescriptor, "DistributionTask");

        return new RunOnceTask(distributionTaskName, () -> {
            signupService.manageParticipantsWithoutTeam(hackathonEvent.getHackathonEventID());
            scheduledTasks.remove(distributionTaskName);
            log.info(eventDescriptor + " distribution complete");
        });
    }

    private String createEventDescriptor(HackathonEvent hackathonEvent){
        return hackathonEvent.getHackathonEventName() + "_" + hackathonEvent.getHackathonEventID();
    }

    private String createTypedTaskName(String taskName, String taskType) {
        return taskName + "_" + taskType;
    }
}
