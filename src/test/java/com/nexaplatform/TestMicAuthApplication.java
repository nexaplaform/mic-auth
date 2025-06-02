package com.nexaplatform;

import org.springframework.boot.SpringApplication;

public class TestMicAuthApplication {

	public static void main(String[] args) {
		SpringApplication.from(MicAuthApplication::main).with(TestContainersConfiguration.class).run(args);
	}

}
