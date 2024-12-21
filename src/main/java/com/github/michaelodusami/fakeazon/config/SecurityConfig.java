package com.github.michaelodusami.fakeazon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.github.michaelodusami.fakeazon.security.CustomUserDetailsService;
import com.github.michaelodusami.fakeazon.security.JwtAuthFilter;

/**
 * The SecurityConfig class configures Spring Security for the Fakeazon application.
 * 
 * Purpose:
 * This configuration class sets up authentication, authorization, CORS, and JWT-based security mechanisms
 * to protect the application's endpoints. It ensures that user credentials and requests are securely handled.
 * 
 * Why It Matters:
 * A robust security configuration is crucial for protecting the application against unauthorized access,
 * CSRF attacks, and ensuring secure authentication and authorization workflows.
 * 
 * Impact on the Application:
 * - Configures the security filter chain for request authorization and JWT validation.
 * - Enables password encryption using BCrypt.
 * - Ensures stateless session management for scalability and performance.
 * - Handles CORS to allow cross-origin requests in a controlled manner.
 * 
 * Annotations:
 * - @Configuration: Indicates that this class contains Spring configuration beans.
 * - @EnableWebSecurity: Enables Spring Security's web security features.
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    private CustomUserDetailsService customUserDetailsService;
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthFilter jwtAuthFilter)
    {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Provides a PasswordEncoder bean for password hashing.
     * 
     * Purpose:
     * Configures BCryptPasswordEncoder to securely hash user passwords.
     * 
     * Impact:
     * Protects stored passwords against brute-force attacks by using a strong hashing algorithm.
     * 
     * @return a BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    /**
     * Configures the SecurityFilterChain for HTTP security.
     * 
     * Purpose:
     * Sets up the security filter chain to handle authentication, authorization, and session management.
     * 
     * Impact:
     * - Allows public access to authentication endpoints (e.g., `/v1/auth/**`).
     * - Secures all other endpoints by requiring authentication.
     * - Implements stateless session management for scalability.
     * - Adds the JWT authentication filter before the UsernamePasswordAuthenticationFilter.
     * 
     * @param http the HttpSecurity instance to configure.
     * @return the configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/v1/auth/register", "/v1/auth/login").permitAll()
                         .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Configures a CORS filter for cross-origin request handling.
     * 
     * Purpose:
     * Allows cross-origin requests from any origin, with all methods and headers permitted.
     * 
     * Impact:
     * - Facilitates communication between the frontend and backend in development.
     * - Prevents CORS errors when APIs are consumed by external clients.
     * 
     * @return the configured CorsFilter.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // @Bean
    // public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    //     return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    // }
}
