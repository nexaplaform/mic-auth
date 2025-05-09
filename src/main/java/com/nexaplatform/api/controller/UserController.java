package com.nexaplatform.api.controller;

import com.nexaplatform.api.dto.in.UserDtoIn;
import com.nexaplatform.api.dto.out.UserDtoOut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @PostMapping
    public ResponseEntity<UserDtoOut> create(@RequestBody UserDtoIn user) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
