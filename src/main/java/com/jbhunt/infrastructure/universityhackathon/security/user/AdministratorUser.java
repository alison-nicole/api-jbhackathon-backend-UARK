package com.jbhunt.infrastructure.universityhackathon.security.user;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.enums.Authority;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.IdentifiedUser;

public class AdministratorUser extends IdentifiedUser{
    public AdministratorUser(SecurityProperties securityProperties){super(securityProperties);}
    public AdministratorUser(SecurityProperties securityProperties, String id){super(securityProperties,id);}
    @Override
    protected void init(){
        super.init();
        grant(Authority.ADMINISTRATOR_EDIT_PRIZES_PAGE);
    }
}
