package com.contacts.tdddemo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.contacts.tdddemo.exception.NotFoundException;
import com.contacts.tdddemo.service.PhonebookService;
import com.contacts.tdddemo.vo.ErrorResult;
import com.contacts.tdddemo.vo.PhonebookIdResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PhonebookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PhonebookService phonebookService;

    private static final String VALID_ID = "00000000-0000-0000-0000-000000000001";

    private static final String INVALID_ID = "00000000-0000-0000-0000-000000000000";

    @BeforeEach
    public void setUpPhonebookService() {
        doReturn(VALID_ID)
                .when(phonebookService).createPhonebook();
        doNothing()
                .when(phonebookService).deletePhonebook(INVALID_ID);
        doThrow(new NotFoundException("Phonebook with ID " + INVALID_ID + " is not found"))
                .when(phonebookService).deletePhonebook(INVALID_ID);
    }

    @Test
    public void createPhonebook() throws Exception {
        PhonebookIdResult expectedResult = new PhonebookIdResult(VALID_ID);
        mockMvc.perform(post("/phonebooks"))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(phonebookService).createPhonebook();
    }

    @Test
    public void deleteValidPhonebook() throws Exception {
        mockMvc.perform(delete("/phonebook/" + VALID_ID))
               .andExpect(status().isNoContent());
        verify(phonebookService).deletePhonebook(VALID_ID);
    }

    @Test
    public void deleteInvalidPhonebook() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Phonebook with ID " + INVALID_ID + " is not found");
        mockMvc.perform(delete("/phonebook/" + INVALID_ID))
               .andExpect(status().isNotFound())
               .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(phonebookService).deletePhonebook(INVALID_ID);
    }
}
