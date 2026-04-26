package com.aura.repository;

import org.springframework.stereotype.Service;

@Service("postgres")
public class PostgresRepository implements PersistenceRepository{
    @Override
    public void save() {
    }
}
