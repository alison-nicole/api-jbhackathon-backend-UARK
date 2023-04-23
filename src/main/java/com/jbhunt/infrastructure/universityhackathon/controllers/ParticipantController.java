package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.constants.HackathonConstants;
import com.jbhunt.infrastructure.universityhackathon.entity.Participant;
import com.jbhunt.infrastructure.universityhackathon.entity.Team;
import com.jbhunt.infrastructure.universityhackathon.services.ParticipantService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/getAllParticipants")
    public @ResponseBody List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }
}
