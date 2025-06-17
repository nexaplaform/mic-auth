package com.nexaplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.nexaplatform", "com.nexaplaform.core"})
public class MicAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicAuthApplication.class, args);
    }
}
