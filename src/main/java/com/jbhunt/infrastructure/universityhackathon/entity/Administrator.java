package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Administrator", schema = "HCKTHN")
public class Administrator {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name= "AdministratorID")
    private Integer administratorID;

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