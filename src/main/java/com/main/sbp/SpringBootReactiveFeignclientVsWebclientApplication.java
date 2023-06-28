package com.main.sbp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringBootReactiveFeignclientVsWebclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactiveFeignclientVsWebclientApplication.class, args);
	}

}
