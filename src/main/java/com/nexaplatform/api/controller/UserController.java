package com.nexaplatform.api.controller;

import com.nexaplatform.api.dto.in.UserDtoIn;
import com.nexaplatform.api.dto.out.UserDtoOut;
import com.nexaplatform.api.service.UserApi;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController implements UserApi {

    @Override
    public ResponseEntity<UserDtoOut> create(UserDtoIn user) {
        return UserApi.super.create(user);
    }

    @Override
    public ResponseEntity<List<UserDtoOut>> getPaginated(Sort.Direction sort, Integer page, Integer size) {
        return UserApi.super.getPaginated(sort, page, size);
    }
}
