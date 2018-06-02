package org.jnd.user.controller;

import org.jnd.user.model.User;
import org.jnd.user.model.exceptions.IncorrectPasswordException;
import org.jnd.user.model.exceptions.UserNotFoundException;
import org.jnd.user.model.exceptions.UsernameExistsException;
import org.jnd.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init(){
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<?> create(@RequestBody User user, @RequestHeader HttpHeaders headers) {

        log.debug("User register");

        ResponseEntity re = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            userRepository.register(user);
            responseHeaders.add("LOGIN_MESSAGE", "CREATED");
            re = new ResponseEntity<>(user, responseHeaders, HttpStatus.CREATED);
        }
        catch (UsernameExistsException unee)    {
            responseHeaders.add("LOGIN_MESSAGE", unee.getMessage());
            re = new ResponseEntity<>(user, responseHeaders, HttpStatus.IM_USED);
        }

        return re;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<?> get(@RequestBody User user,  @RequestHeader HttpHeaders headers) {

        log.info("Login User : " + user);
        ResponseEntity re = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            user = userRepository.get(user);
            log.info("Login OK : " + user);
            responseHeaders.add("LOGIN_MESSAGE", "OK");
            re = new ResponseEntity<>(user, responseHeaders, HttpStatus.OK);
        }
        catch (UserNotFoundException unee)    {
            log.info("Login NOT FOUND : " + user);
            responseHeaders.add("LOGIN_MESSAGE", unee.getMessage());
            re = new ResponseEntity<>(user, responseHeaders, HttpStatus.NOT_FOUND);
        }
        catch (IncorrectPasswordException unee)    {
            log.info("Login BAD PASSWORD : " + user);
            responseHeaders.add("LOGIN_MESSAGE", unee.getMessage());
            re = new ResponseEntity<>(user, responseHeaders, HttpStatus.FORBIDDEN);
        }

        return re;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<?> update(@RequestBody User user, @RequestHeader HttpHeaders headers) {

        log.debug("Update User : " + user);

        ResponseEntity re = null;
        HttpHeaders responseHeaders = new HttpHeaders();

        try {
            userRepository.update(user);
            responseHeaders.add("LOGIN_MESSAGE", "UPDATED OK");
            re = new ResponseEntity<>(user, responseHeaders, HttpStatus.OK);
        }
        catch (Exception e)    {
            responseHeaders.add("LOGIN_MESSAGE", e.getMessage());
            re = new ResponseEntity<>(user, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return re;
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String ping() {
        return "OK";
    }
}
