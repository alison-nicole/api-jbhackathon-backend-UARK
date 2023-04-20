package com.jbhunt.infrastructure.universityhackathon.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "prizes", schema = "HCKTHN")
public class Prizes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PrizeID")
    private Integer prizeID;

    @Column(name = "HackathonEventID", nullable = false)
    private Integer hackathonEventID;

    @Column(name="PrizeName", nullable=false)
    private String prizeName;

    @Column(name = "PrizeMonetaryValue",nullable = false)
    private String prizeMonetaryValue;

    @Column(name = "PrizeLink",nullable = false)
    private String prizeLink;

    @Column(name = "PrizeImageCode",nullable = false)
    private String prizeImageCode;
}