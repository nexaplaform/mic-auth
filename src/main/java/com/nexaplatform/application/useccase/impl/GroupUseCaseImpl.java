package com.nexaplatform.application.useccase.impl;

import com.nexaplatform.application.populator.GroupHandlerPopulator;
import com.nexaplatform.application.useccase.GroupUseCase;
import com.nexaplatform.domain.models.Group;
import com.nexaplatform.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.nexaplatform.domain.utils.Constant.ID;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupUseCaseImpl implements GroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupHandlerPopulator groupHandlerPopulator;

    @Override
    public Group create(Group entity) {
        groupHandlerPopulator.populate(entity);
        entity.setUniqueName(entity.getUniqueName().toLowerCase());
        return groupRepository.create(entity);
    }

    @Override
    public Group getById(Long id) {
        return groupRepository.getById(id);
    }

    @Override
    public Group update(Long id, Group entity) {
        groupHandlerPopulator.populate(entity);
        entity.setUniqueName(entity.getUniqueName().toLowerCase());
        Group group = this.getById(id);
        BeanUtils.copyProperties(entity, group, ID);
        return groupRepository.update(id, group);
    }

    @Override
    public void delete(Long id) {
        this.getById(id);
        groupRepository.delete(id);
    }
}
