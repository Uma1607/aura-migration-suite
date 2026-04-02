package com.aura.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("postgres")
@Qualifier("postgres")
public class PostgresRepository implements PersistenceRepository{
    @Override
    public void save() {

    }
}
