package com.contacts.tdddemo.service.impl;

import com.contacts.tdddemo.model.Contact;
import com.contacts.tdddemo.service.ContactService;
import com.contacts.tdddemo.vo.ContactItem;

import java.util.List;

public class ContactServiceImpl implements ContactService {

    @Override
    public List<ContactItem> listContacts(String pid) {
        return null;
    }

    @Override
    public String createContact(String pid, Contact contact) {
        return null;
    }

    @Override
    public void updateContact(String pid, String cid, Contact contact) {

    }

    @Override
    public void deleteContact(String pid, String cid) {

    }
}
