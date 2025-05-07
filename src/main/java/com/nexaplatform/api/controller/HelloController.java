package com.nexaplatform.api.controller;

import com.nexaplatform.api.dto.HelloDtoOut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<HelloDtoOut> sayHello() {
        return new ResponseEntity<>(HelloDtoOut.builder()
                .value("Hola mundo")
                .build(), HttpStatus.OK);
    }
}
