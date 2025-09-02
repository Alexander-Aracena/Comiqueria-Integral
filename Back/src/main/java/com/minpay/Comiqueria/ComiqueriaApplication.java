package com.minpay.Comiqueria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ComiqueriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComiqueriaApplication.class, args);
	}

}
