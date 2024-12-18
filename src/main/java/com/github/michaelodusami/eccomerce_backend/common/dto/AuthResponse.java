package com.github.michaelodusami.eccomerce_backend.common.dto;

import com.github.michaelodusami.eccomerce_backend.modules.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthResponse {
    private String name;
    private String email;

    public static AuthResponse toUser(User user)
    {
        return new AuthResponse(user.getName(), user.getEmail());
    }
}
