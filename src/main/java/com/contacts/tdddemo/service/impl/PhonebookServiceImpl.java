package com.contacts.tdddemo.service.impl;

import com.contacts.tdddemo.repository.PhonebookRepository;
import com.contacts.tdddemo.service.PhonebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhonebookServiceImpl implements PhonebookService {

    @Autowired
    private PhonebookRepository phonebookRepository;

    @Override
    public String createPhonebook() {
        return null;
    }

    @Override
    public void deletePhonebook(String id) {

    }
}
