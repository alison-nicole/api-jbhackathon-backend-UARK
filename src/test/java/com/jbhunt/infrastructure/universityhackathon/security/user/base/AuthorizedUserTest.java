package com.jbhunt.infrastructure.universityhackathon.security.user.base;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.enums.Authority;
import com.jbhunt.infrastructure.universityhackathon.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizedUserTest {
    private final SecurityProperties testSecurityProperties = new SecurityProperties(false, "/", 60, "3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam3123kjasdjf1p34huipuh52345asdjioi870984j12ka9fkql14nam");
    @Test
    public void testGrants(){
        AuthorizedUser user = createUser();

        user.grant(Authority.JUDGE_CREATE);
        user.grantFromStrings(List.of("JUDGE_GET"));

        Assert.assertEquals(2, user.grantedAuthorities.size());
        Assert.assertEquals(Authority.JUDGE_CREATE.name(), user.grantedAuthorities.get(0).getAuthority());
        Assert.assertEquals(Authority.JUDGE_GET.name(), user.grantedAuthorities.get(1).getAuthority());
    }

    @Test
    public void testRevoke() {
        AuthorizedUser user = createUser();

        user.grant(Authority.JUDGE_CREATE);
        user.revoke(Authority.JUDGE_CREATE);

        Assert.assertEquals(0, user.grantedAuthorities.size());
    }

    @Test
    public void testRevokeAll(){
        AuthorizedUser user = createUser();

        user.grant(Authority.JUDGE_CREATE);
        user.grant(Authority.JUDGE_GET);
        user.revokeAll();

        Assert.assertEquals(0, user.grantedAuthorities.size());
    }

    @Test
    public void testGenerateToken(){
        AuthorizedUser user = createUser();
        user.grant(Authority.JUDGE_CREATE);

        String token = user.generateToken();
        List<String> auths = new ArrayList<>(getAuthoritiesFromToken(token));

        Assert.assertEquals(1, auths.size());
        Assert.assertEquals("JUDGE_CREATE", auths.get(0));
    }

    @Test
    public void testParseFromToken(){
        AuthorizedUser user = createUser();
        user.grant(Authority.JUDGE_CREATE);

        AuthorizedUser parsed = user.fromToken(user.generateToken());

        Assert.assertEquals(1, parsed.grantedAuthorities.size());
        Assert.assertEquals("JUDGE_CREATE", parsed.grantedAuthorities.get(0).getAuthority());
    }

    Collection<String> getAuthoritiesFromToken(String token){
        final Claims claims = JwtTokenUtil.getAllClaims(token, testSecurityProperties.getJwtSecret());
        Collection<LinkedHashMap<String, String>> claimMap = claims.get("Authorities", Collection.class);
        return claimMap.stream().map(x -> x.values().stream().findFirst().orElse("")).collect(Collectors.toList());
    }

    //Create a special authorized user that stores its authorities to the token so that we can properly test things out
    AuthorizedUser createUser(){
        return new AuthorizedUser(testSecurityProperties) {
            @Override
            protected void init() {}

            @Override
            public AuthorizedUser fromToken(String token) {
                Collection<String> authorities = getAuthoritiesFromToken(token);
                grantFromStrings(authorities);
                return this;
            }

            @Override
            public JwtBuilder toTokenBuilder() {
                Map<String, Object> claims = new HashMap<>();
                claims.put("Authorities", grantedAuthorities);
                return Jwts.builder().addClaims(claims);
            }
        };
    }
}
