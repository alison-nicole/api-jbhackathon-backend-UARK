package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.data.dto.SignUpFormDTO;

public class SignupFormDTOMock {

    public static SignUpFormDTO getValidSignUpFormDTOJoinTeam() {
        SignUpFormDTO signUpFormDTO = new SignUpFormDTO();
        signUpFormDTO.setFirstName("First");
        signUpFormDTO.setLastName("Last");
        signUpFormDTO.setIsGradStudent(false);
        signUpFormDTO.setSchoolEmailAddress("testemail@test.com");
        signUpFormDTO.setAccommodations("Accommodations");
        signUpFormDTO.setTeamName("TestTeam");
        signUpFormDTO.setTeamOpen(null);
        signUpFormDTO.setTeamColorCode("");
        signUpFormDTO.setTeamIconCode("");

        return signUpFormDTO;
    }

    public static SignUpFormDTO getValidSignUpFormDTOCreatedTeam() {
        SignUpFormDTO signUpFormDTO = new SignUpFormDTO();
        signUpFormDTO.setFirstName("First");
        signUpFormDTO.setLastName("Last");
        signUpFormDTO.setIsGradStudent(false);
        signUpFormDTO.setSchoolEmailAddress("testemail@test.com");
        signUpFormDTO.setAccommodations("Accommodations");
        signUpFormDTO.setTeamName("TestTeam");
        signUpFormDTO.setTeamOpen(true);
        signUpFormDTO.setTeamColorCode("#ffffff");
        signUpFormDTO.setTeamIconCode("medal");

        return signUpFormDTO;
    }

    public static SignUpFormDTO getValidSignUpFormDTOWithoutTeam() {
        SignUpFormDTO signUpFormDTO = new SignUpFormDTO();
        signUpFormDTO.setFirstName("First");
        signUpFormDTO.setLastName("Last");
        signUpFormDTO.setClassSeniority("senior");
        signUpFormDTO.setDevType("backend");
        signUpFormDTO.setScore(null);
        signUpFormDTO.setIsGradStudent(false);
        signUpFormDTO.setSchoolEmailAddress("testemail@test.com");
        signUpFormDTO.setAccommodations("Accommodations");
        signUpFormDTO.setTeamName("");
        signUpFormDTO.setTeamOpen(null);
        signUpFormDTO.setTeamColorCode("");
        signUpFormDTO.setTeamIconCode("");

        return signUpFormDTO;
    }

    public static SignUpFormDTO getValidSignUpWithTechStackFormDTOWithoutTeam() {
        SignUpFormDTO signUpFormDTO = new SignUpFormDTO();
        signUpFormDTO.setFirstName("First");
        signUpFormDTO.setLastName("Last");
        signUpFormDTO.setClassSeniority("senior");
        signUpFormDTO.setDevType("backend");
        signUpFormDTO.setTechStack("Python","Java","MongoDB","React");
        signUpFormDTO.setScore(4);
        signUpFormDTO.setIsGradStudent(false);
        signUpFormDTO.setSchoolEmailAddress("testemail@test.com");
        signUpFormDTO.setAccommodations("Accommodations");
        signUpFormDTO.setTeamName("");
        signUpFormDTO.setTeamOpen(null);
        signUpFormDTO.setTeamColorCode("");
        signUpFormDTO.setTeamIconCode("");

        return signUpFormDTO;
    }

    public static SignUpFormDTO getValidSignUpFormDTOGradStudentJoinTeam() {
        SignUpFormDTO signUpFormDTO = new SignUpFormDTO();
        signUpFormDTO.setFirstName("First");
        signUpFormDTO.setLastName("Last");
        signUpFormDTO.setIsGradStudent(true);
        signUpFormDTO.setSchoolEmailAddress("testemail@test.com");
        signUpFormDTO.setAccommodations("Accommodations");
        signUpFormDTO.setTeamName("Test");
        signUpFormDTO.setTeamOpen(null);
        signUpFormDTO.setTeamColorCode("");
        signUpFormDTO.setTeamIconCode("");

        return signUpFormDTO;
    }
}
