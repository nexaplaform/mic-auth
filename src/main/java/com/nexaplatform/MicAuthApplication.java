package com.nexaplatform;

import com.nexaplaform.core.api.configuration.OpenApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({OpenApiConfig.class})
public class MicAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicAuthApplication.class, args);
    }

}
