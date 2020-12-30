package com.contacts.tdddemo.controller;

import com.contacts.tdddemo.model.Contact;
import com.contacts.tdddemo.service.ContactService;
import com.contacts.tdddemo.vo.ContactIdResult;
import com.contacts.tdddemo.vo.ContactList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phonebook/{pid}")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts")
    public ContactList listContacts(@PathVariable String pid) {
        return null;
    }

    @PostMapping("/contacts")
    public ContactIdResult createContact(@RequestBody Contact contact) {
        return null;
    }

    @PutMapping("/contact/{cid}")
    public void updateContact(@PathVariable String cid, @RequestBody Contact contact) {
    }

    @DeleteMapping("/contact/{cid}")
    public void deleteContact(@PathVariable String cid) {
    }
}
