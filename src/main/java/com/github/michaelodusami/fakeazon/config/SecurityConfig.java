package com.github.michaelodusami.fakeazon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.github.michaelodusami.fakeazon.common.security.CustomUserDetailsService;
import com.github.michaelodusami.fakeazon.common.security.JwtAuthFilter;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthFilter authFilter;

    private UserRepository userRepository;

    @Autowired
    public SecurityConfig(JwtAuthFilter authFilter, UserRepository repository) {
        this.authFilter = authFilter;
        this.userRepository = repository;
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        return new CustomUserDetailsService(userRepository);
    }
    

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth
            .userDetailsService(userDetailsService())
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorize -> 
            authorize.requestMatchers("/v1/auth/**").permitAll().anyRequest().authenticated()
        ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).httpBasic(Customizer.withDefaults());
        return http.build();
    }

     @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
