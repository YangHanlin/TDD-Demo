package com.contacts.tdddemo.config;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhoneUtilBeanConfig {

    @Bean
    public PhoneNumberUtil phoneNumberUtil() {
        return PhoneNumberUtil.getInstance();
    }
}
