package com.eduardomallmann.compasso.technicaltest.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MessageUtilsIT {

    @Test
    void getMessage() {
        //given
        final String response = "Error listing cities by name";
        //when
        final String result = MessageUtils.getMessage("city.list.name.error");
        //then
        assertEquals(response, result);
    }
}