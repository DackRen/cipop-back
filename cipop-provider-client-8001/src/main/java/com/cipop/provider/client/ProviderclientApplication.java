package com.cipop.provider.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cipop.bean", "com.cipop.provider.client"})
public class ProviderclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderclientApplication.class, args);
	}
}
