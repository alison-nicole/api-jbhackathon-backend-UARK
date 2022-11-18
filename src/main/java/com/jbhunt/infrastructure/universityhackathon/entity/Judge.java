package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Judge", schema = "HCKTHN")
public class Judge  {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="JudgeID")
    private Integer judgeID;

    @Column(name="FirstName", nullable=false)
    private String firstName;

    @Column(name="LastName", nullable=false)
    private String lastName;

    @Column(name="EmailAddress", nullable = false)
    private String email;

    @Column(name="EffectiveTimestamp")
    private Date effectiveTimestamp;

    @Column(name="ExpirationTimestamp")
    private Date expirationTimestamp;
}
