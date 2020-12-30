package com.contacts.tdddemo.vo;

import lombok.Data;

import java.util.List;

@Data
public class ContactList {

    private List<ContactItem> contacts;
}
