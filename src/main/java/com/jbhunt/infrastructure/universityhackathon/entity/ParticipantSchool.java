package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="ParticipantSchool", schema = "HCKTHN")
public class ParticipantSchool {
    @Id
    @Column(name="ParticipantSchoolName", nullable=false)
    private String participantSchoolName;

    @Column(name="schoolEmailDomain", nullable=false)
    private String schoolEmailDomain;
}
