package org.jnd.user.controller;


import org.jnd.user.model.IPAddress;
import org.jnd.user.model.User;
import org.jnd.user.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/ip")
public class IPValidationController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<?> get(@RequestBody IPAddress address, @RequestHeader HttpHeaders headers) {

        log.info("validate : " + address.getAddress());
        ResponseEntity re = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            address.setGranted(true);
            responseHeaders.add("ADDRESS", "VALID");
            re = new ResponseEntity<>(address, responseHeaders, HttpStatus.OK);
        }
        catch (UserNotFoundException unee)    {
            responseHeaders.add("ADDRESS", "NOT VALID");
            re = new ResponseEntity<>(address, responseHeaders, HttpStatus.NOT_FOUND);
        }

        log.info("validated : " + address.getAddress()+" : " + address.isGranted());
        return re;
    }
}
