package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Calendar;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HackathonEvent", schema = "HCKTHN")
public class HackathonEvent {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="HackathonEventID", nullable=false)
    private Integer hackathonEventID;

    @Column(name="HackathonEventName", nullable=false)
    private String hackathonEventName;

    @Column(name="HackathonTopicDescription", nullable=false)
    private String hackathonTopicDescription;

    @Column(name="HackathonEventStartDate", nullable=false)
    private Date hackathonEventStartDate;

    @Column(name="HackathonEventEndDate", nullable=false)
    private Date hackathonEventEndDate;

    @Column(name="HackathonEventEarlyRegistrationEndDate", nullable=false)
    private Calendar hackathonEventEarlyRegistrationEndDate;
}
