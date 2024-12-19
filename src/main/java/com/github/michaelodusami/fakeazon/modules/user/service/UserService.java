package com.github.michaelodusami.fakeazon.modules.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.michaelodusami.fakeazon.common.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

import lombok.NonNull;

public class UserService {
    
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    /** 
     * @return List<User>
     */
    public List<User> findAll()
    {
        return userRepository.findAll();
    }

    
    /** 
     * @param id
     * @return Optional<User>
     */
    public Optional<User> findById(@NonNull Long id)
    {
        return userRepository.findById(id);
    }

    
    /** 
     * @param id
     * @return Optional<User>
     */
    public Optional<User> findByEmail(@NonNull String email)
    {
        return userRepository.findByEmail(email);
    }
    
    /** 
     * @param user
     * @return Optional<User>
     */
    public Optional<User> save(@NonNull RegisterRequest registerRequest)
    {
        
        if (findByEmail(registerRequest.getEmail()).isPresent())
        {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }

    
    /** 
     * @param id
     * @return boolean
     */
    public boolean deleteUser(@NonNull Long id)
    {
        userRepository.deleteById(id);
        return findById(id).isEmpty();
    }
}
