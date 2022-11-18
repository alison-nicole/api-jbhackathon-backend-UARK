package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.constants.HackathonConstants;
import com.jbhunt.infrastructure.universityhackathon.services.ParticipantService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping(value = HackathonConstants.PARTICIPANT)
@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping("/email/{email}")
    public @ResponseBody boolean checkForEmail(@PathVariable String email) {
        return participantService.checkEmail(email);
    }
}
