package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.entity.Judge;

import java.util.ArrayList;
import java.util.List;

public class JudgeMock {
    public static Judge getJudgeMock() {
        Judge judge = new Judge();
        judge.setJudgeID(4);
        judge.setFirstName("Bob");
        judge.setLastName("Ross");
        return judge;
    }

    public static List<Judge> getListOfJudges() {
        List<Judge> list = new ArrayList<Judge>();
        list.add(getJudgeMock());
        Judge judge2 = getJudgeMock();
        judge2.setJudgeID(5);
        judge2.setFirstName("John");
        list.add(judge2);
        return list;
    }
}
