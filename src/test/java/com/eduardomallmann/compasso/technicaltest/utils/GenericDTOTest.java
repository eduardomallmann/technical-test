package com.eduardomallmann.compasso.technicaltest.utils;

import com.eduardomallmann.compasso.technicaltest.domains.city.CityDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class GenericDTOTest {

    @Test
    public void normalizeField_ShouldReturnStringNormalized() {
        //given
        final String name = "Juliana dos Santos";
        GenericDTO genericDTO = new GenericDTO();
        //when
        String result = genericDTO.normalizeField(name.toLowerCase());
        //then
        assertEquals(name, result);
    }

    @Test
    public void toJson_ShouldReturnJsonFormattedString() {
        //given
        final String response = "{\"city\":\"Florianópolis\",\"state\":\"Santa Catarina\"}";
        CityDTO city = new CityDTO("Florianópolis", "Santa Catarina");
        //when
        final String result = city.toJson();
        //then
        assertEquals(response, result);
    }

}