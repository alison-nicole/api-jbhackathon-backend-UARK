package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="HackathonSubEvent", schema = "HCKTHN")
public class HackathonSubEvent {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="HackathonSubEventID")
    private int hackathonSubEventID;

    @Column(name="HackathonEventID", nullable=false)
    private int hackathonEventID;

    @Column(name="SubEventName", nullable=false)
    private String subEventName;

    @Column(name="SubEventStartTimestamp", nullable=false)
    private Date hackathonEventStartDate;

    @Column(name="SubEventEndTimestamp", nullable=false)
    private Date hackathonEventEndDate;
}
