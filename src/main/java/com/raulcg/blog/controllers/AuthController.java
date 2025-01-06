package com.raulcg.blog.controllers;

import com.raulcg.blog.models.User;
import com.raulcg.blog.request.SigninRequest;
import com.raulcg.blog.request.SignupRequest;
import com.raulcg.blog.responses.SignupResponse;
import com.raulcg.blog.responses.signinResponse;
import com.raulcg.blog.security.jwt.JwtUtils;
import com.raulcg.blog.security.services.UserDetailsImpl;
import com.raulcg.blog.security.services.UserDetailsServiceImpl;
import com.raulcg.blog.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest userReq) {
        User savedUser = userService.registerUser(userReq);

        // Cargar los detalles del usuario recién registrado
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userReq.getEmail());

        String jwt = jwtUtils.generateTokenFromUserDetails(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        SignupResponse response = new SignupResponse();
        response.setJwtToken(jwt);
        response.setRefreshToken(refreshToken);
        response.setMessage("User registered successfully!");

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SigninRequest userReq) {

        Optional<User> user = userService.findByEmail(userReq.getEmail());

        if (user.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid credentials. Please check your username and password.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Authentication authentication;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userReq.getEmail(), userReq.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateTokenFromUserDetails(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        signinResponse response = new signinResponse();
        response.setAccessToken(jwt);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(7200);
        response.setUser(user.get());
        response.setRole(user.get().getRole());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        String jwtToken = jwtUtils.generateTokenFromRefreshToken(refreshToken);
        Map<String, String> res = new HashMap<>();
        res.put("access_token", jwtToken);
        res.put("expires_in", "7200");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
