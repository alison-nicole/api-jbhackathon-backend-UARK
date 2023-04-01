package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.data.dto.AdministratorInfoDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Administrator;
import com.jbhunt.infrastructure.universityhackathon.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor

public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    public Optional<Administrator> getAdministratorByEmail(String email){return administratorRepository.findAdministratorByEmail(email);}

    public Administrator createAdministrator(AdministratorInfoDTO administratorInfo){
        var administrator = new Administrator();
        long now = System.currentTimeMillis();

        administrator.setFirstName(administratorInfo.getFirstName());
        administrator.setLastName(administratorInfo.getLastName());
        administrator.setEmail(administratorInfo.getEmail());
        administrator.setEffectiveTimestamp(new Date(now));

        Optional<Administrator> previousAdministratorInfo = administratorRepository.findAdministratorByFirstNameAndLastName(administratorInfo.getFirstName(),administratorInfo.getLastName());
        previousAdministratorInfo.ifPresent(value -> administrator.setAdministratorID(value.getAdministratorID()));

        return administratorRepository.save(administrator);
    }
}
