package com.github.michaelodusami.fakeazon.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private String email;

    private String password;

    private Set<GrantedAuthority> authorities;

    public UserDetails(User user)
    {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
    
}
