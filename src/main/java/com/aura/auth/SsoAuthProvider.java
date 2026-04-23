package com.aura.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("sso")
@Slf4j
public class SsoAuthProvider implements AuthProvider{
    @Override
    public void authenticate() {
        log.info("SSO Authentication successful");
    }
}
