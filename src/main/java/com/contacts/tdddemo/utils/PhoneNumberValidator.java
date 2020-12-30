package com.contacts.tdddemo.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Autowired
    private PhoneNumberUtil phoneUtil;

    @Value("${phone-number-util.default-region:CN}")
    private String defaultRegion;

    @Override
    public void initialize(ValidPhoneNumber constraint) {
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneUtil.parse(str, defaultRegion);
        } catch (NumberParseException e) {
            log.debug("Phone number '" + str + "' cannot be parsed");
            return false;
        }
        boolean result = phoneUtil.isValidNumber(phoneNumber);
        if (!result) {
            log.debug("Phone number '" + str + "' is an invalid number");
        }
        return result;
    }
}
