package com.contacts.tdddemo.vo;

import com.contacts.tdddemo.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactItem {

    private String id;

    private String firstName;

    private String lastName;

    public ContactItem(String id, Contact contact) {
        this.id = id;
        this.firstName = contact.getFirstName();
        this.lastName = contact.getLastName();
    }
}
