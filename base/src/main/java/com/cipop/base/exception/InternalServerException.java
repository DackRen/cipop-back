package com.cipop.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @ClassName: InternalServerException
 * @Description: InternalServerException
 * @Author: Yang Naihua
 * @Create: 2018-06-21 14:00
 **/
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends ApiException {
    public InternalServerException(ErrorCode error) {
        this(error.toString());
    }

    public InternalServerException(String message, Throwable e) {
        super(message, e);
    }

    public InternalServerException(ErrorCode errorCode, Throwable e) {
        super(errorCode.toString(), e);
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(Throwable e) {
        super(e);
    }

}
