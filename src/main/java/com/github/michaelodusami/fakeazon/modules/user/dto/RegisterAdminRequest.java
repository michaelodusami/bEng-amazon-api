package com.github.michaelodusami.fakeazon.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterAdminRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}
