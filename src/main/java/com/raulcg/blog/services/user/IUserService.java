package com.raulcg.blog.services.user;

import com.raulcg.blog.models.User;
import com.raulcg.blog.request.SignupRequest;

import java.util.Optional;

public interface IUserService {

    User registerUser(SignupRequest signupRequest);

    User createUser(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}