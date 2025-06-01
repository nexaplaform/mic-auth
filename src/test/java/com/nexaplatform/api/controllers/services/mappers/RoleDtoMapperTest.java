package com.nexaplatform.api.controllers.services.mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class RoleDtoMapperTest {

    private final RoleDtoMapper roleDtoMapper = Mappers.getMapper(RoleDtoMapper.class);

    @Test
    void toUpperCase_OK() {

        String rolaName = "admin";

        String response = roleDtoMapper.toUpperCase(rolaName);

        assertNotNull(response);
        assertEquals(rolaName.toUpperCase(), response);
    }

    @Test
    void toUpperCase_Role_Name_null() {

        String rolaName = null;

        String response = roleDtoMapper.toUpperCase(rolaName);
        assertNull(response);
    }

}