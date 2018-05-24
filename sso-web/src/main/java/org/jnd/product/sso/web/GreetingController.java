package org.jnd.product.sso.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class GreetingController {

    private Log log = LogFactory.getLog(InventoryController.class);

    @GetMapping("/greeting")
    public String greeting(Principal principal, Model model) {
        model.addAttribute("name", "justin");
        log.info("***");
        KeycloakPrincipal kPrinciple = (KeycloakPrincipal)principal;
        AccessToken token = kPrinciple.getKeycloakSecurityContext().getToken();
        log.info(kPrinciple.getKeycloakSecurityContext().getTokenString());
        return "greeting";
    }

}
