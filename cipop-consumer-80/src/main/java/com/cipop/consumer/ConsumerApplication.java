package com.cipop.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cipop.bean", "com.cipop.consumer"})
public class ConsumerApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.default", "dev");
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
