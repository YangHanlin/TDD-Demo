package com.contacts.tdddemo.service.impl;

import com.contacts.tdddemo.exception.NotFoundException;
import com.contacts.tdddemo.model.Phonebook;
import com.contacts.tdddemo.repository.ContactRepository;
import com.contacts.tdddemo.repository.PhonebookRepository;
import com.contacts.tdddemo.service.PhonebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class PhonebookServiceImpl implements PhonebookService {

    @Autowired
    private PhonebookRepository phonebookRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public String createPhonebook() {
        String id = UUID.randomUUID().toString();
        phonebookRepository.save(new Phonebook(id, new ArrayList<>()));
        return id;
    }

    @Override
    public void deletePhonebook(String id) {
        if (!phonebookRepository.existsById(id)) {
            throw new NotFoundException("Phonebook with ID " + id + " is not found");
        }
        Phonebook phonebook = phonebookRepository.findById(id).get();
        for (String cid : phonebook.getContactIds()) {
            contactRepository.deleteById(cid);
        }
        phonebookRepository.deleteById(id);
    }
}
