package com.contacts.tdddemo.repository;

import com.contacts.tdddemo.model.Phonebook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhonebookRepository extends CrudRepository<Phonebook, String> {
}
