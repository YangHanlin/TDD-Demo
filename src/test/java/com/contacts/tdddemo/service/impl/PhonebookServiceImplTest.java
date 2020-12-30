package com.contacts.tdddemo.service.impl;

import com.contacts.tdddemo.exception.NotFoundException;
import com.contacts.tdddemo.model.Phonebook;
import com.contacts.tdddemo.repository.ContactRepository;
import com.contacts.tdddemo.repository.PhonebookRepository;
import com.contacts.tdddemo.service.PhonebookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PhonebookServiceImplTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PhonebookRepository phonebookRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PhonebookService phonebookService;

    public static final String EXISTENT_ID = "00000000-0000-0000-0000-000000000001";

    public static final String NONEXISTENT_ID = "00000000-0000-0000-0000-000000000000";

    public static final Phonebook EXISTENT_PHONEBOOK = new Phonebook(EXISTENT_ID, new ArrayList<>());

    @BeforeEach
    void setUp() {
        phonebookRepository.save(EXISTENT_PHONEBOOK);
    }

    @AfterEach
    void tearDown() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushDb();
    }

    @Test
    void createPhonebook() {
        String id = phonebookService.createPhonebook();
        Phonebook expectedPhonebook = new Phonebook(id, new ArrayList<>());
        Optional<Phonebook> phonebook = phonebookRepository.findById(id);
        assertTrue(phonebook.isPresent());
        assertEquals(expectedPhonebook, phonebook.get());
    }

    @Test
    void deletePhonebook() {
        Phonebook phonebook = phonebookRepository.findById(EXISTENT_ID).get();
        phonebookService.deletePhonebook(EXISTENT_ID);
        assertFalse(phonebookRepository.existsById(EXISTENT_ID));
        for (String cid : phonebook.getContactIds()) {
            assertFalse(contactRepository.existsById(cid));
        }
    }

    @Test
    void deletePhonebookWithInvalidId() {
        Exception e = assertThrows(NotFoundException.class, () -> phonebookService.deletePhonebook(NONEXISTENT_ID));
        assertEquals("Phonebook with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }
}