package org.jnd.product.sso.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnd.product.sso.web.proxy.InventoryProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private Log log = LogFactory.getLog(HomeController.class);

    @Autowired
    private InventoryProxy inventoryProxy;

    @GetMapping("/home")
    public String greeting(Principal principal, Model model) {
        return "home";
    }
}
