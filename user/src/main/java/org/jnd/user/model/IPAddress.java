package org.jnd.user.model;

public class IPAddress {

    private String address;
    private String owner;
    private boolean granted;

    public String getAddress() {
        return address;
    }

    public IPAddress(String owner, String address) {
        this.address = address;
        this.owner = owner;
        this.granted = false;
    }

    public IPAddress() {
        this.granted = false;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }


}
