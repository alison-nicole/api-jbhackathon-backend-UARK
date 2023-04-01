package com.jbhunt.infrastructure.universityhackathon.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorInfoDTO {
    String firstName;
    String lastName;
    String email;
}
