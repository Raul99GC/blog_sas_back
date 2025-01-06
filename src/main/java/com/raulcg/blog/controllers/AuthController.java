package com.raulcg.blog.controllers;

import com.raulcg.blog.models.User;
import com.raulcg.blog.request.SigninRequest;
import com.raulcg.blog.request.SignupRequest;
import com.raulcg.blog.responses.SignupResponse;
import com.raulcg.blog.security.jwt.JwtUtils;
import com.raulcg.blog.security.services.UserDetailsImpl;
import com.raulcg.blog.security.services.UserDetailsServiceImpl;
import com.raulcg.blog.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

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



        return null;
    }
}
