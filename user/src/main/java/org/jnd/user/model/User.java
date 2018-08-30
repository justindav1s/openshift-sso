package org.jnd.user.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

@Indices({
        @Index(value = "username", type = IndexType.Unique),
        @Index(value = "id", type = IndexType.Unique),
        @Index(value = "lastname", type = IndexType.NonUnique)
})
public class User implements Serializable {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
    @Id
    private String username;
    private String password;
    private ArrayList groups;
    private String ipaddress;

    public User(String username, String password, String firstname, String lastname, String email, String ipaddress) {
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

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
}
