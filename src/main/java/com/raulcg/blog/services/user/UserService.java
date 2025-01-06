package com.raulcg.blog.services.user;

import com.raulcg.blog.UserRole;
import com.raulcg.blog.exceptions.EmailAlreadyExistsException;
import com.raulcg.blog.exceptions.UsernameAlreadyExistsException;
import com.raulcg.blog.models.Role;
import com.raulcg.blog.models.User;
import com.raulcg.blog.repositories.RoleRepository;
import com.raulcg.blog.repositories.UserRepository;
import com.raulcg.blog.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already taken");
        } else if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        Role role = roleRepository.findByRoleName(UserRole.ROLE_COSTUMER).orElseThrow(() -> new RuntimeException("Role not found"));

        User newUser = new User(signupRequest.getUsername(), signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()));
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setProfileImage(signupRequest.getProfileImage());
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
