package com.pdds.dto;

public record ChangePasswordDTO(
        String oldPassword,
        String newPassword) {
}
