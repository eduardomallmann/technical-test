package com.eduardomallmann.compasso.technicaltest.utils;

import com.eduardomallmann.compasso.technicaltest.exceptions.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
class ResponseTest {

    private ResponseContent responseContent1;
    private ResponseContent responseContent2;

    @BeforeEach
    public void setUp() throws Exception {
        //given
        this.responseContent1 = ResponseContent.builder().status(HttpStatus.BAD_REQUEST.toString()).errorMessage(new ErrorMessage()).build();
        this.responseContent2 = ResponseContent.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.toString()).errorMessage(new ErrorMessage()).build();
    }

    @Test
    public void builder_ShouldCreateAnInstanceWithBuilder() {
        //given
        List<ResponseContent> responseContentList = Arrays.asList(responseContent1, responseContent2);
        //when
        Response response = Response.builder()
                                    .content(responseContentList)
                                    .numberOfElements(responseContentList.size())
                                    .pageNumber(0)
                                    .pageSize(responseContentList.size())
                                    .totalElements(responseContentList.size())
                                    .totalPages(1)
                                    .first(true)
                                    .last(true)
                                    .empty(false)
                                    .build();
        //then
        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
        assertTrue(response.getContent().contains(this.responseContent1));
        assertTrue(response.getContent().contains(this.responseContent2));
        assertEquals(2, response.getNumberOfElements());
        assertEquals(2, response.getPageSize());
        assertEquals(0, response.getPageNumber());
        assertEquals(1, response.getTotalPages());
        assertEquals(2, response.getTotalElements());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertFalse(response.isEmpty());
    }

    @Test
    public void builder_ShouldCreateAnInstanceWithBuilderAndOneObjectToContent() {
        //when
        Response response = Response.builder()
                                    .content(this.responseContent1)
                                    .numberOfElements(1)
                                    .pageNumber(0)
                                    .pageSize(1)
                                    .totalElements(1)
                                    .totalPages(1)
                                    .first(true)
                                    .last(true)
                                    .empty(false)
                                    .build();
        //then
        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
        assertTrue(response.getContent().contains(this.responseContent1));
        assertEquals(1, response.getNumberOfElements());
        assertEquals(1, response.getPageSize());
        assertEquals(0, response.getPageNumber());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertFalse(response.isEmpty());
    }

    @Test
    public void of_ShouldCreateAnInstanceWithAList() {
        //when
        Response<ResponseContent> response = Response.of(Arrays.asList(responseContent1, responseContent2));
        //then
        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
        assertTrue(response.getContent().contains(this.responseContent1));
        assertTrue(response.getContent().contains(this.responseContent2));
        assertEquals(2, response.getNumberOfElements());
        assertEquals(2, response.getPageSize());
        assertEquals(0, response.getPageNumber());
        assertEquals(1, response.getTotalPages());
        assertEquals(2, response.getTotalElements());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertFalse(response.isEmpty());
    }

    @Test
    public void of_ShouldCreateAnInstanceWithAnObject() {
        //when
        Response<ResponseContent> response = Response.of(this.responseContent1);
        //then
        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
        assertTrue(response.getContent().contains(this.responseContent1));
        assertEquals(1, response.getNumberOfElements());
        assertEquals(1, response.getPageSize());
        assertEquals(0, response.getPageNumber());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertFalse(response.isEmpty());
    }

    @Test
    public void of_ShouldCreateAnInstanceWithAPage() {
        //given
        List<ResponseContent> content = Arrays.asList(this.responseContent1, this.responseContent2);
        Page<ResponseContent> pageContent = new PageImpl<>(content, PageRequest.of(0, content.size()), content.size());
        //when
        Response<ResponseContent> response = Response.of(pageContent);
        //then
        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
        assertTrue(response.getContent().contains(this.responseContent1));
        assertTrue(response.getContent().contains(this.responseContent2));
        assertEquals(2, response.getNumberOfElements());
        assertEquals(2, response.getPageSize());
        assertEquals(0, response.getPageNumber());
        assertEquals(1, response.getTotalPages());
        assertEquals(2, response.getTotalElements());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertFalse(response.isEmpty());
    }
}