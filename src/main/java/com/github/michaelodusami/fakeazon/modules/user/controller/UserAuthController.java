package com.github.michaelodusami.fakeazon.modules.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.michaelodusami.fakeazon.modules.user.dto.AuthResponse;
import com.github.michaelodusami.fakeazon.modules.user.dto.LoginRequest;
import com.github.michaelodusami.fakeazon.modules.user.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.enums.UserRole;
import com.github.michaelodusami.fakeazon.modules.user.service.UserService;
import com.github.michaelodusami.fakeazon.security.JwtService;
import com.github.michaelodusami.fakeazon.security.UserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
public class UserAuthController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest authRequest)
    {
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

            User user = userService.findByEmail(authRequest.getEmail()).get();

            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,  jwtService.generateToken(user.getEmail())).body(AuthResponse.toUser(user));
        }
        catch(BadCredentialsException exception)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest registerRequest)
    {
        try {
            userService.save(registerRequest, UserRole.ROLE_USER).get();
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid RegisterRequest registerRequest)
    {
        try {
            userService.save(registerRequest, UserRole.ROLE_ADMIN).get();
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } 
    }
  
}
