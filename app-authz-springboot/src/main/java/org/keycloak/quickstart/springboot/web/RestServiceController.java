package org.keycloak.quickstart.springboot.web;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.quickstart.springboot.security.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController

public class RestServiceController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/rest/ip_protected", method = RequestMethod.GET)
    public String handleProtected(Model model) {
        configCommonAttributes(model);
        return "ip_protected";
    }

    @RequestMapping(value = "/rest/justin", method = RequestMethod.GET)
    public String handlePJustinProtected(Model model) {
        configCommonAttributes(model);
        return "welcome justin";
    }

    private void configCommonAttributes(Model model) {
        model.addAttribute("identity", new Identity(getKeycloakSecurityContext()));
    }

    private KeycloakSecurityContext getKeycloakSecurityContext() {
        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }
}
