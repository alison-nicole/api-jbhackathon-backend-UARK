package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.entity.Participant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParticipantMock {
    public static Participant getTestParticipant() {
        Participant participant = new Participant();
        participant.setFirstName("First");
        participant.setLastName("Last");
        participant.setDevType("backend");
        participant.setClassSeniority("freshman");
        participant.setScore(1);
        participant.setGraduateIndicator(false);
        participant.setSchoolEmailAddress("testemail@test.com");
        participant.setAccommodations("Accommodations");
        participant.setTeamID(null);
        participant.setHackathonEventID(1);
        return participant;
    }

    public static Participant getTestCustomParticipant(String classSeniority, String devType, Integer score) {
        Participant participant = new Participant();
        participant.setFirstName("First");
        participant.setLastName("Last");
        participant.setDevType(devType);
        participant.setClassSeniority(classSeniority);
        participant.setScore(score);
        participant.setGraduateIndicator(false);
        participant.setDevType("backend");
        participant.setClassSeniority("senior");
        participant.setScore(4);
        participant.setGraduateIndicator(false);
        participant.setSchoolEmailAddress("testemail@test.com");
        participant.setAccommodations("Accommodations");
        participant.setTeamID(null);
        participant.setHackathonEventID(1);
        return participant;
    }

    public static Participant getTestParticipantWithTechStack() {
        Participant participant = new Participant();
        participant.setFirstName("First");
        participant.setLastName("Last");
        participant.setDevType("backend");
        participant.setTechStack(setParticipantTechStack());
        participant.setClassSeniority("senior");
        participant.setScore(4);
        participant.setTShirtSize("s");
        participant.setPhoneNumber("123456789");
        participant.setGraduateIndicator(false);
        participant.setSchoolEmailAddress("testemail@test.com");
        participant.setAccommodations("Accommodations");
        participant.setTeamID(null);

        participant.setHackathonEventID(1);
        return participant;
    }


    public static Participant getTestGraduateParticipant() {
        Participant participant = new Participant();
        participant.setFirstName("First");
        participant.setLastName("Last");
        participant.setGraduateIndicator(true);
        participant.setSchoolEmailAddress("testemail@test.com");
        participant.setAccommodations("Accommodations");
        participant.setTeamID(null);
        participant.setHackathonEventID(1);
        return participant;
    }

    public static List<Participant> getListParticipantsWithoutTeam(int numberParticipants) {
        var participantList = new ArrayList<Participant>();
        for(int i = 0; i < numberParticipants; i++) {
            var participant = getTestParticipant();
            participant.setParticipantID(i);
            participant.setScore(i);
            participant.setHackathonEventID(1);
            if((i / 3) == 0)
                participant.setGraduateIndicator(true);
            participantList.add(participant);
        }
        return participantList;
    }


    static String setParticipantTechStack(){
        String techStack = "";
        techStack = "Java, Angular, PostgreSQL";
        return techStack;
    }

    public static List<Participant> getListCustomParticipantsWithoutTeam(int numberParticipants){
        var participantList = new ArrayList<Participant>();
        for (int i = 0; i < numberParticipants; i++){
            var participant = getTestCustomParticipant("sophomore","frontend", 2);
            if((i/2) == 0){
               participant = getTestCustomParticipant("freshman", "backend", 1);
            }
            participant.setParticipantID(i);
            participant.setHackathonEventID(1);
            participantList.add(participant);


        }
        return participantList;
    }

    public  static  List<Participant> getListCustomParticipantsWithTeam(int numberParticipants){

        var participantsList = new ArrayList<Participant>();

        for (int i = 0; i < numberParticipants; i++){
            Participant participant = new Participant();
            participant.setFirstName("First");
            participant.setLastName("Last");
            participant.setDevType("backend");
            participant.setClassSeniority("senior");
            participant.setScore(4);
            participant.setGraduateIndicator(false);
            participant.setSchoolEmailAddress("testemail@test.com");
            participant.setAccommodations("Accommodations");
            participant.setTeamID(1);
            participant.setHackathonEventID(1);
        }

        return participantsList;
    }
    public static Participant getCreatedParticipant(Participant participant) {
        participant.setParticipantID(1);
        return participant;
    }

    public static Participant getCreatedSecondParticipant(Participant participant) {
        participant.setParticipantID(2);
        return participant;
    }
}
