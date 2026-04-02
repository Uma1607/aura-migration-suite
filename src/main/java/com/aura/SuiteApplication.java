package com.aura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.aura")
@EnableAsync
public class SuiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuiteApplication.class, args);
    }

}
