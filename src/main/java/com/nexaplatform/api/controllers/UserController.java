package com.nexaplatform.api.controllers;

import com.nexaplaform.core.api.configuration.BaseApi;
import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplatform.api.services.dto.in.UserDtoIn;
import com.nexaplatform.api.services.dto.out.UserDtoOut;
import com.nexaplatform.api.services.mappers.UserDtoMapper;
import com.nexaplatform.application.useccase.UserUseCase;
import com.nexaplatform.domain.utils.ApplicationRole;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
/* @CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.OPTIONS}) */
@Tag(name = "User Operations.", description = "Operations related to user administration.")
public class UserController implements BaseApi<UserDtoIn, UserDtoOut, Long> {

    private final UserDtoMapper mapper;
    private final UserUseCase userUseCase;

    @Override
    public ResponseEntity<UserDtoOut> create(UserDtoIn dto) {
        return new ResponseEntity<>(mapper.toDto(userUseCase.create(mapper.toDomain(dto))), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<List<UserDtoOut>> getPaginated(Integer page, Integer size, SortEnumDTO sort) {
        return new ResponseEntity<>(mapper.toDtoList(userUseCase.getPaginated(page, size, SortEnumDTO.ASC)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDtoOut> getById(Long id) {
        return new ResponseEntity<>(mapper.toDto(userUseCase.getById(id)), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<UserDtoOut> update(Long id, UserDtoIn dto) {
        return new ResponseEntity<>(mapper.toDto(userUseCase.update(id, mapper.toDomain(dto))), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<Void> delete(Long id) {
        userUseCase.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
