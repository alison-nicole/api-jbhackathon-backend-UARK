package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.configuration.SecurityProperties;
import com.jbhunt.infrastructure.universityhackathon.entity.Judge;
import com.jbhunt.infrastructure.universityhackathon.enums.CodeType;
import com.jbhunt.infrastructure.universityhackathon.security.user.base.AuthorizedUser;
import com.jbhunt.infrastructure.universityhackathon.security.user.JudgeUser;
import com.jbhunt.infrastructure.universityhackathon.services.JudgeService;
import com.jbhunt.infrastructure.universityhackathon.services.VerificationService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthenticationController {
    private final VerificationService verificationService;
    private final JudgeService judgeService;
    private final SecurityProperties securityProperties;

    @PostMapping(value = "/auth/authenticate")
    public ResponseEntity<Void> authenticate(@RequestBody String code, HttpServletRequest request, HttpServletResponse response){
        Optional<AuthorizedUser> authenticated = verificationService.authenticate(code, request.getCookies());

        if(authenticated.isPresent()){
            response.addCookie(authenticated.get().generateCookie());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/auth/verify/judge")
    public ResponseEntity<String> verifyJudge(@RequestBody String email, HttpServletResponse response){
        Optional<Judge> found = judgeService.getJudgeByEmail(email);
        if(found.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        JudgeUser user = new JudgeUser(securityProperties, String.valueOf(found.get().getJudgeID()));

        String code = verificationService.generateAndSend(email, CodeType.NUMERIC, 6);
        response.addCookie(verificationService.createVerificationCookie(user, code));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
