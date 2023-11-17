package com.api.bankapirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Solution to JMX error in IntelliJ:
// https://stackoverflow.com/questions/54929656/intellij-idea-not-showing-anything-endpoints-tab-failed-to-retrieve-applicatio

@SpringBootApplication
public class BankApiRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApiRestApplication.class, args);
    }

}
