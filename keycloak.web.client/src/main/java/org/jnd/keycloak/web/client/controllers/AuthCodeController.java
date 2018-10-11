package org.jnd.keycloak.web.client.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthCodeController {

    private Log log = LogFactory.getLog(AuthCodeController.class);

    @RequestMapping(value = "/auth_code", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getcode(HttpServletRequest request) {

        log.info("AuthCodeController GET");

        String code = request.getParameter("code");
        log.info("code : " + code);



        return new ResponseEntity(null, null, HttpStatus.OK);

    }
}
