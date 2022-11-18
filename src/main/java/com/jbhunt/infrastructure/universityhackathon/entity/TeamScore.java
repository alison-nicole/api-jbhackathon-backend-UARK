package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name="TeamScore", schema = "HCKTHN")
public class TeamScore {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ScoreID", nullable=false)
    private Integer scoreID;

    @Column(name="TeamID", nullable = false)
    private Integer teamID;

    @Column(name="JudgeID", nullable = false)
    private Integer judgeID;

    @Column(name="Score", nullable = false)
    private int score;

    @Column(name="feedback")
    private String feedback;
}
