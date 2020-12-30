package com.contacts.tdddemo.controller;

import com.contacts.tdddemo.model.Contact;
import com.contacts.tdddemo.service.ContactService;
import com.contacts.tdddemo.vo.ContactIdResult;
import com.contacts.tdddemo.vo.ContactList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/phonebook/{pid}")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts")
    public ContactList listContacts(@PathVariable String pid) {
        return new ContactList(contactService.listContacts(pid));
    }

    @PostMapping("/contacts")
    public ContactIdResult createContact(@PathVariable String pid, @RequestBody @Valid Contact contact, HttpServletResponse response) {
        ContactIdResult result = new ContactIdResult(contactService.createContact(pid, contact));
        response.setStatus(HttpServletResponse.SC_CREATED);
        return result;
    }

    @PutMapping("/contact/{cid}")
    public void updateContact(@PathVariable String pid, @PathVariable String cid, @RequestBody @Valid Contact contact, HttpServletResponse response) {
        contactService.updateContact(pid, cid, contact);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @DeleteMapping("/contact/{cid}")
    public void deleteContact(@PathVariable String pid, @PathVariable String cid, HttpServletResponse response) {
        contactService.deleteContact(pid, cid);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
