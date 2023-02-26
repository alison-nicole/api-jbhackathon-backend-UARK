package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Participant", schema = "HCKTHN")
public class Participant {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ParticipantID")
    private Integer participantID;

    @Column(name="HackathonEventID", nullable=false)
    private Integer hackathonEventID;

    @Column(name="FirstName", nullable=false)
    private String firstName;

    @Column(name="LastName", nullable=false)
    private String lastName;

    @Column(name="SchoolEmailAddress", nullable=false)
    private String schoolEmailAddress;

    @Column(name="TeamID")
    private Integer teamID;

    @Column(name="GraduateIndicator")
    private Boolean graduateIndicator;

    @Column(name="Accommodations")
    private String accommodations;

    @Column(name="major")
    private String major;

    @Column(name="universityName")
    private String universityName;

    @Column(name="graduateYear")
    private Integer graduateYear;

    @Column(name="discordName")
    private String discordName;

    @Column(name="tShirtSize")
    private String tShirtSize;
}
