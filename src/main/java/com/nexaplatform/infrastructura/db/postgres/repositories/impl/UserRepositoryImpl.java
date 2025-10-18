package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplaform.core.exception.EntityNotFoundException;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.UserRepository;
import com.nexaplatform.infrastructura.db.postgres.entities.UserEntity;
import com.nexaplatform.infrastructura.db.postgres.mappers.UserEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.UserRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nexaplatform.domain.errors.Error.USER_NOT_FOUND;

@Log4j2
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserEntityMapper uMapper;
    private final UserRepositoryAdapter uRepository;

    @Override
    public User create(User user) {
        return uMapper.toDomain(uRepository.save(uMapper.toEntity(user)));
    }

    @Override
    public List<User> getPaginated(Integer page, Integer size, SortEnumDTO sort) {
        String sortProperty = "id";
        Sort sortObject = Sort.by(sort.toString(), sortProperty);
        Pageable pageable = PageRequest.of(page, size, sortObject);
        Page<UserEntity> userEntity = uRepository.findAll(pageable);
        return uMapper.toDomainList(userEntity.getContent());
    }

    @Override
    public List<User> findAll() {
        return uMapper.toDomainList(uRepository.findAll());
    }

    @Override
    public User getById(Long id) {
        return uMapper.toDomain(uRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND.getCode(),
                        String.format(USER_NOT_FOUND.getMessage(), id))));
    }

    @Override
    public User update(Long id, User user) {
        User userResponse = this.getById(id);
        user.setStatus(userResponse.getStatus());
        BeanUtils.copyProperties(user, userResponse, "id");
        return uMapper.toDomain(uRepository.save(uMapper.toEntity(userResponse)));
    }

    @Override
    public void delete(Long id) {
        this.getById(id);
        uRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity userEntity = uRepository.findByEmail(email);
        log.info("User name: {}", email);
        return uMapper.toDomain(userEntity);
    }
}
