package com.javaguides.springboot.Controller;

import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.beans.Useramount;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.UserRepository;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController

public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;


    @CrossOrigin
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createuser(@RequestBody User user) {
        System.out.println(userRepository.findByuserEmail(user.getUserEmail()));
        if(userRepository.findByuserEmail(user.getUserEmail())==null) {
            System.out.println("Creating new user");
//        user.setUserGroup(null);
            userRepository.save(user);
            return user;
        }
        else {
            System.out.println("into this");
            return new User("user exists");
        }
    }
    @CrossOrigin
    @PostMapping(value="/signin",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Object loginuser(@RequestBody User user){
        System.out.println("indise logn");
        return user;
//        try {
//            if (user.getUserPassword().equals(userRepository.findByuserEmail(user.getUserEmail()).getUserPassword())) {
////                String token = JwtUtil.generateToken(user.getUserEmail());
////                System.out.println("token");
////                System.out.println(token);
//                return userRepository.findByuserEmail(user.getUserEmail());
//            } else {
//                return false;
//            }
//        }
//        catch(Exception e) {
//            return false;
//        }
    }


    @GetMapping(value="/userdetailsgroup/{GroupId}")
    @ResponseBody
    public List <Object> userDetailsGroup(@PathVariable String GroupId){
//        Optional<Group> group =(groupRepository.findById(GroupId));
        List<Useramount> userId =groupRepository.findById(GroupId).get().getUserAmounts();
        List <Object> alluser=new ArrayList<Object>();
        for (int i=0;i<userId.size();i++) {
            System.out.println(userId.get(i).getUserID());
            System.out.println(userRepository.findById(userId.get(i).getUserID()));
            alluser.add(userRepository.findById(userId.get(i).getUserID()));
        }
        return alluser;
    }

}
