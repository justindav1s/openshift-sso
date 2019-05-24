package org.jnd.keycloak.web.client.model;

public class TPPClient {

    private String name = null;
    private String callback_uri = null;
    private String client_id = null;
    private String client_secret = null;

    public String getRealm() {
        return realm;
    }

    private String realm = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallback_uri() {
        return callback_uri;
    }

    public void setCallback_uri(String callback_uri) {
        this.callback_uri = callback_uri;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
