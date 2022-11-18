package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.HackathonFormDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.services.HackathonEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.jbhunt.infrastructure.universityhackathon.constants.HackathonConstants.HACKATHON;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = HACKATHON)
public class HackathonEventController {

    private final HackathonEventService hackathonEventService;

    // Create Hackathon event
    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<HackathonEvent> saveHackathon(@Valid @RequestBody HackathonFormDTO newHackathon) {
        return new ResponseEntity<>(hackathonEventService.createHackathonEvent(newHackathon), HttpStatus.CREATED);
    }

    // Returns list of hackathon events that are still on-going [First hackathon is the most recent]
    @ResponseBody
    @GetMapping("/current")
    public ResponseEntity<List<HackathonEvent>> getCurrentHackathon() {
        return new ResponseEntity<>(hackathonEventService.getCurrentHackathon(), HttpStatus.OK);
    }
}
