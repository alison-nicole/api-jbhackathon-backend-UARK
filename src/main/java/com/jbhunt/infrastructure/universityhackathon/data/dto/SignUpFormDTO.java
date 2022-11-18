package com.jbhunt.infrastructure.universityhackathon.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpFormDTO {
    private String firstName;
    private String lastName;
    private String schoolEmailAddress;
    private Boolean isGradStudent;
    private String teamName;
    private Boolean teamOpen;
    private String teamColorCode;
    private String teamIconCode;
    private String accommodations;
}
