package com.contacts.tdddemo.service.impl;

import com.contacts.tdddemo.exception.NotFoundException;
import com.contacts.tdddemo.model.Contact;
import com.contacts.tdddemo.model.Phonebook;
import com.contacts.tdddemo.repository.ContactRepository;
import com.contacts.tdddemo.repository.PhonebookRepository;
import com.contacts.tdddemo.vo.ContactItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test requires a test server up and running and configured in active profile.
 */

@SpringBootTest
class ContactServiceImplTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PhonebookRepository phonebookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ContactServiceImpl contactService;

    public static final List<Contact> INITIAL_CONTACTS = Arrays.asList(
            Contact.builder()
                    .firstName("Mikoto").lastName("Misaka").phone("13012345678")
                    .id("00000000-0000-0000-0000-000000000001")
                    .build(),
            Contact.builder()
                    .firstName("Touma").lastName("Kamijou").phone("13987654321")
                    .id("00000000-0000-0000-0000-000000000002")
                    .build(),
            Contact.builder()
                    .firstName("翰林").lastName("杨").phone("10086").email("hi@tree-diagram.site")
                    .id("00000000-0000-0000-0000-000000000003")
                    .build()
    );

    public static final Contact VALID_CONTACT = Contact.builder()
            .firstName("Donald").lastName("Trump")
            .phone("+1-1234567")
            .email("donaldtrump@donaldtrump.com")
            .build();

    public static final String EXISTENT_PID = "00000000-0000-0000-0000-000000000001";

    public static final String NONEXISTENT_ID = "00000000-0000-0000-0000-000000000000";

    @BeforeEach
    void setUp() {
        // Initialize the only phonebook
        List<String> contactIds = new ArrayList<>();
        INITIAL_CONTACTS.forEach(contact -> contactIds.add(contact.getId()));
        Phonebook phonebook = new Phonebook(EXISTENT_PID, contactIds);
        phonebookRepository.save(phonebook);

        // Initialize contacts
        INITIAL_CONTACTS.forEach(contactRepository::save);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.getMongoDbFactory().getMongoDatabase().drop();
    }

    @Test
    void listContacts() {
        List<ContactItem> contacts = contactService.listContacts(EXISTENT_PID);
        assertEquals(INITIAL_CONTACTS.size(), contacts.size());
        for (int i = 0; i < contacts.size(); ++i) {
            Contact expectedContact = INITIAL_CONTACTS.get(i);
            ContactItem contact = contacts.get(i);
            assertEquals(expectedContact.getId(), contact.getId());
            assertEquals(expectedContact.getFirstName(), contact.getFirstName());
            assertEquals(expectedContact.getLastName(), contact.getLastName());
        }
    }

    @Test
    void listContactsWithInvalidPid() {
        Exception e = assertThrows(NotFoundException.class, () -> contactService.listContacts(NONEXISTENT_ID));
        assertEquals("Phonebook with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }

    @Test
    void getContactById() {
        Contact expectedContact = INITIAL_CONTACTS.get(0);
        Contact contact = contactService.getContactById(EXISTENT_PID, expectedContact.getId());
        assertNotNull(contact);
        assertEquals(expectedContact, contact);
    }

    @Test
    void getContactByInvalidPid() {
        Contact expectedContact = INITIAL_CONTACTS.get(0);
        Exception e = assertThrows(NotFoundException.class, () -> contactService.getContactById(NONEXISTENT_ID, expectedContact.getId()));
        assertEquals("Phonebook with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }

    @Test
    void getContactByInvalidCid() {
        Exception e = assertThrows(NotFoundException.class, () -> contactService.getContactById(EXISTENT_PID, NONEXISTENT_ID));
        assertEquals("Contact with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }

    @Test
    void createContact() {
        String id = contactService.createContact(EXISTENT_PID, VALID_CONTACT);
        Optional<Contact> contact = contactRepository.findById(id);
        assertTrue(contact.isPresent());
        assertEquals(VALID_CONTACT, contact.get());
        Optional<Phonebook> phonebook = phonebookRepository.findById(EXISTENT_PID);
        assertTrue(phonebook.isPresent());
        assertTrue(phonebook.get().getContactIds().contains(id));
    }

    @Test
    void createContactWithInvalidPid() {
        Exception e = assertThrows(NotFoundException.class, () -> contactService.createContact(NONEXISTENT_ID, VALID_CONTACT));
        assertEquals("Phonebook with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }

    @Test
    void updateContact() {
        Contact expectedContact = INITIAL_CONTACTS.get(0);
        expectedContact.setEmail("m.misaka@academy.city");
        contactService.updateContact(EXISTENT_PID, expectedContact.getId(), expectedContact);
        Optional<Contact> contact = contactRepository.findById(expectedContact.getId());
        assertTrue(contact.isPresent());
        assertEquals(expectedContact, contact.get());
    }

    @Test
    void updateContactWithInvalidPid() {
        Contact expectedContact = INITIAL_CONTACTS.get(0);
        expectedContact.setEmail("m.misaka@academy.city");
        Exception e = assertThrows(NotFoundException.class, () -> contactService.updateContact(NONEXISTENT_ID, expectedContact.getId(), expectedContact));
        assertEquals("Phonebook with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }

    @Test
    void updateContactWithInvalidCid() {
        Contact expectedContact = INITIAL_CONTACTS.get(0);
        expectedContact.setEmail("m.misaka@academy.city");
        Exception e = assertThrows(NotFoundException.class, () -> contactService.updateContact(EXISTENT_PID, NONEXISTENT_ID, expectedContact));
        assertEquals("Contact with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }

    @Test
    void deleteContact() {
        Contact contact = INITIAL_CONTACTS.get(INITIAL_CONTACTS.size() - 1);
        contactService.deleteContact(EXISTENT_PID, contact.getId());
        Optional<Contact> foundContact = contactRepository.findById(contact.getId());
        assertFalse(foundContact.isPresent());
        Optional<Phonebook> phonebook = phonebookRepository.findById(EXISTENT_PID);
        assertTrue(phonebook.isPresent());
        assertFalse(phonebook.get().getContactIds().contains(contact.getId()));
    }

    @Test
    void deleteContactWithInvalidPid() {
        Contact contact = INITIAL_CONTACTS.get(INITIAL_CONTACTS.size() - 1);
        Exception e = assertThrows(NotFoundException.class, () -> contactService.deleteContact(NONEXISTENT_ID, contact.getId()));
        assertEquals("Phonebook with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }

    @Test
    void deleteContactWithInvalidCid() {
        Exception e = assertThrows(NotFoundException.class, () -> contactService.deleteContact(EXISTENT_PID, NONEXISTENT_ID));
        assertEquals("Contact with ID " + NONEXISTENT_ID + " is not found", e.getMessage());
    }
}
