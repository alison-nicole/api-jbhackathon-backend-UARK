package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.*;

import javax.persistence.*;



@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Team", schema = "HCKTHN")
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="TeamID")
    private Integer teamID;

    // Non-Identity Keys
    @Column(name="TeamName", nullable=false)
    private String teamName;

    @Column(name="Score")
    private Integer score;

    @Column(name="TeamOwnerID")
    private Integer teamOwnerID;

    @Column(name="MemberCount")
    private Integer memberCount;

    @Column(name="GraduateCount")
    private Integer graduateCount;

    @Column(name="open")
    private boolean open;

    @Column(name="HackathonEventID", nullable=false)
    private Integer hackathonEventID;

    @Column(name="TeamCode", nullable=false)
    private String teamCode;

    @Column(name="TeamColorCode")
    private String teamColorCode;

    @Column(name="TeamIconCode")
    private String teamIconCode;

    @Transient
    private Double teamStrength;

}
