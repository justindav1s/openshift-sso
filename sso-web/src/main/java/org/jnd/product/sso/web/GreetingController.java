package org.jnd.product.sso.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(Principal principal, Model model) {
        model.addAttribute("name", principal.getName());
        return "greeting";
    }

}
