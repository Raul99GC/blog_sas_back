package com.raulcg.blog.security;

import com.raulcg.blog.UserRole;
import com.raulcg.blog.models.Role;
import com.raulcg.blog.repositories.RoleRepository;
import com.raulcg.blog.security.jwt.AuthEntryPointJwt;
import com.raulcg.blog.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthTokenFilter authJwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Permite que los requests que no esten protegidos por Spring Security
        http.sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/**").authenticated()
                .anyRequest().permitAll()
        );

        http.addFilterBefore(authJwtTokenFilter, BasicAuthenticationFilter.class);

        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // ? Crea un bean del tipo AuthenticationManager para que esté disponible en el contexto de Spring y pueda ser utilizado en otros lugares de tu aplicación.
    // ? Se utiliza para gestionar el proceso de autenticación en la aplicación.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
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
