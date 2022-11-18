package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.SignUpFormDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.enums.CodeType;
import com.jbhunt.infrastructure.universityhackathon.services.SignupService;
import com.jbhunt.infrastructure.universityhackathon.services.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;
    private final VerificationService verificationService;

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<Participant> saveParticipant(@Valid @RequestBody SignUpFormDTO newParticipant) {

        try {
            Participant participant = signupService.saveParticipant(newParticipant);
            return new ResponseEntity<>(participant, HttpStatus.CREATED);
        }
        catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/signup/emailverification/{recipientEmail}")
    public @ResponseBody int verifyEmail(@PathVariable String recipientEmail) {
        return Integer.parseInt(verificationService.generateAndSend(recipientEmail, CodeType.NUMERIC, 6));
    }
}
