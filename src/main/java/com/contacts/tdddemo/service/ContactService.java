package com.contacts.tdddemo.service;

import com.contacts.tdddemo.model.Contact;
import com.contacts.tdddemo.vo.ContactItem;

import java.util.List;

public interface ContactService {

    List<ContactItem> listContacts(String pid);

    Contact getContactById(String pid, String cid);

    String createContact(String pid, Contact contact);

    void updateContact(String pid, String cid, Contact contact);

    void deleteContact(String pid, String cid);
}
