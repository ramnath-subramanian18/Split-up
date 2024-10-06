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

import java.time.LocalDateTime;
import java.util.*;
import com.javaguides.springboot.Service.LogglyService;
import com.javaguides.springboot.Service.StringLog;

@RestController

public class UserController {
    //It allows Spring to automatically "wire" or inject the required beans
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private LogglyService logglyService;
    @Autowired
    private StringLog stringLog;

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
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
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
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
        }
    }

    @CrossOrigin
    @GetMapping("/userDetails")
    public ResponseEntity<?> fetchDetails(@RequestParam String userId) {
        try {
            Optional<User> foundUser = userRepository.findById(userId);
            if (foundUser.isPresent()) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("userName", foundUser.get().getUserName());
                jsonResponse.put("userEmail", foundUser.get().getUserEmail());
                return ResponseEntity.ok(jsonResponse.toString()); // Return the username if user is found
            } else {

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "UserName doesn't exist");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
            }
        } catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }


    @CrossOrigin
    @GetMapping("/userEmail")
    public ResponseEntity<?> fetchUserEmail(@RequestParam String userEmail) {
        try {
            User foundUser = userRepository.findByuserEmail(userEmail);
            if (foundUser != null) {
//            Optional<User> foundUser = userRepository.findByuserEmail(userEmail);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("userName", foundUser.getUserName());
                jsonResponse.put("userEmail", foundUser.getUserEmail());
                return ResponseEntity.ok(jsonResponse.toString()); // Return the username if user is found
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "UserEmail doesn't exist");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
            }
        } catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }




//mostly it is not used
    @CrossOrigin
    @GetMapping(value="/userdetailsgroup/{GroupId}")
    @ResponseBody
    public List <Object> userDetailsGroup(@PathVariable String GroupId){
            List<Useramount> userId = groupRepository.findById(GroupId).get().getUserAmounts();
            List<Object> alluser = new ArrayList<Object>();
            for (int i = 0; i < userId.size(); i++) {
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
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonResponse.toString());
        }
    }
}
