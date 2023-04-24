package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

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

    @Column(name="GraduateYear")
    private Integer graduateYear;

    @Column(name="DiscordName")
    private String discordName;

    @Column(name="tShirtSize")
    private String tShirtSize;

    @Column(name = "ClassSeniority")
    private String classSeniority;

    @Column(name="DevType")
    private String devType;

    @Column(name = "score")
    private Integer score;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "TechStack")
    private String techStack;

    //@ElementCollection
//    @Column(name = "TechStack")
//    private Set<String> techStack;

}
