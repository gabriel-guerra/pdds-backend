package com.pdds.dto;

import com.pdds.domain.enums.Role;

public record CreateUserDTO(
        String email,
        String password,
        String fullName,
        String birthday,
        Role role
) {



}
