package com.contacts.tdddemo.controller;

import com.contacts.tdddemo.exception.NotFoundException;
import com.contacts.tdddemo.model.Contact;
import com.contacts.tdddemo.service.ContactService;
import com.contacts.tdddemo.service.PhonebookService;
import com.contacts.tdddemo.vo.ContactIdResult;
import com.contacts.tdddemo.vo.ContactItem;
import com.contacts.tdddemo.vo.ContactList;
import com.contacts.tdddemo.vo.ErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PhonebookService phonebookService;

    @MockBean
    private ContactService contactService;

    public static final List<ContactItem> CONTACT_ITEMS = Arrays.asList(
            new ContactItem("00000000-0000-0000-0000-000000000000", "Donald", "Trump"),
            new ContactItem("00000000-0000-0000-0000-000000000001", "Mikoto", "Misaka"),
            new ContactItem("00000000-0000-0000-0000-000000000002", "翰林", "杨")
    );

    public static final Contact VALID_CONTACT = Contact.builder()
            .firstName("Touma").lastName("Kamijou")
            .phone("+8613812345678")
            .email("t.kamijou@academy.city")
            .address("A Certain High Scholl, District 7, Academy City")
            .build();

    public static final List<Contact> INVALID_CONTACTS = Arrays.asList(
            VALID_CONTACT.toBuilder().firstName(null).lastName(null).build(),
            VALID_CONTACT.toBuilder().firstName("  ").lastName("  ").build(),
            VALID_CONTACT.toBuilder().phone(null).build(),
            VALID_CONTACT.toBuilder().phone("invalid phone number").build(),
            VALID_CONTACT.toBuilder().email("www.kamijoutouma.com").build()
    );

    public static final String EXISTENT_ID = "00000000-0000-0000-0000-000000000002";

    public static final String NONEXISTENT_ID = "00000000-0000-0000-0000-000000000003";

    @BeforeEach
    void setUpContactService() {
        // Mocks for listContacts()
        doReturn(CONTACT_ITEMS)
                .when(contactService).listContacts(PhonebookControllerTest.EXISTENT_ID);
        doThrow(new NotFoundException("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found"))
                .when(contactService).listContacts(PhonebookControllerTest.NONEXISTENT_ID);

        // Mocks for getContactById()
        doReturn(VALID_CONTACT)
                .when(contactService).getContactById(PhonebookControllerTest.EXISTENT_ID, EXISTENT_ID);
        doThrow(new NotFoundException("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found"))
                .when(contactService).getContactById(eq(PhonebookControllerTest.NONEXISTENT_ID), any());
        doThrow(new NotFoundException("Contact with ID " + NONEXISTENT_ID + " is not found"))
                .when(contactService).getContactById(PhonebookControllerTest.EXISTENT_ID, NONEXISTENT_ID);

        // Mocks for createContact()
        doReturn(NONEXISTENT_ID)
                .when(contactService).createContact(eq(PhonebookControllerTest.EXISTENT_ID), any());
        doThrow(new NotFoundException("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found"))
                .when(contactService).createContact(eq(PhonebookControllerTest.NONEXISTENT_ID), any());

        // Mocks for updateContact()
        doNothing()
                .when(contactService).updateContact(eq(PhonebookControllerTest.EXISTENT_ID), eq(EXISTENT_ID), any());
        doThrow(new NotFoundException("Contact with ID " + NONEXISTENT_ID + " is not found"))
                .when(contactService).updateContact(eq(PhonebookControllerTest.EXISTENT_ID), eq(NONEXISTENT_ID), any());
        doThrow(new NotFoundException("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found"))
                .when(contactService).updateContact(eq(PhonebookControllerTest.NONEXISTENT_ID), any(), any());

        // Mocks for deleteContact()
        doNothing()
                .when(contactService).deleteContact(PhonebookControllerTest.EXISTENT_ID, EXISTENT_ID);
        doThrow(new NotFoundException("Contact with ID " + NONEXISTENT_ID + " is not found"))
                .when(contactService).deleteContact(PhonebookControllerTest.EXISTENT_ID, NONEXISTENT_ID);
        doThrow(new NotFoundException("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found"))
                .when(contactService).deleteContact(eq(PhonebookControllerTest.NONEXISTENT_ID), any());
    }

    @Test
    void listValidPhonebookContacts() throws Exception {
        ContactList expectedResult = new ContactList(CONTACT_ITEMS);
        mockMvc.perform(get("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contacts"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).listContacts(PhonebookControllerTest.EXISTENT_ID);
    }

    @Test
    void listInvalidPhonebookContacts() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found");
        mockMvc.perform(get("/phonebook/" + PhonebookControllerTest.NONEXISTENT_ID + "/contacts"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).listContacts(PhonebookControllerTest.NONEXISTENT_ID);
    }

    @Test
    void getContactById() throws Exception {
        mockMvc.perform(get("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contact/" + EXISTENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VALID_CONTACT)));
        verify(contactService).getContactById(PhonebookControllerTest.EXISTENT_ID, EXISTENT_ID);
    }

    @Test
    void getContactByInvalidPid() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found");
        mockMvc.perform(get("/phonebook/" + PhonebookControllerTest.NONEXISTENT_ID + "/contact/" + EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).getContactById(PhonebookControllerTest.NONEXISTENT_ID, EXISTENT_ID);
    }

    @Test
    void getContactByInvalidCid() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Contact with ID " + NONEXISTENT_ID + " is not found");
        mockMvc.perform(get("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contact/" + NONEXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).getContactById(PhonebookControllerTest.EXISTENT_ID, NONEXISTENT_ID);
    }

    @Test
    void createValidContact() throws Exception {
        ContactIdResult expectedResult = new ContactIdResult(NONEXISTENT_ID);
        mockMvc.perform(
                post("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contacts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(VALID_CONTACT))
        )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).createContact(PhonebookControllerTest.EXISTENT_ID, VALID_CONTACT);
    }

    @Test
    void createValidContactWithInvalidPid() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found");
        mockMvc.perform(
                post("/phonebook/" + PhonebookControllerTest.NONEXISTENT_ID + "/contacts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(VALID_CONTACT))
        )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).createContact(PhonebookControllerTest.NONEXISTENT_ID, VALID_CONTACT);
    }

    @Test
    void createInvalidContact() throws Exception {
        for (Contact contact : INVALID_CONTACTS) {
            mockMvc.perform(
                    post("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact))
            )
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void updateValidContact() throws Exception {
        mockMvc.perform(
                put("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contact/" + EXISTENT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(VALID_CONTACT))
        )
                .andExpect(status().isNoContent());
        verify(contactService).updateContact(PhonebookControllerTest.EXISTENT_ID, EXISTENT_ID, VALID_CONTACT);
    }

    @Test
    void updateValidContactWithInvalidPid() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found");
        mockMvc.perform(
                put("/phonebook/" + PhonebookControllerTest.NONEXISTENT_ID + "/contact/" + EXISTENT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(VALID_CONTACT))
        )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).updateContact(PhonebookControllerTest.NONEXISTENT_ID, EXISTENT_ID, VALID_CONTACT);
    }

    @Test
    void updateValidContactWithInvalidCid() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Contact with ID " + NONEXISTENT_ID + " is not found");
        mockMvc.perform(
                put("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contact/" + NONEXISTENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(VALID_CONTACT))
        )
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).updateContact(PhonebookControllerTest.EXISTENT_ID, NONEXISTENT_ID, VALID_CONTACT);
    }

    @Test
    void updateInvalidContact() throws Exception {
        for (Contact contact : INVALID_CONTACTS) {
            mockMvc.perform(
                    put("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contact/" + EXISTENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact))
            )
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void deleteContact() throws Exception {
        mockMvc.perform(delete("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contact/" + EXISTENT_ID))
                .andExpect(status().isNoContent());
        verify(contactService).deleteContact(PhonebookControllerTest.EXISTENT_ID, EXISTENT_ID);
    }

    @Test
    void deleteContactWithInvalidPid() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Phonebook with ID " + PhonebookControllerTest.NONEXISTENT_ID + " is not found");
        mockMvc.perform(delete("/phonebook/" + PhonebookControllerTest.NONEXISTENT_ID + "/contact/" + EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).deleteContact(PhonebookControllerTest.NONEXISTENT_ID, EXISTENT_ID);
    }

    @Test
    void deleteContactWithInvalidCid() throws Exception {
        ErrorResult expectedResult = new ErrorResult("Contact with ID " + NONEXISTENT_ID + " is not found");
        mockMvc.perform(delete("/phonebook/" + PhonebookControllerTest.EXISTENT_ID + "/contact/" + NONEXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResult)));
        verify(contactService).deleteContact(PhonebookControllerTest.EXISTENT_ID, NONEXISTENT_ID);
    }
}
