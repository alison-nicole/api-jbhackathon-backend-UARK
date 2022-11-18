package com.jbhunt.infrastructure.universityhackathon.configuration;


import com.jbhunt.infrastructure.universityhackathon.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class TaskConfiguration {
    private final TaskService taskService;
   @PostConstruct
    public void setup() {
       taskService.scheduleDistributionTasks();
   }
}
