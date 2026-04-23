package com.aura.repository;

import org.springframework.stereotype.Service;

@Service("mysql")
public class MySqlRepository implements PersistenceRepository{
    @Override
    public void save() {
    }
}
