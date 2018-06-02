package org.jnd.user.model.exceptions;

public class IncorrectPasswordException extends RuntimeException {

    public String message;

    public IncorrectPasswordException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
