package com.contacts.tdddemo.model;

import lombok.Data;

import java.util.List;

@Data
public class Phonebook {

    private String id;

    private List<String> contactIds;
}
