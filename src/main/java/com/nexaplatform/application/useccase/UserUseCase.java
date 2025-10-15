package com.nexaplatform.application.useccase;

import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.BaseRepository;

public interface UserUseCase extends BaseRepository<User, Long> {

    User findUserByEmail(String email);
}
