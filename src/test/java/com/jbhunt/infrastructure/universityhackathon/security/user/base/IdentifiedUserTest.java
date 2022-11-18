package com.jbhunt.infrastructure.universityhackathon.security.user.base;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IdentifiedUserTest {
    private final SecurityProperties testSecurityProperties = new SecurityProperties(false, "/", 60, "3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam");

    @Test
    public void testFromToken() {
        IdentifiedUser user = new IdentifiedUser(testSecurityProperties, "testId");
        String token = user.generateToken();
        IdentifiedUser parsedUser = (IdentifiedUser) new IdentifiedUser(testSecurityProperties).fromToken(token);
        Assert.assertEquals(user.getUserId(), parsedUser.getUserId());
    }

    @Test
    public void testValidateToken(){
        IdentifiedUser user = new IdentifiedUser(testSecurityProperties, "testId");
        String token = user.generateToken();

        Assert.assertTrue(user.validateToken(token));

        user.setUserId("wrongId");
        Assert.assertFalse(user.validateToken(token));
    }
}
