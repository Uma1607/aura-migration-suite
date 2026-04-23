package com.aura.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("google")
@Slf4j
public class GoogleAuthProvider implements AuthProvider{
    @Override
    public void authenticate() {
        log.info("Google Authentication successful");
    }
}
