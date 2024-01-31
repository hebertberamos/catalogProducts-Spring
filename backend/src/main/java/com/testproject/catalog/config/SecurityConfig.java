package com.testproject.catalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public WebSecurityCustomizer customizer(){
        return (web) -> web.ignoring().requestMatchers((new AntPathRequestMatcher("/h2-console/**")));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")   // Only user who have the Role ADMIN can create a new category
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")     // Only user who have the Role ADMIN can create a new products
                        .requestMatchers(HttpMethod.DELETE, "/users").hasRole("ADMIN")      // Only user who have the Role ADMIN can delete a user
                        .requestMatchers(HttpMethod.DELETE, "/products").hasRole("ADMIN")   // Only user who have the Role ADMIN can delete a product
                        .requestMatchers(HttpMethod.DELETE, "/categories").hasRole("ADMIN") // Only user who have the Role ADMIN can delete a category
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("EMPLOYEE")      // Only user who have the Role EMPLOYEE can get users
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()     // All users have the right to register on the website
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()        // All user have the right to login on the website.
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
