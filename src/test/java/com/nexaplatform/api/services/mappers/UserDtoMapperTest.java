package com.nexaplatform.api.services.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.nexaplatform.providers.user.UserProvider.FULL_NAME;
import static com.nexaplatform.providers.user.UserProvider.getUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDtoMapperTest {

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Test
    void getFullNameTest() {
        String fullName = userDtoMapper.getFullName(getUser());

        assertNotNull(fullName);
        assertEquals(FULL_NAME, fullName);
    }

    @Test
    void whenUserIsNullTest() {
        String fullName = userDtoMapper.getFullName(null);

        assertNull(fullName);
    }

}