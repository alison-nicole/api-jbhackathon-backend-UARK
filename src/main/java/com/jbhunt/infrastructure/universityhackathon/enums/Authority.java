package com.jbhunt.infrastructure.universityhackathon.enums;


public enum Authority {
    JUDGE_CREATE("judge_create"),
    JUDGE_GET("judge_get"),
    JUDGING_SUBMIT("judging_submit"),
    JUDGING_GET_ALL_TEAMS("judging_get_all_teams");

    private final String name;
    Authority(final String name){
        this.name = name;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
