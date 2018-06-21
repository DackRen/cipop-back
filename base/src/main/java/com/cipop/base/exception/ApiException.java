package com.cipop.base.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @ClassName: ApiException
 * @Description: ApiException
 * @Author: Yang Naihua
 * @Create: 2018-06-21 13:55
 **/
@Component
public class ApiException extends RuntimeException {
    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        ApiException.messageSource = messageSource;
    }

    private ApiException() {
    }

    public ApiException(String message, Throwable e) {
        super(message, e);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable e) {
        super(e);
    }

    @Override
    public String getLocalizedMessage() {
        return messageSource.getMessage(getMessage(), null, "", Locale.SIMPLIFIED_CHINESE);
    }
}