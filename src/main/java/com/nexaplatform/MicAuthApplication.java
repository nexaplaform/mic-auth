package com.nexaplatform;

import com.nexaplaform.core.annotations.EnableCoreServices;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCoreServices
@SpringBootApplication
@OpenAPIDefinition(servers = {
        @Server(url = "https://mic-auth-production.up.railway.app/", description = "Production Server")
})
public class MicAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicAuthApplication.class, args);
    }
}
