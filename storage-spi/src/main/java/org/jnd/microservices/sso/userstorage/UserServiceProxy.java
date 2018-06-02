package org.jnd.microservices.sso.userstorage;

import org.keycloak.models.RealmModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class UserServiceProxy {

    private String base_url = null;

    public UserServiceProxy(String base_url)    {
        this.base_url = base_url;
    }

    private RestTemplate restTemplate = new RestTemplate();

    public User getUser(User user) {

        System.out.println("UserServiceProxy : getUser : " + user.getUsername());

        try {
            HttpEntity<User> request = new HttpEntity<>(user);

            ResponseEntity<User> exchange =
                    this.restTemplate.exchange(
                            base_url + "/user/get",
                            HttpMethod.POST,
                            request,
                            User.class);

            user = exchange.getBody();
        }
        catch (HttpClientErrorException hcee)   {
            //do nothing this user does not exist, the 404 causes the exception to be thrown
            System.out.println("UserServiceProxy : getUser  : NOT FOUND : " + user.getUsername());
        }
        return user;
    }

    public User loginUser(User user) {

        System.out.println("UserServiceProxy : login : " + user.getUsername());

        try {
            HttpEntity<User> request = new HttpEntity<>(user);

            ResponseEntity<User> exchange =
                    this.restTemplate.exchange(
                            base_url + "/user/login",
                            HttpMethod.POST,
                            request,
                            User.class);

            user = exchange.getBody();
        }
        catch (HttpClientErrorException hcee)   {
            System.out.println("UserServiceProxy : login  : NOT FOUND/BAD PASSWORD : " + user.getUsername());
        }

        System.out.println("UserServiceProxy : after login : " + user.toString());
        return user;
    }

    public User register(User user) {

        System.out.println("UserServiceProxy : register");

        HttpEntity<User> request = new HttpEntity<>(user);

        ResponseEntity<User> exchange =
                this.restTemplate.exchange(
                        base_url + "/user/register",
                        HttpMethod.POST,
                        request,
                        User.class);

        user = exchange.getBody();
        return user;
    }

    public User[] getAllUsers() {

        System.out.println("UserServiceProxy : getAllUsers");

        ResponseEntity<User[]> exchange =
                this.restTemplate.exchange(
                        base_url+"/user/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<User[]>() {});

        System.out.println("All Users : "+exchange.getBody());

        User[] users = exchange.getBody();

        return users;
    }

    public int getUsersCount() {

        System.out.println("UserServiceProxy : getUsersCount");

        ResponseEntity<Integer> exchange =
                this.restTemplate.exchange(
                        base_url+"/user/count",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Integer>() {});

        System.out.println("User Count : "+exchange.getBody());

        int count = exchange.getBody();

        return count;
    }


}
