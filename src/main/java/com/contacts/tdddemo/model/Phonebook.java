package com.contacts.tdddemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phonebook {

    private String id;

    private List<String> contactIds = new ArrayList<>();
}
