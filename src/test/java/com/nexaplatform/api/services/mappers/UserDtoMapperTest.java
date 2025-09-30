package com.nexaplatform.api.services.mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.nexaplatform.providers.user.UserProvider.FULL_NAME_ONE;
import static com.nexaplatform.providers.user.UserProvider.getUserOne;
import static org.junit.jupiter.api.Assertions.*;

class UserDtoMapperTest {

    private final UserDtoMapper userDtoMapper = Mappers.getMapper(UserDtoMapper.class);

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