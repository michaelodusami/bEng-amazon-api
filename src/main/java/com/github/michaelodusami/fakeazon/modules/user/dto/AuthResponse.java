package com.github.michaelodusami.fakeazon.modules.user.dto;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthResponse {
    private Long id;
    private String name;
    private String email;

    public static AuthResponse toUser(User user)
    {
        return new AuthResponse(user.getId(), user.getName(), user.getEmail());
    }
}
