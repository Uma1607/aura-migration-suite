package com.aura.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("mysql")
@Qualifier("mysql")
public class MySqlRepository implements PersistenceRepository{
    @Override
    public void save() {

    }
}
