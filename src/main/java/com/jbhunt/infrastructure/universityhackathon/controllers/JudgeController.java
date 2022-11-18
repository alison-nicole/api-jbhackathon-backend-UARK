package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgeInfoDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Judge;
import com.jbhunt.infrastructure.universityhackathon.services.JudgeService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.jbhunt.infrastructure.universityhackathon.constants.HackathonConstants.JUDGE;

@CrossOrigin
@RestController
@RequestMapping(value = JUDGE)
@RequiredArgsConstructor
public class JudgeController {

    private final JudgeService judgeService;

    // Grabs all judges that are still eligible
    @GetMapping("/all")
    public @ResponseBody ResponseEntity<List<String>> getAllJudges() {
        return new ResponseEntity<>(judgeService.getJudges(), HttpStatus.OK);
    }

    // Create/edit/delete new judges
    @PostMapping("/save")
    public @ResponseBody ResponseEntity<Judge> createJudge(@Valid @RequestBody JudgeInfoDTO judgeInfo) {
        return new ResponseEntity<>(judgeService.createJudge(judgeInfo), HttpStatus.CREATED);
    }

    @GetMapping("/exists/{email}")
    public @ResponseBody ResponseEntity<Boolean> judgeExists(@PathVariable String email) {
        return new ResponseEntity<>(judgeService.getJudgeByEmail(email).isPresent(), HttpStatus.OK);
    }

}
