package com.raulcg.blog;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupResponse {

    private String jwtToken;
    private String refreshToken;
    private String message;

}
