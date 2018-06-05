package org.jnd.product.sso.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnd.product.sso.web.proxy.InventoryProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class InventoryController {

    private Log log = LogFactory.getLog(InventoryController.class);

    @Autowired
    private InventoryProxy inventoryProxy;

    @GetMapping("/products")
    public String greeting(Principal principal, Model model) {
        ResponseEntity<List> exchange = inventoryProxy.getProductTypes(principal);
        model.addAttribute("product_types", exchange.getBody());
        return "products";
    }

    @GetMapping("/all_products")
    public String products(Principal principal, Model model) {
        log.info(" products(Model model) : "+model);
        ResponseEntity<List> exchange = inventoryProxy.getAllProducts(principal);
        model.addAttribute("products", exchange.getBody());
        return "all_products";
    }


    @RequestMapping(value = "/product_type/{type}", method = RequestMethod.GET)
    public String product_type(@PathVariable String type, Principal principal, Model model) {
        log.info(" product_type : "+type);
        ResponseEntity<List> exchange = inventoryProxy.getProductsofType(principal, type);
        model.addAttribute("products", exchange.getBody());
        return "product_group";
    }






}
