package com.github.michaelodusami.eccomerce_backend.modules.user.repository;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.github.michaelodusami.eccomerce_backend.modules.user.entity.User;

public interface UserRepository extends ListCrudRepository<User, Long> {
    
    public Optional<User> findByEmail(String email);
} 
