package com.jbhunt.infrastructure.universityhackathon.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HackathonFormDTO {
    private String hackathonEventName;
    private String hackathonTopicDescription;
    private Date hackathonEventStartDate;
    private Date hackathonEventEndDate;
    private Date hackathonEventEarlyRegistrationEndDate;
}
