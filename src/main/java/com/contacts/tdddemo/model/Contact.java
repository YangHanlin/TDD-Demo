package com.contacts.tdddemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Contact {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String address;

    private String note;
}
