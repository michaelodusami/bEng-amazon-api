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

import com.github.michaelodusami.fakeazon.common.dto.RegisterRequest;
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

    // 1. Get All Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 2. Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Get User by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Create New User
    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.save(user, UserRole.ROLE_USER).get();
        return ResponseEntity.status(201).body(createdUser);
    }

    // Create New Admin User
    @PostMapping("/admin")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<User> createAdminUser(@RequestBody User user) {
        User createdUser = userService.save(user, UserRole.ROLE_ADMIN).get();
        return ResponseEntity.status(201).body(createdUser);
    }

    // 5. Update User by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. Delete User by ID
    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // // 7. Get Users by Role
    // @GetMapping("/role/{role}")
    // public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
    //     List<User> users = userService.findUsersByRole(role);
    //     return ResponseEntity.ok(users);
    // }

    // 8. Change User Password
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody String newPassword) {
        boolean updated = userService.changePassword(id, newPassword);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // // 9. Disable User Account
    // @PatchMapping("/{id}/disable")
    // public ResponseEntity<Void> disableUserAccount(@PathVariable Long id) {
    //     boolean disabled = userService.disableUser(id);
    //     return disabled ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    // }

    // // 10. Get Recently Created Users
    // @GetMapping("/recent")
    // public ResponseEntity<List<User>> getRecentUsers() {
    //     List<User> users = userService.findRecentUsers();
    //     return ResponseEntity.ok(users);
    // }
}
