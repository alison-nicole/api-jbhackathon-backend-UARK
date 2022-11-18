package com.jbhunt.infrastructure.universityhackathon.security.user;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.enums.Authority;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.IdentifiedUser;

public class JudgeUser extends IdentifiedUser {
    public JudgeUser(SecurityProperties securityProperties) {
        super(securityProperties);
    }
    public JudgeUser(SecurityProperties securityProperties, String id){
        super(securityProperties, id);
    }
    @Override
    protected void init(){
        super.init();
        grant(Authority.JUDGING_SUBMIT, Authority.JUDGING_GET_ALL_TEAMS);
    }
}
