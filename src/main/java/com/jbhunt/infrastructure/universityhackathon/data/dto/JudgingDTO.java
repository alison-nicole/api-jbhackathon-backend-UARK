package com.jbhunt.infrastructure.universityhackathon.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgingDTO {
    private String teamName;
    private String judgeFirstName;
    private String judgeLastName;
    private int teamScore;
    private String feedback;
}
