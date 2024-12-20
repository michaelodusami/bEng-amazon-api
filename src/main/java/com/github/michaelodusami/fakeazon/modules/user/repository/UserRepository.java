package com.github.michaelodusami.fakeazon.modules.user.repository;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {
    
    public Optional<User> findByEmail(String email);
} 
