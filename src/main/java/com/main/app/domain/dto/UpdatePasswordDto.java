package com.main.app.domain.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {

    private String email;
    private String updatedPassword;
    private String confirmPassword;
}
