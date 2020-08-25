package com.eduardomallmann.compasso.technicaltest.utils;

import com.eduardomallmann.compasso.technicaltest.exceptions.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class BuilderTest {

    private String varOne;
    private String varTwo;

    @Test
    public void create_with_build() {
        //when
        BuilderTest testBuilder = Builder.create(BuilderTest.class)
                                          .with(r -> r.varOne = "test1")
                                          .with(r -> r.varTwo = "test2")
                                          .build();
        //then
        assertNotNull(testBuilder);
        assertEquals("test1", testBuilder.varOne);
        assertEquals("test2", testBuilder.varTwo);
    }

    @Test
    public void create_with_parameters() {
        //when
        List<String> list = Builder.create(ArrayList.class, Arrays.asList("test1", "test2")).build();
        //then
        assertNotNull(list);
        assertEquals(2, list.size());
        assertTrue(list.contains("test1"));
        assertTrue(list.contains("test2"));
    }

    @Test
    public void create_with_objects_parameters() {
        //when
        ResponseContent result = Builder.create(ResponseContent.class, HttpStatus.BAD_REQUEST.toString(), new ErrorMessage()).build();
        //then
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST.toString(), result.getStatus());
    }

    @Test
    public void If_endIf() {
        //given
        String test1 = "test1", test2 = "";
        //when
        BuilderTest testBuilder = Builder.create(BuilderTest.class)
                                          .If(() -> !test1.trim().isEmpty())
                                          .with(r -> r.varOne = "test1")
                                          .endIf()
                                          .If(() -> !test2.trim().isEmpty())
                                          .with(r -> r.varTwo = "test2")
                                          .endIf()
                                          .build();
        //then
        assertNotNull(testBuilder);
        assertEquals("test1", testBuilder.varOne);
        assertNull(testBuilder.varTwo);
    }



}