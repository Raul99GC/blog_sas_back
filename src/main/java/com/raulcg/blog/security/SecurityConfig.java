package com.raulcg.blog.security;

import com.raulcg.blog.UserRole;
import com.raulcg.blog.models.Role;
import com.raulcg.blog.repositories.RoleRepository;
import com.raulcg.blog.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthTokenFilter authJwtTokenFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Permite que los requests que no esten protegidos por Spring Security
        http.sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(authJwtTokenFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository) {

        return args -> {
            if (roleRepository.findByRoleName(UserRole.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(UserRole.ROLE_ADMIN));
            }

            if (roleRepository.findByRoleName(UserRole.ROLE_COSTUMER).isEmpty()) {
                roleRepository.save(new Role(UserRole.ROLE_COSTUMER));
            }
        };
    }
}
