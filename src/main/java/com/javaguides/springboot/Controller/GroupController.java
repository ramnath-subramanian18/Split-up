package com.javaguides.springboot.Controller;
import com.javaguides.springboot.Service.LogglyService;
import com.javaguides.springboot.Service.StringLog;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.javaguides.springboot.Service.LogglyService;
import com.javaguides.springboot.Service.StringLog;
import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.beans.Useramount;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;
import java.util.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.swing.text.html.Option;
import javax.swing.text.html.StyleSheet;
//import com.javaguides.springboot.beans.UserAmount;

//import static jdk.nio.zipfs.ZipFileAttributeView.AttrID.group;

@RestController
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LogglyService logglyService;
    @Autowired
    private StringLog stringLog;

    //Create a group
    @CrossOrigin
    @PostMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        try {
            Optional<User> userOptional1 = userRepository.findById(group.getUserAmounts().get(0).getUserID());
            if (userOptional1.isPresent()) {
                User user = userOptional1.get();
                List<String> userGroup = user.getUserGroup();
                if (userGroup == null) {
                    userGroup = new ArrayList<>();
                    user.setUserGroup(userGroup);
                }

                    for (String groupID : userGroup) {
                        if (groupRepository.findById(groupID).get().getGroupName().equals(group.getGroupName())) {
                            JSONObject jsonResponse = new JSONObject();
                            jsonResponse.put("message", "Group already Exists");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
                        }
                    }
                groupRepository.save(group);
                String groupIDSingle = group.get_id();
                String userID = group.getUserAmounts().get(0).getUserID();
                Optional<User> userOptional = userRepository.findById(userID);

                if (userOptional.isPresent()) {
                    userGroup.add(groupIDSingle);
                    userRepository.save(user);
                }
                return ResponseEntity.ok(group);
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "user name doesn't exists");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
            }
        }
        catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }
    //Put request to add a user to a group
    // group id is added to the user table and group table
    @CrossOrigin
    @PutMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editGroup(@RequestBody Map<String, String> requestBody) {
        try {
            String emailId = requestBody.get("emailId");
            String groupID = requestBody.get("groupID");
            String userName=requestBody.get("userName");

            if (emailId == null || emailId.isEmpty() || groupID == null || groupID.isEmpty()) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Email ID or Group ID is missing");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
            }

            User user = userRepository.findByuserEmail(emailId);
            if (user == null) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "User not found with email: " + emailId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
            } else {

                Optional<Group> optionalGroup = groupRepository.findById(groupID);
                if (optionalGroup.isPresent()) {
                    Group group = optionalGroup.get();

                    // Check if the user already exists in the group
                    for (Useramount userAmount : group.getUserAmounts()) {
                        if (userAmount.getUserID().equals(user.get_id())) {
                            JSONObject jsonResponse = new JSONObject();
                            jsonResponse.put("message", "User already exists in the group: " + emailId);
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
                        }
                    }

                    // Add user to the group
                    String userId = user.get_id().toString();
                    Float userBalance = 0.0F;
                    ArrayList<Useramount> useramountBeforeAppending = new ArrayList<>(group.getUserAmounts());
                    useramountBeforeAppending.add(new Useramount(userId, userName,userBalance));
                    group.setUserAmounts(useramountBeforeAppending);
                    groupRepository.save(group);

                    // Update user's groups
                    if (user.getUserGroup() == null) {
                        List<String> userGroup = new ArrayList<>();
                        userGroup.add(groupID);
                        user.setUserGroup(userGroup);
                    } else {
                        List<String> userGroup = user.getUserGroup();
                        if (!userGroup.contains(groupID)) {
                            userGroup.add(groupID);
                        }
                        user.setUserGroup(userGroup);
                    }
                    userRepository.save(user);

                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("message", "User added to the group successfully.");
                    return ResponseEntity.ok(jsonResponse.toString());
                } else {
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("message", "Group not found with ID: " + groupID);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
                }
            }
        } catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }


    //list the group details based on group id
    @CrossOrigin
    @GetMapping("/groups/userID/{groupID}")
    @ResponseBody
    public ResponseEntity<?> listGroup(@PathVariable String groupID) {
        try {
            // Fetch the group once
            Optional<Group> groupOpt = groupRepository.findById(groupID);

            if (!groupOpt.isPresent()) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "Group not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse.toString());
            }
            else{
                return ResponseEntity.ok(groupOpt);
            }
//            Group group = groupOpt.get();
//            List<Useramount> userAmounts = group.getUserAmounts();
//
//            // Use a map to store user details to avoid multiple DB calls
//            Map<String, String> userMap = new HashMap<>();
//            for (Useramount eachUserBalance : userAmounts) {
//                Optional<User> userOpt = userRepository.findById(eachUserBalance.getUserID());
//                if (userOpt.isPresent()) {
//                    User user = userOpt.get();
//                    userMap.put(eachUserBalance.getUserID(), user.getUserName());
//                } else {
//                    userMap.put(eachUserBalance.getUserID(), "Unknown"); // Handle user not found
//                }
//            }
//
//            // Build the user amounts list
//            List<Map<String, String>> allUserAmountsName = new ArrayList<>();
//            for (Useramount eachUserBalance : userAmounts) {
//                Map<String, String> userAmountsName = new HashMap<>();
//                userAmountsName.put("userID", eachUserBalance.getUserID());
//                userAmountsName.put("userBalance", eachUserBalance.getUserBalance().toString());
//                userAmountsName.put("userName", userMap.getOrDefault(eachUserBalance.getUserID(), "Unknown"));
//                allUserAmountsName.add(userAmountsName);
//            }
//
//            // Build the response map
//            Map<String, Object> allDetailsGroup = new HashMap<>();
//            allDetailsGroup.put("userAmounts", allUserAmountsName);
//            allDetailsGroup.put("_id", group.getGroupName());
//            allDetailsGroup.put("groupName", group.getGroupName());
//            allDetailsGroup.put("groupOwner", group.getGroupOwner());
//            allDetailsGroup.put("groupDescription", group.getGroupDescription()); // Fixed: should be getGroupDescription
//
//            return ResponseEntity.ok(allDetailsGroup);
        } catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }


    //Display all groups for given user ID
    @CrossOrigin
    @GetMapping("/groups/{userID}")
    @ResponseBody

    public ResponseEntity<?> displayGroup(@PathVariable String userID) {
        try {
            Optional<User> userOptional = userRepository.findById(userID);
            if (!userOptional.isPresent()) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "userid is not valid");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
            } else {
                List<Object> allGroupUser = new ArrayList<>();

                for (Group i : groupRepository.findByuserAmounts(userID)) {
                    Map<String, String> singleGroupUser = new HashMap<>();
                    singleGroupUser.put("name", i.getGroupName());
                    singleGroupUser.put("id", i.get_id());
                    singleGroupUser.put("type",i.getGroupType());
                    for (Useramount userAmount : i.getUserAmounts()) {
                        if (userID.equals(userAmount.getUserID())) {
                            singleGroupUser.put("balance", userAmount.getUserBalance().toString());
                        }
                    }
                    allGroupUser.add(singleGroupUser);
                }
                return ResponseEntity.ok(allGroupUser);
            }
        }
        catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }

    @CrossOrigin
    @GetMapping("/totalUserBalance/{groupID}")
    @ResponseBody

    public ResponseEntity<?> groupDetails(@PathVariable String groupID){
        try {
            Optional<Group> groupOptional = groupRepository.findById(groupID);
            if (!groupOptional.isPresent()) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "group is not empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
            } else {
                Group group = groupOptional.get(); // Use orElse(null) to handle cases where the group is not found
                return ResponseEntity.ok(group);

            }
        }
        catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            e.printStackTrace();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }




    //    @GetMapping("/group/{email}")
//    @ResponseBody
//    public Map<String, List> displayGroup(@PathVariable String email) {
////        System.out.print(groupRepository.findByUsersAmount("viki@gmailcom"));
//
//        Map<String, List> groupNameResult = new HashMap<>();
//        List<String> groupNames = new ArrayList<>();
//        List<Group> groups = groupRepository.findAll();
//        for (Group group : groups) {
//            for (Map.Entry<String, Float> set :
//                    group.getUsersAmount().entrySet()) {
//                String Email = set.getKey();
//                if (Email.equals(email)) {
//                    System.out.println(group.getGroupName());
//                    groupNames.add(group.getGroupName());
//                }
//            }
//        }
//        groupNameResult.put("Result", groupNames);
//        return groupNameResult;
//    }
}


