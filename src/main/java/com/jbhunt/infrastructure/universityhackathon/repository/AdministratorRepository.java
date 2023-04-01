package com.jbhunt.infrastructure.universityhackathon.repository;

import com.jbhunt.infrastructure.universityhackathon.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    Optional<Administrator> findAdministratorByFirstNameAndLastName(String firstName, String lastName);
    Optional<Administrator> findAdministratorByEmail(String email);

}