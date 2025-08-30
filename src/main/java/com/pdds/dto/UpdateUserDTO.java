package com.pdds.dto;

import com.pdds.domain.enums.Role;

public record UpdateUserDTO(
        String email,
        String fullName,
        String birthday,
        Role role
) {
}
