package com.github.michaelodusami.eccomerce_backend.modules.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.michaelodusami.eccomerce_backend.modules.user.entity.User;
import com.github.michaelodusami.eccomerce_backend.modules.user.repository.UserRepository;

import lombok.NonNull;

public class UserService {
    
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
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
    public Optional<User> save(@NonNull User user)
    {
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
