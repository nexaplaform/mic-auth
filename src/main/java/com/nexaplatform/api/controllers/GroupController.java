package com.nexaplatform.api.controllers;

import com.nexaplaform.core.api.configuration.BaseApi;
import com.nexaplatform.api.services.dto.in.GroupDtoIn;
import com.nexaplatform.api.services.dto.out.GroupDtoOut;
import com.nexaplatform.api.services.mappers.GroupDtoMapper;
import com.nexaplatform.application.useccase.GroupUseCase;
import com.nexaplatform.domain.models.Group;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/groups")
@Tag(name = "Group operations.", description = "Operations related to group administration.")
public class GroupController implements BaseApi<GroupDtoIn, GroupDtoOut, Long> {

    private final GroupDtoMapper mapper;
    private final GroupUseCase groupUseCase;

    @Override
    public ResponseEntity<GroupDtoOut> create(GroupDtoIn dto) {
        Group group = groupUseCase.create(mapper.toModel(dto));
        return new ResponseEntity<>(mapper.toDto(group), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<GroupDtoOut> getById(Long id) {
        return new ResponseEntity<>(mapper.toDto(groupUseCase.getById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GroupDtoOut> update(@Valid Long id, GroupDtoIn dto) {
        Group group = groupUseCase.update(id, mapper.toModel(dto));
        return new ResponseEntity<>(mapper.toDto(group), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        groupUseCase.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
