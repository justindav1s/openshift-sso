package org.jnd.product.sso.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AdminController {

    private Log log = LogFactory.getLog(AdminController.class);

    @GetMapping("/admin")
    public ResponseEntity admin(HttpServletRequest request) {

        log.info("Admin endpoint called");

        return new ResponseEntity(null, null, HttpStatus.OK);
        
    }

}
