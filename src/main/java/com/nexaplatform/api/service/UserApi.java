package com.nexaplatform.api.service;


import com.nexaplatform.api.dto.in.UserDtoIn;
import com.nexaplatform.api.dto.out.UserDtoOut;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
public interface UserApi {

    @PostMapping
    public default ResponseEntity<UserDtoOut> create(@RequestBody UserDtoIn user) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping
    public default ResponseEntity<List<UserDtoOut>> getPaginated(
            @RequestParam Sort.Direction sort,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
