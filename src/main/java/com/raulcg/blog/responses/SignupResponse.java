package com.raulcg.blog.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupResponse {

    private String jwtToken;
    private String refreshToken;
    private String message;

}
