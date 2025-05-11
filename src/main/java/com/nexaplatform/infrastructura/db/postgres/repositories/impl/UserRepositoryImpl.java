package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplatform.domain.exception.EntityNotFoundException;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.UserRepository;
import com.nexaplatform.infrastructura.db.postgres.mappers.UserEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.UserRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nexaplatform.domain.errors.Error.USER_NOT_FOUND;
import static com.nexaplatform.domain.exception.CodeError.ERROR_CODE_NOT_FOUND;

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
    public List<User> getPaginated(Integer page, Integer size, Sort.Direction sort) {
        return uMapper.toDomainList(uRepository.findAll());
    }

    @Override
    public User getById(Long id) {
        return uMapper.toDomain(uRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ERROR_CODE_NOT_FOUND,
                        String.format(USER_NOT_FOUND.getValue(), id))));
    }

    @Override
    public User update(Long id, User user) {
        User userResponse = this.getById(id);
        user.setId(id);
        user.setStatus(userResponse.getStatus());
        BeanUtils.copyProperties(user, userResponse);
        return uMapper.toDomain(uRepository.save(uMapper.toEntity(userResponse)));
    }

    @Override
    public void delete(Long id) {
        this.getById(id);
        uRepository.deleteById(id);
    }
}
