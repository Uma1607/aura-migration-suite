package com.aura.auth;

import org.springframework.stereotype.Service;

@Service("google")
public class GoogleAuthProvider implements AuthProvider{
    @Override
    public void authenticate() {
        System.out.println("Auth Success!");
    }
}
