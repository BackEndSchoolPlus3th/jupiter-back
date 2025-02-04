package com.jupiter.wyl.global.exception;

import com.jupiter.wyl.global.rsData.RsData;
import com.jupiter.wyl.standard.base.Empty;

public class ServiceException extends RuntimeException {
    private final String resultCode;
    private final String msg;

    public ServiceException(ExceptionCode exceptionCode) {
        super(exceptionCode.getCode() + " : " + exceptionCode.getMessage());

        this.resultCode = exceptionCode.getCode();
        this.msg = exceptionCode.getMessage();
    }

    public RsData<Empty> getRsData() {
        return new RsData<>(resultCode, msg);
    }
}
