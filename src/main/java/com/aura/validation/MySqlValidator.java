package com.aura.validation;

import org.springframework.stereotype.Component;

@Component("mysqlvalidator")
public class MySqlValidator implements ConnectionValidator{
    @Override
    public void validate() {
    }
}
