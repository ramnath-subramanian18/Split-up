package com.javaguides.springboot.Controller;

import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.repositories.UserRepository;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createuser(@RequestBody User user) {
        System.out.println("Creating new user");
        System.out.println(user.getUserName());
        System.out.println(user.getUserPassword());
        System.out.println(user.getUserEmail());
        userRepository.save(user);
        return user;
    }
    @PostMapping(value="/signin",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonObject loginuser(@RequestBody User user){
        System.out.println("inside the login function");
        if(user.getUserPassword().equals(userRepository.findByuserEmail(user.getUserEmail()).getUserPassword())){
            return new JsonObject("{'Result':'True'}");
        }
        else{
            return new JsonObject("{'Result':'False'}");
        }

    }
}
