package com.github.michaelodusami.fakeazon.modules.user.dto;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginRequest {

    private String email;
    private String password;

    public static LoginRequest toLoginRequest(User user)
    {
        return new LoginRequest(user.getEmail(), user.getPassword());
    }
}