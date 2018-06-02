package org.jnd.user.model.exceptions;

public class UsernameExistsException extends RuntimeException {

    private String message;

    public UsernameExistsException(String message){
        this.setMessage(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
