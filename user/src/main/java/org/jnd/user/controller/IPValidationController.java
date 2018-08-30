package org.jnd.user.controller;


import org.jnd.user.model.IPAddress;
import org.jnd.user.model.User;
import org.jnd.user.model.exceptions.UserNotFoundException;
import org.jnd.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

@CrossOrigin
@RestController
@RequestMapping("/ip")
public class IPValidationController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<?> get(@RequestBody IPAddress address, @RequestHeader HttpHeaders headers) {

        log.info("validate : " + address.getAddress());

        ResponseEntity re = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            User user = userRepository.get(address.getOwner());
            if (user.getIpaddress().equals(address.getAddress())) {
                address.setGranted(true);
            }
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
