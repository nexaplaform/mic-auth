package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.BaseApi;
import com.nexaplatform.api.controllers.services.dto.in.UserDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.UserDtoOut;
import com.nexaplatform.api.controllers.services.mappers.UserDtoMapper;
import com.nexaplatform.application.UserUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Operations", description = "Operations related to user administration")
public class UserController implements BaseApi<UserDtoIn, UserDtoOut, Long> {

    private final UserDtoMapper mapper;
    private final UserUseCase userUseCase;

    @Override
    public ResponseEntity<UserDtoOut> create(UserDtoIn dto) {
        return new ResponseEntity<>(mapper.toDto(userUseCase.create(mapper.toDomain(dto))), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<UserDtoOut>> getPaginated(Integer page, Integer size, Sort.Direction sort) {
        return new ResponseEntity<>(mapper.toDtoList(userUseCase.getPaginated(page, size, sort)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDtoOut> getById(Long id) {
        return new ResponseEntity<>(mapper.toDto(userUseCase.getById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDtoOut> update(Long id, UserDtoIn dto) {
        return new ResponseEntity<>(mapper.toDto(userUseCase.update(id, mapper.toDomain(dto))), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {userUseCase.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
