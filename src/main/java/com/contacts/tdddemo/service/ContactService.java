package com.contacts.tdddemo.service;

import com.contacts.tdddemo.model.Contact;
import com.contacts.tdddemo.vo.ContactItem;

import java.util.List;

public interface ContactService {

    List<ContactItem> listContacts(String pid);

    String createContact(Contact contact);

    void updateContact(String id, Contact contact);

    void deleteContact(String id);
}
