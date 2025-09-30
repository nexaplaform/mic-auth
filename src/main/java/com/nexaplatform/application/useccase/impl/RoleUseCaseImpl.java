package com.nexaplatform.application.useccase.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplatform.application.useccase.RoleUserCase;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoleUseCaseImpl implements RoleUserCase {

    private final RoleRepository roleRepository;

    @Override
    public Role create(Role entity) {
        return roleRepository.create(entity);
    }

    @Override
    public List<Role> getPaginated(Integer page, Integer size, SortEnumDTO sort) {
        return roleRepository.getPaginated(page, size, sort);
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.getById(id);
    }

    @Override
    public Role update(Long id, Role entity) {
        return roleRepository.update(id, entity);
    }

    @Override
    public void delete(Long id) {
        roleRepository.delete(id);
    }
}
