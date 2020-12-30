package com.contacts.tdddemo.controller;

import com.contacts.tdddemo.service.PhonebookService;
import com.contacts.tdddemo.vo.PhonebookIdResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class PhonebookController {

    @Autowired
    PhonebookService phonebookService;

    @PostMapping("/phonebooks")
    public PhonebookIdResult createPhonebook() {
        return new PhonebookIdResult(phonebookService.createPhonebook());
    }

    @DeleteMapping("/phonebook/{id}")
    public void deletePhonebook(@PathVariable String id, HttpServletResponse response) {
        phonebookService.deletePhonebook(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
