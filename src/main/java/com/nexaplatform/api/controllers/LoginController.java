package com.nexaplatform.api.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/login")
@Tag(name = "Login operations", description = "Operations related to login.")
public class LoginController {
    
    @GetMapping
    public ResponseEntity<String> login() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
