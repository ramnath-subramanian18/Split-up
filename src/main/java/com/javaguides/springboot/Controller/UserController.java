package com.javaguides.springboot.Controller;
import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.beans.Useramount;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController

public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @CrossOrigin
    @GetMapping("/testing")
    public String testing() {
        return "testing";
    }


    @CrossOrigin
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createuser(@RequestBody User user) {
        try {
            if (userRepository.findByuserEmail(user.getUserEmail()) == null) {
                userRepository.save(user);
                return ResponseEntity.ok(user);

            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "UserEmail already exists");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
            }

        }
        catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
        }
    }



    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody User user) {
        try{
            User foundUser = userRepository.findByuserEmail(user.getUserEmail());
            if (foundUser != null && foundUser.getUserPassword().equals(user.getUserPassword())) {
                return ResponseEntity.ok(foundUser); // Return the user object if credentials are valid
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
            }
        }
        catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
        }
    }

    @CrossOrigin
    @GetMapping("/userName")
    public ResponseEntity<?> fetchUsername(@RequestParam String userId) {
        System.out.println("into username class");
        try {
            Optional<User> foundUser = userRepository.findById(userId);
            System.out.println(foundUser);
            if (foundUser.isPresent()) {
                System.out.println(foundUser);
                System.out.println(foundUser.get().getUserName());
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("userName", foundUser.get().getUserName());
                jsonResponse.put("userEmail", foundUser.get().getUserEmail());
                System.out.println(jsonResponse);
                return ResponseEntity.ok(jsonResponse.toString()); // Return the username if user is found
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "UserName doesn't exist");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }


//    @CrossOrigin
//    @PostMapping(value="/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public Object loginuser(@RequestBody User user) {
//        try {
//
//            User foundUser = userRepository.findByuserEmail(user.getUserEmail());
//            if (foundUser != null && foundUser.getUserPassword().equals(user.getUserPassword())) {
//                return foundUser;
//
//            } else {
//                System.out.println("Invalid credentials");
//                return "false";
//            }
//        } catch (Exception e) {
//            System.err.println("Error occurred during login: " + e.getMessage());
//            e.printStackTrace();
//            return "error";
//        }
//
//    }



//no uised I guess check and remove it
    @CrossOrigin
    @GetMapping(value="/userdetailsgroup/{GroupId}")
    @ResponseBody
    public List <Object> userDetailsGroup(@PathVariable String GroupId){
        List<Useramount> userId =groupRepository.findById(GroupId).get().getUserAmounts();
        List <Object> alluser=new ArrayList<Object>();
        for (int i=0;i<userId.size();i++) {
            System.out.println(userId.get(i).getUserID());
            System.out.println(userRepository.findById(userId.get(i).getUserID()));
            alluser.add(userRepository.findById(userId.get(i).getUserID()));
        }
        return alluser;
    }
    @CrossOrigin
    @GetMapping(value="/userBalance/{userId}")
    @ResponseBody
    public ResponseEntity<?> userBalance(@PathVariable String userId) {
        HashMap<String, Double> hashMap = new HashMap<>();
        Double userBalance = 0.0;
        List<Group> groups = groupRepository.findByuserAmounts(userId);
        try {
            if (groups.isEmpty()) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Group Emmpty");

                return ResponseEntity.ok(jsonResponse.toString());
            } else {
                for (Group group : groups) {
                    for (Useramount useramount : group.getUserAmounts()) {
                        if (useramount.getUserID().equals(userId)) {
                            userBalance += useramount.getUserBalance();
                        }
                    }
                }
                hashMap.put("totalBalance", userBalance);
                return ResponseEntity.ok(hashMap);
            }
        }
        catch (Exception e) {

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
        }
    }
}
