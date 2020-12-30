package com.contacts.tdddemo.service.impl;

import com.contacts.tdddemo.exception.NotFoundException;
import com.contacts.tdddemo.model.Contact;
import com.contacts.tdddemo.model.Phonebook;
import com.contacts.tdddemo.repository.ContactRepository;
import com.contacts.tdddemo.repository.PhonebookRepository;
import com.contacts.tdddemo.service.ContactService;
import com.contacts.tdddemo.vo.ContactItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    PhonebookRepository phonebookRepository;

    @Override
    public List<ContactItem> listContacts(String pid) {
        if (!phonebookRepository.existsById(pid)) {
            throw new NotFoundException("Phonebook with ID " + pid + " is not found");
        }
        Phonebook phonebook = phonebookRepository.findById(pid).get();
        List<ContactItem> result = new ArrayList<>();
        phonebook.getContactIds().forEach(cid -> result.add(new ContactItem(cid, contactRepository.findById(cid).get())));
        return result;
    }

    @Override
    public Contact getContactById(String pid, String cid) {
        if (!phonebookRepository.existsById(pid)) {
            throw new NotFoundException("Phonebook with ID " + pid + " is not found");
        }
        if (!contactRepository.existsById(cid)) {
            throw new NotFoundException("Contact with ID " + cid + " is not found");
        }
        Phonebook phonebook = phonebookRepository.findById(pid).get();
        if (!phonebook.getContactIds().contains(cid)) {
            throw new NotFoundException("Contact with ID " + cid + " is not found");
        }
        return contactRepository.findById(pid).get();
    }

    @Override
    public String createContact(String pid, Contact contact) {
        if (!phonebookRepository.existsById(pid)) {
            throw new NotFoundException("Phonebook with ID " + pid + " is not found");
        }
        String cid = UUID.randomUUID().toString();
        contact.setId(cid);
        contactRepository.save(contact);
        Phonebook phonebook = phonebookRepository.findById(pid).get();
        phonebook.getContactIds().add(cid);
        phonebookRepository.save(phonebook);
        return cid;
    }

    @Override
    public void updateContact(String pid, String cid, Contact contact) {
        if (!phonebookRepository.existsById(pid)) {
            throw new NotFoundException("Phonebook with ID " + pid + " is not found");
        }
        if (!contactRepository.existsById(cid)) {
            throw new NotFoundException("Contact with ID " + cid + " is not found");
        }
        Phonebook phonebook = phonebookRepository.findById(pid).get();
        if (!phonebook.getContactIds().contains(cid)) {
            throw new NotFoundException("Contact with ID " + cid + " is not found");
        }
        contact.setId(cid);
        contactRepository.save(contact);
    }

    @Override
    public void deleteContact(String pid, String cid) {
        if (!phonebookRepository.existsById(pid)) {
            throw new NotFoundException("Phonebook with ID " + pid + " is not found");
        }
        if (!contactRepository.existsById(cid)) {
            throw new NotFoundException("Contact with ID " + cid + " is not found");
        }
        Phonebook phonebook = phonebookRepository.findById(pid).get();
        if (!phonebook.getContactIds().contains(cid)) {
            throw new NotFoundException("Contact with ID " + cid + " is not found");
        }
        contactRepository.deleteById(cid);
        phonebook.getContactIds().remove(cid);
        phonebookRepository.save(phonebook);
    }
}
