package com.contacts.tdddemo.controller;

import com.contacts.tdddemo.vo.PhonebookIdResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class PhonebookController {

    @PostMapping("/phonebooks")
    public PhonebookIdResult createPhonebook() {
        return null;
    }

    @DeleteMapping("/phonebook/{id}")
    public void deletePhonebook(@PathVariable String id) {
    }
}
