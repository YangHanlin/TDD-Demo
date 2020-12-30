package com.contacts.tdddemo.model;

import lombok.Data;

@Data
public class Contact {

    private String id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String address;

    private String note;
}
