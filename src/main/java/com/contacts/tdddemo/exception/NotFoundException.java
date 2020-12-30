package com.contacts.tdddemo.exception;

public class NotFoundException extends ContactsAppException {
    public NotFoundException(String message) {
        super(message);
    }
}
