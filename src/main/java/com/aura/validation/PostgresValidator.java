package com.aura.validation;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("postgresvalidator")
public class PostgresValidator implements ConnectionValidator{
    @Override
    public void validate() {

    }
}
