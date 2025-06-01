package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplatform.domain.exception.EntityNotFoundException;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.repository.RoleRepository;
import com.nexaplatform.infrastructura.db.postgres.entities.RoleEntity;
import com.nexaplatform.infrastructura.db.postgres.mappers.RoleEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.RoleRepositoryAdapter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nexaplatform.domain.errors.Error.ROLE_NOT_FOUND;

@Log4j2
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleEntityMapper mapper;
    private final RoleRepositoryAdapter roleRepository;

    @Override
    @Transactional
    public Role create(Role entity) {
        return mapper.toDomain(roleRepository.save(mapper.toEntity(entity)));
    }

    @Override
    public List<Role> getPaginated(Integer page, Integer size, Sort.Direction sort) {
        String sortProperty = "id";
        Sort sortObject  = Sort.by(sort != null? sort : Sort.Direction.ASC, sortProperty);
        Pageable pageable = PageRequest.of(page, size, sortObject);
        Page<RoleEntity> roleEntity = roleRepository.findAll(pageable);
        return mapper.toDomainList(roleEntity.getContent());
    }

    @Override
    public Role getById(Long id) {
        return mapper.toDomain(roleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ROLE_NOT_FOUND.getCode(),
                String.format(ROLE_NOT_FOUND.getMessage(), id))));
    }

    @Override
    @Transactional
    public Role update(Long id, Role entity) {
        Role role = this.getById(id);
        BeanUtils.copyProperties(entity, role, "id");
        return mapper.toDomain(roleRepository.save(mapper.toEntity(role)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.getById(id);
        roleRepository.deleteById(id);
    }
}
