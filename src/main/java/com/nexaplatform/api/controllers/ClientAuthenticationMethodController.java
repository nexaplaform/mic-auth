package com.nexaplatform.api.controllers;

import com.nexaplaform.core.api.configuration.BaseApi;
import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplatform.api.services.dto.in.AuthenticationMethodDtoIn;
import com.nexaplatform.api.services.dto.out.AuthenticationMethodDtoOut;
import com.nexaplatform.api.services.mappers.AuthenticationMethodDtoMapper;
import com.nexaplatform.application.useccase.ClientAuthenticationMethodUseCase;
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
@RequestMapping("/v1/authenticationmethods")
@Tag(name = "Client Authentication Methods Operations.",
        description = "Operations related to Client Authentication Methods.")
public class ClientAuthenticationMethodController implements
        BaseApi<AuthenticationMethodDtoIn, AuthenticationMethodDtoOut, Long> {

    private final AuthenticationMethodDtoMapper mapper;
    private final ClientAuthenticationMethodUseCase useCase;

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<AuthenticationMethodDtoOut> create(AuthenticationMethodDtoIn dto) {
        return new ResponseEntity<>(mapper.toDtoOut(useCase.create(mapper.toDomain(dto))), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<List<AuthenticationMethodDtoOut>>
    getPaginated(Integer page, Integer size, SortEnumDTO sort) {
        return new ResponseEntity<>(mapper.toDtoOutList(useCase.getPaginated(page, size, sort)), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<AuthenticationMethodDtoOut> getById(Long id) {
        return new ResponseEntity<>(mapper.toDtoOut(useCase.getById(id)), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<AuthenticationMethodDtoOut> update(Long id, AuthenticationMethodDtoIn dto) {
        return new ResponseEntity<>(mapper.toDtoOut(useCase.update(id, mapper.toDomain(dto))), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<Void> delete(Long id) {
        useCase.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
