package org.jnd.user.model.exceptions;

public class UserNotFoundException extends RuntimeException {

    public String message;

    public UserNotFoundException(String message){
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
