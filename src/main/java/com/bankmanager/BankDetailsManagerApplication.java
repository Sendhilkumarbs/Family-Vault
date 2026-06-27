package com.bankmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankDetailsManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankDetailsManagerApplication.class, args);
    }
}
