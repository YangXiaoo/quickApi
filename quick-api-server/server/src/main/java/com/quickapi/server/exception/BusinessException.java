package com.quickapi.server.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 2332608236621015980L;

    private IMessage errorCode;
    private Object data;

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(IMessage errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(IMessage errorCode, Object data) {
        this.data = data;
        this.errorCode = errorCode;
    }

    public BusinessException(IMessage errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public String getMsg() {
            return getMessage();
    }

    public IMessage getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(IMessage errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
