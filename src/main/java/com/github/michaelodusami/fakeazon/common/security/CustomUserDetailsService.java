package com.github.michaelodusami.fakeazon.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        return user.map(com.github.michaelodusami.fakeazon.common.security.UserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        
    }
    
}
