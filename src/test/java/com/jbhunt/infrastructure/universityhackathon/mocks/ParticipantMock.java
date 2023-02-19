package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.entity.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantMock {
    public static Participant getTestParticipant() {
        Participant participant = new Participant();
        participant.setFirstName("First");
        participant.setLastName("Last");
        participant.setDevType("backend"); //TODO: modify dev type test participant
        participant.setClassSeniority("freshman"); //TODO: modify class seniority for test participant
        participant.setScore(1);
        participant.setGraduate(false);
        participant.setSchoolEmailAddress("testemail@test.com");
        participant.setAccommodations("Accommodations");
        participant.setTeamID(null);
        participant.setEffectiveTimestamp(null);
        participant.setExpirationTimestamp(null);
        participant.setHackathonEventID(1);
        return participant;
    }

    //TODO: remove this method
    public static Participant getTestCustomParticipant(String classSeniority, String devType, Integer score) {
        Participant participant = new Participant();
        participant.setFirstName("First");
        participant.setLastName("Last");
        participant.setDevType(devType);
        participant.setClassSeniority(classSeniority);
        participant.setScore(score);
        participant.setGraduate(false);
        participant.setSchoolEmailAddress("testemail@test.com");
        participant.setAccommodations("Accommodations");
        participant.setTeamID(null);
        participant.setEffectiveTimestamp(null);
        participant.setExpirationTimestamp(null);
        participant.setHackathonEventID(1);
        return participant;
    }

    public static Participant getTestGraduateParticipant() {
        Participant participant = new Participant();
        participant.setFirstName("First");
        participant.setLastName("Last");
        participant.setGraduate(true);
        participant.setSchoolEmailAddress("testemail@test.com");
        participant.setAccommodations("Accommodations");
        participant.setTeamID(null);
        participant.setEffectiveTimestamp(null);
        participant.setExpirationTimestamp(null);
        participant.setHackathonEventID(1);
        return participant;
    }
//this will assign all test participants with the same score.
    public static List<Participant> getListParticipantsWithoutTeam(int numberParticipants) {
        var participantList = new ArrayList<Participant>();
        for(int i = 0; i < numberParticipants; i++) {
            var participant = getTestParticipant();
            participant.setParticipantID(i);
            participant.setHackathonEventID(1);
            //FIXME: add a test method for teams with gradute participants
//            if((i / 3) == 0)
//                participant.setGraduate(true);
            participantList.add(participant);
        }
        return participantList;
    }

    public static List<Participant> getListCustomParticipantsWithoutTeam(int numberParticipants){
        var participantList = new ArrayList<Participant>();
        for (int i = 0; i < numberParticipants; i++){
            var participant = getTestCustomParticipant("sophomore","frontend", 2);
            //alternate class seniority
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
            participant.setGraduate(false);
            participant.setSchoolEmailAddress("testemail@test.com");
            participant.setAccommodations("Accommodations");
            participant.setTeamID(1);
            participant.setEffectiveTimestamp(null);
            participant.setExpirationTimestamp(null);
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
