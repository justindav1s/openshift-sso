/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jnd.microservices.sso.userstorage;

import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.util.ServerCookie;
import org.keycloak.models.*;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class UsernamePasswordAuthenticator implements Authenticator {

    public static final String CREDENTIAL_TYPE = "secret_question";

    protected boolean hasCookie(AuthenticationFlowContext context) {
        System.out.println("UsernamePasswordAuthenticator : hasCookie");
        Cookie cookie = context.getHttpRequest().getHttpHeaders().getCookies().get("JD_VALID_CREDENTIALS");
        boolean result = cookie != null;
        if (result) {
            System.out.println("Bypassing username/password because cookie is set");
        }
        return false;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        System.out.println("UsernamePasswordAuthenticator : authenticate");
        Response challenge = context.form().createForm("jd_login.ftl");
        context.challenge(challenge);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        System.out.println("UsernamePasswordAuthenticator : action");
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey("cancel")) {
            context.cancelLogin();
            return;
        }
        boolean validated = validateAnswer(context);
//        if (!validated) {
//            Response challenge =  context.form()
//                    .setError("badSecret")
//                    .createForm("jd_login.ftl");
//            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
//            return;
//        }
//        setCookie(context);
        System.out.println("UsernamePasswordAuthenticator : action exit");
        context.success();
    }

    protected void setCookie(AuthenticationFlowContext context) {
        System.out.println("UsernamePasswordAuthenticator : setCookie");
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        int maxCookieAge = 60 * 60 * 24 * 30; // 30 days
        if (config != null) {
            maxCookieAge = Integer.valueOf(config.getConfig().get("cookie.max.age"));

        }
        URI uri = context.getUriInfo().getBaseUriBuilder().path("realms").path(context.getRealm().getName()).build();
//        addCookie("JD_VALID_CREDENTIALS", "true",
//                uri.getRawPath(),
//                null, null,
//                maxCookieAge,
//                false, true);
    }

    public static void addCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly) {
        System.out.println("UsernamePasswordAuthenticator : addcookie");
        HttpResponse response = ResteasyProviderFactory.getContextData(HttpResponse.class);
        StringBuffer cookieBuf = new StringBuffer();
        ServerCookie.appendCookieValue(cookieBuf, 1, name, value, path, domain, comment, maxAge, secure, httpOnly);
        String cookie = cookieBuf.toString();
        response.getOutputHeaders().add(HttpHeaders.SET_COOKIE, cookie);
    }


    protected boolean validateAnswer(AuthenticationFlowContext context) {
        System.out.println("UsernamePasswordAuthenticator : validateAnswer");
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String username = formData.getFirst("jd_username");
        String password = formData.getFirst("jd_password");
        System.out.println("Username : ["+username+"]");
        System.out.println("Password : ["+password+"]");
        boolean valid = false;
        if (username.equals("jnd") && password.equals("password"))
            valid = true;

        return true;
    }

    @Override
    public boolean requiresUser() {
        System.out.println("UsernamePasswordAuthenticator : requireUser");
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        System.out.println("UsernamePasswordAuthenticator : configuredFor");
        //return session.userCredentialManager().isConfiguredFor(realm, user, UsernamePasswordCredentialProvider.PASSWORD);
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        System.out.println("UsernamePasswordAuthenticator : setRequiredActions");
        //user.addRequiredAction(SecretQuestionRequiredAction.PROVIDER_ID);
    }

    @Override
    public void close() {

    }
}
