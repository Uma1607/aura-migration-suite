package com.aura.auth;

import org.springframework.stereotype.Service;

@Service("dummy")
public class DummyAuthProvider implements AuthProvider{
    @Override
    public void authenticate() {
        System.out.println("Auth Success!");
    }
}
