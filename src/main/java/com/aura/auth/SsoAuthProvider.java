package com.aura.auth;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("sso")
public class SsoAuthProvider implements AuthProvider{
    @Override
    public void authenticate() {
        System.out.println("Auth Success!");
    }
}
