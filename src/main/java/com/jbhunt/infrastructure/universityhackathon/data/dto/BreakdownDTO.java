package com.jbhunt.infrastructure.universityhackathon.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreakdownDTO {
    private String teamName;
    private String judge;
    private Integer score1;
    private Integer score2;
    private Integer score3;
    private Integer score4;
    private Double sum;
    private String internal_feedback;
    private String external_feedback;
}
