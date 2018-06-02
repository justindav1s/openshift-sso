package org.jnd.microservices.sso.userstorage;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;


public class User implements Serializable {


    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private ArrayList groups;

    public User(String username, String password, String firstname, String lastname, String email) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.setGroups(new ArrayList<String>());
        this.id = UUID.randomUUID().toString();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    public User() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList getGroups() {
        return groups;
    }

    public void setGroups(ArrayList groups) {
        this.groups = groups;
    }

    public boolean isValid()    {
        boolean isValid = false;
        if ( (this.getFirstname() != null) &&  (this.getLastname() != null) )   {
            isValid = true;
        }
        return isValid;
    }
}
