package com.springboot.PersonalAddressBook.common.exceptions;

// 自定义异常
public class UnAuthException extends RuntimeException {
    public UnAuthException() {
        super();
    }

    public UnAuthException(String message) {
        super(message);
    }
}
