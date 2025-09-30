package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplaform.core.exception.EntityNotFoundException;
import com.nexaplatform.domain.models.Group;
import com.nexaplatform.domain.repository.GroupRepository;
import com.nexaplatform.infrastructura.db.postgres.entities.GroupEntity;
import com.nexaplatform.infrastructura.db.postgres.mappers.GroupEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.GroupRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nexaplatform.domain.errors.Error.RECORD_NOT_FOUND;

@Log4j2
@Repository
@Transactional
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupEntityMapper mapper;
    private final GroupRepositoryAdapter repository;

    @Override
    public Group create(Group entity) {
        GroupEntity groupEntity = repository.save(mapper.toEntity(entity));
        return mapper.toModel(groupEntity);
    }

    @Override
    public List<Group> getPaginated(Integer page, Integer size, SortEnumDTO sort) {
        return GroupRepository.super.getPaginated(page, size, sort);
    }

    @Override
    public Group getById(Long id) {
        GroupEntity groupResponse = repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(RECORD_NOT_FOUND.getCode(),
                        String.format(RECORD_NOT_FOUND.getMessage(), id)));
        return mapper.toModel(groupResponse);
    }

    @Override
    public Group update(Long id, Group entity) {
        GroupEntity groupEntity = repository.save(mapper.toEntity(entity));
        return mapper.toModel(groupEntity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ResponseEntity<List<Group>> findAllPaginated(int page, int size, String sort, SortEnumDTO direction) {
        return GroupRepository.super.findAllPaginated(page, size, sort, direction);
    }

    @Override
    public ResponseEntity<List<Group>> filters(Group filterProperties, int page, int size, SortEnumDTO direction, String... sortProperties) {
        return GroupRepository.super.filters(filterProperties, page, size, direction, sortProperties);
    }
}
