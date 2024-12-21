package com.github.michaelodusami.fakeazon.modules.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.michaelodusami.fakeazon.modules.user.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.enums.UserRole;
import com.github.michaelodusami.fakeazon.modules.user.service.UserService;

import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.save(user, UserRole.ROLE_USER).get();
        return ResponseEntity.status(201).body(createdUser);
    }

    @PostMapping("/admin")
    @RolesAllowed("ADMIN")
    public ResponseEntity<User> createAdminUser(@RequestBody User user) {
        User createdUser = userService.save(user, UserRole.ROLE_ADMIN).get();
        return ResponseEntity.status(201).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // 7. Get Users by Role
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.findUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody String newPassword) {
        boolean updated = userService.changePassword(id, newPassword);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
