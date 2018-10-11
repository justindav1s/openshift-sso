package org.jnd.keycloak.web.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnd.keycloak.web.client.controllers.TokenController;

import java.util.Base64;


public class TokenResponse {

    private Log log = LogFactory.getLog(TokenResponse.class);

    private String access_token = null;
    private int expires_in = 0;
    private int refresh_expires_in = 0;
    private String refresh_token = null;
    private String token_type = null;
    @JsonProperty("not-before-policy")
    private int not_before_policy = 0;
    private String session_state = null;

    public String getAccess_tokenDecoded() {
        String[] parts = access_token.split("\\.");
        byte[] decoded = Base64.getMimeDecoder().decode(parts[1]);
        String token = new String(decoded);
        return token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public void setRefresh_expires_in(int refresh_expires_in) {
        this.refresh_expires_in = refresh_expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getSession_state() {
        return session_state;
    }

    public void setSession_state(String session_state) {
        this.session_state = session_state;
    }

    public int getNot_before_policy() {
        return not_before_policy;
    }

    public void setNot_before_policy(int not_before_policy) {
        this.not_before_policy = not_before_policy;
    }
}
