package org.jnd.keycloak.web.client.controllers;


import org.jnd.keycloak.web.client.model.TPPClient;
import org.jnd.keycloak.web.client.model.User;
import org.keycloak.representations.account.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
public class CreateUserController {

    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createUser(@RequestBody User user) {

        UUID id = UUID.randomUUID();
        String username = user.getUsername()+"-"+id.toString().split("-")[0];

        UserRepresentation userrep = new UserRepresentation();
        user.setUsername(username);
        user.setFirstname(user.getFirstname());
        //TODO
        return null;
    }

}
