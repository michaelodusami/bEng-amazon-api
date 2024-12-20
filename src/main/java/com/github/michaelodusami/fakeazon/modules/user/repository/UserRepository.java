package com.github.michaelodusami.fakeazon.modules.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {
    
    public Optional<User> findByEmail(String email);

    @Query("SELECT u from User u JOIN u.roles r WHERE r = :role")
    List<User> findUsersByRole(@Param("role") String role);
} 
