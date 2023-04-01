package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.AdministratorInfoDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Administrator;
import com.jbhunt.infrastructure.universityhackathon.services.AdministratorService;
import static com.jbhunt.infrastructure.universityhackathon.constants.HackathonConstants.ADMINISTRATOR;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping
@RequiredArgsConstructor

public class AdministratorController {

    private final AdministratorService administratorService;

    //create administrators
    @PostMapping("/save")
    public  @ResponseBody ResponseEntity<Administrator> createAdministrator(@Valid @RequestBody AdministratorInfoDTO administratorInfo){
        return new ResponseEntity<>(administratorService.createAdministrator(administratorInfo), HttpStatus.CREATED);
    }

    //get administrator
    @PostMapping("/exists/{email}")
    public @ResponseBody ResponseEntity<Boolean> administratorExists(@PathVariable String email){
        return new ResponseEntity<>(administratorService.getAdministratorByEmail(email).isPresent(), HttpStatus.OK);
    }
}
