package com.github.michaelodusami.fakeazon.modules.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.michaelodusami.fakeazon.common.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.enums.UserRole;
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
        user.getRoles().add(UserRole.ROLE_USER.getRole());
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }

    /** 
     * @param user
     * @return Optional<User>
     */
    public Optional<User> save(@NonNull User user, UserRole role)
    {
        
        if (findByEmail(user.getEmail()).isPresent())
        {
            throw new IllegalArgumentException("Email already registered");
        }
      
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.getRoles().add(role.getRole());
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


    public Optional<User> updateUser(Long id, User updatedUser) {
        // Find the existing user by ID
        return userRepository.findById(id).map(existingUser -> {
            // Update fields from updatedUser
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
                existingUser.setPassword(encodedPassword);
            }
            if (updatedUser.getRoles() != null) {
                existingUser.setRoles(updatedUser.getRoles());
            }
            // Save and return the updated user
            return userRepository.save(existingUser);
        });
    }


    public boolean changePassword(Long id, String newPassword) {
        return userRepository.findById(id).map(user -> {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }


    public List<User> findUsersByRole(String role) {
        return userRepository.findUsersByRole(role);
    }
    
    
}
