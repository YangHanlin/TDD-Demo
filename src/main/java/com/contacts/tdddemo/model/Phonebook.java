package com.contacts.tdddemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@RedisHash("phonebook")
public class Phonebook {

    private String id;

    private List<String> contactIds = new ArrayList<>();
}
