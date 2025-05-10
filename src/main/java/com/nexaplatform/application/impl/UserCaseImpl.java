package com.nexaplatform.application.impl;

import com.nexaplatform.application.UserUseCase;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserCaseImpl implements UserUseCase {

    private final UserRepository uRepository;

    @Override
    public User create(User user) {
        user.setInitialStatus();
        return uRepository.create(user);
    }

    @Override
    public List<User> getPaginated(Integer page, Integer size, Sort.Direction sort) {
        return uRepository.getPaginated(page, size, sort);
    }

    @Override
    public User getById(Long id) {
        return uRepository.getById(id);
    }

    @Override
    public User update(Long id, User user) {
        return uRepository.update(id, user);
    }

    @Override
    public void delete(Long id) {
        uRepository.delete(id);
    }
}
