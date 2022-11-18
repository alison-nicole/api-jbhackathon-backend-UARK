package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgingDTO;

public class JudgingDTOMock {
    public static JudgingDTO getValidJudgingDTO() {
        JudgingDTO judgingDTO = new JudgingDTO();
        judgingDTO.setJudgeFirstName("Bob");
        judgingDTO.setJudgeLastName("Ross");
        judgingDTO.setTeamName("Best Team");
        judgingDTO.setTeamScore(50);
        judgingDTO.setFeedback("Great Job");
        return judgingDTO;
    }

    public static JudgingDTO getInvalidJudgeJudgingDTO() {
        JudgingDTO judgingDTO = new JudgingDTO();
        judgingDTO.setJudgeFirstName("Invalid");
        judgingDTO.setJudgeLastName("Invalid");
        judgingDTO.setTeamName("Best Team");
        judgingDTO.setTeamScore(50);
        judgingDTO.setFeedback("Great Job");
        return judgingDTO;
    }
}
