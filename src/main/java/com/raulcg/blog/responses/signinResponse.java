package com.raulcg.blog.responses;

import com.raulcg.blog.models.Role;
import com.raulcg.blog.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class signinResponse {

    private String accessToken;
    private String refreshToken;
    int expiresIn;
    User user;
    Role role;
}
