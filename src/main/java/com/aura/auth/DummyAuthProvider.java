package com.aura.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("dummy")
@Slf4j
public class DummyAuthProvider implements AuthProvider{
    @Override
    public void authenticate() {
        log.info("Dummy Authentication successful");
    }
}
