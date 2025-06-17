package com.nexaplatform.api.controllers;

import com.nexaplatform.api.controllers.services.BaseApi;
import com.nexaplatform.api.controllers.services.dto.in.RoleDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.RoleDtoOut;
import com.nexaplatform.api.controllers.services.mappers.RoleDtoMapper;
import com.nexaplatform.application.RoleUserCase;
import com.nexaplatform.domain.utils.ApplicationRole;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/roles")
@Tag(name = "Role operations.", description = "Operations related to role administration.")
public class RoleController implements BaseApi<RoleDtoIn, RoleDtoOut, Long> {

    private final RoleDtoMapper mapper;
    private final RoleUserCase roleUserCase;

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<RoleDtoOut> create(RoleDtoIn dto) {
        return new ResponseEntity<>(mapper.toDtoOut(roleUserCase.create(mapper.toDomain(dto))), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<List<RoleDtoOut>> getPaginated(Integer page, Integer size, Sort.Direction sort) {
        return new ResponseEntity<>(mapper.toDtoOutList(roleUserCase.getPaginated(page, size, sort)), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<RoleDtoOut> getById(Long id) {
        return new ResponseEntity<>(mapper.toDtoOut(roleUserCase.getById(id)), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<RoleDtoOut> update(Long id, RoleDtoIn dto) {
        return new ResponseEntity<>(mapper.toDtoOut(roleUserCase.update(id, mapper.toDomain(dto))), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyAuthority(" + "'" + ApplicationRole.ADMIN + "'" + ")")
    public ResponseEntity<Void> delete(Long id) {
        roleUserCase.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
