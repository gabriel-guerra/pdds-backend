package com.pdds.dto;

import com.pdds.domain.enums.Role;
import lombok.Getter;
import lombok.Setter;

public record UserDTO(
        String email,
        String password,
        String fullName,
        String birthday,
        Role role
) {



}
