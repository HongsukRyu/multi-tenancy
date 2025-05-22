package com.multitenancy.back.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * FrontEnd 로 보내는 결과 집합
 */
@Getter
@Setter
public class Success {
    private boolean success = false;
    private Object result = null;
    private Error error = new Error();

    public Success() {
    }

    public Success(boolean flag) {
        success = flag;
    }

    @Getter
    @Setter
    static
    class Error {
        private String code;
        private String msg;
    }

    public void setErrorMsg(String msg) {
        this.error.setMsg(msg);
    }

    public void setErrorMsg(Exception e) {
        Optional<Throwable> rootCause = Stream.iterate(e, Throwable::getCause)
                .filter(element -> element.getCause() == null)
                .findFirst();

        if(rootCause.isEmpty())
            this.error.setMsg(e.getMessage());
        else
            this.error.setMsg(rootCause.toString());
    }

    public void setErrorCode(String code) {
        this.error.setCode(code);
    }

    public String getErrorCode() {
        return this.error.getCode();
    }

    public String getErrorMsg() {
        return this.error.getMsg();
    }

    // Static factory method for ok
    public static Success ok() {
        return new Success(true);
    }

    public static Success ok(Object obj) {
        Success success = Success.ok();
        success.setResult(obj);
        return success;
    }

    // Static factory method for error
    public static Success error(String errorCode, String errorMsg) {
        Success success = new Success(false);
        success.setErrorCode(errorCode);
        success.setErrorMsg(errorMsg);
        return success;
    }

    public static Success error(String errorCode, String errorMsg, Object obj) {
        Success success = error(errorCode, errorMsg);
        success.setResult(obj);
        return success;
    }
}
