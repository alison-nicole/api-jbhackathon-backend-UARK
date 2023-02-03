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

    //add class Seniority attribute (string format from signup form) for score = (0-4) 0:HS, 1:FR, 2:SP, 3:JU, 4:SE
    @Column(name = "ClassSeniority")
    private String classSeniority;

    //add developer type
    @Column(name="DevType")
    private String devType;

    // score level
    /*
     * Note:
     * Using the @Transient annotation to indicate that this field of the entity
     * should not be persisted to the database, it will not be saved.
     * */
    @Transient
    private Integer score;

    @Column(name="Graduate")
    private Boolean graduate;

    @Column(name="Accommodations")
    private String accommodations;

    @Column(name="EffectiveTimestamp")
    private Date effectiveTimestamp;

    @Column(name="ExpirationTimestamp")
    private Date expirationTimestamp;
}
