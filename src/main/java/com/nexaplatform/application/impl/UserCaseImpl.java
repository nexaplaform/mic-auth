package com.nexaplatform.application.impl;

import com.nexaplatform.application.UserUseCase;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.models.UserStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class UserCaseImpl implements UserUseCase {

    @Override
    public User create(User user) {
        return User.builder()
                .id(1L)
                .firstName("Jesus")
                .lastName("Finol")
                .email("jfinol02@gmail.com")
                .password("123456789")
                .phoneNumber("5 5555 5555")
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Override
    public List<User> getPaginated(Integer page, Integer size, Sort.Direction sort) {
        return List.of(User.builder()
                .id(1L)
                .firstName("Jesus")
                .lastName("Finol")
                .email("jfinol02@gmail.com")
                .password("123456789")
                .phoneNumber("5 5555 5555")
                .status(UserStatus.INACTIVE)
                .build());
    }
}
