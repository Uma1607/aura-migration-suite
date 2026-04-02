package com.aura.validation;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("tableauvalidator")
public class TableauServerValidator implements BiServerValidator{
    @Override
    public void validate() {
    }
}
