package com.nexaplatform.api.services.mappers;

import com.nexaplatform.api.controllers.services.mappers.UserDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.nexaplatform.providers.user.UserProvider.FULL_NAME_ONE;
import static com.nexaplatform.providers.user.UserProvider.getUserOne;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDtoMapperTest {

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Test
    void getFullNameTest() {
        String fullName = userDtoMapper.getFullName(getUserOne());

        assertNotNull(fullName);
        assertEquals(FULL_NAME_ONE, fullName);
    }

    @Test
    void whenUserIsNullTest() {
        String fullName = userDtoMapper.getFullName(null);

        assertNull(fullName);
    }

}