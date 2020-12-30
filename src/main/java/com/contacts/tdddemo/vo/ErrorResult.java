package com.contacts.tdddemo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResult {

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object extras;

    public ErrorResult(String message) {
        this.message = message;
    }
}
