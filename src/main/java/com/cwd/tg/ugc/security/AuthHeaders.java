package com.cwd.tg.ugc.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthHeaders {
    private String requestId;
    private String authToken;
}
