package com.jbhunt.infrastructure.universityhackathon.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrizesDTO {
    private Integer hackathonEventID;
    private String prizeName;
    private String prizeMonetaryValue;
    private  String prizeLink;
    private  String prizeImageCode;

}