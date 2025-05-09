package com.nexaplatform.application;

import com.nexaplatform.domain.models.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserUseCase {

    public User create(User user);

    public List<User> getPaginated(Integer page, Integer size, Sort.Direction sort);
}
