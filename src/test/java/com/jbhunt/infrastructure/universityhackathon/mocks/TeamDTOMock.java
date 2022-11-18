package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.data.dto.TeamDTO;

public class TeamDTOMock {
    public static TeamDTO getTeamDTO() {
        TeamDTO teamDTO = new TeamDTO();
        Byte[] byteArray = {0,1,2,3,4,5,6,7,8,9};
        teamDTO.setTeamPicture(byteArray);
        teamDTO.setProjectName("");
        teamDTO.setProjectDescription("");
        teamDTO.setProjectTechnologyDescription("");
        teamDTO.setPublicIndicator(1);

        return teamDTO;
    }
}
