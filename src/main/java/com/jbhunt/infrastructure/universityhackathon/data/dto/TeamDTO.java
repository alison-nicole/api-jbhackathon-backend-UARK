package com.jbhunt.infrastructure.universityhackathon.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {
    private String teamName;
    private int hackathonEventID;
    private String teamCode;
    private Byte[] teamPicture;
    private String projectName;
    private String projectDescription;
    private String projectTechnologyDescription;
    private int publicIndicator;

    //TODO: research if it is needed to add strength attribute
}
