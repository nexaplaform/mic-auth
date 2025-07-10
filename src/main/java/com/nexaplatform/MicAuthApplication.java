package com.nexaplatform;

import com.nexaplaform.core.annotations.EnableCoreServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCoreServices
@SpringBootApplication
public class MicAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicAuthApplication.class, args);
    }
}
