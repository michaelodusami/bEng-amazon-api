package com.github.michaelodusami.eccomerce_backend.modules.user.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.github.michaelodusami.eccomerce_backend.modules.user.entity.User;

public interface UserRepository extends ListCrudRepository<User, Long> {
    
} 
