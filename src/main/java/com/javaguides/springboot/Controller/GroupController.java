package com.javaguides.springboot.Controller;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.beans.Useramount;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.swing.text.html.Option;
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
    //Create a group
    @CrossOrigin
    @PostMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createGroup(@RequestBody Group group) {



        Optional<User> userOptional1=userRepository.findById(group.getUserAmounts().get(0).getUserID());
        if (userOptional1.isPresent()) {
            User user = userOptional1.get();
            List<String> userGroup = user.getUserGroup();
            System.out.println(userGroup);
            for (String groupID : userGroup) {
                System.out.println(groupID);
                System.out.println(groupRepository.findById(groupID).get().getGroupName());
//
                if(groupRepository.findById(groupID).get().getGroupName().equals(group.getGroupName())) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("error", "Group already Exists");
                    return hashMap;
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
            return group;

        }
        else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("error","user name doesn't exists");
            return hashMap;
        }

    }
    //Put request to add a user to a group
    // group id is added to the user table and group table
    @CrossOrigin
    @PutMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editGroup(@RequestBody Map<String, String> requestBody) {
        System.out.println("Request Body: " + requestBody.get("emailId"));
        if(userRepository.findByuserEmail(requestBody.get("emailId"))!=null){
            User user = userRepository.findByuserEmail(requestBody.get("emailId"));
            System.out.println(user);
            String userId = user.get_id().toString();
            Float userBalance = 0.0F;

            Optional<Group> optionalGroup = groupRepository.findById(requestBody.get("groupID"));

            if (optionalGroup.isPresent()) {
                Group group = optionalGroup.get();
                ArrayList<Useramount> useramountBeforeAppending = new ArrayList<>(group.getUserAmounts());
                useramountBeforeAppending.add(new Useramount(userId, userBalance));
                group.setUserAmounts(useramountBeforeAppending);
                groupRepository.save(group);
            } else {
                System.out.println("Group not found with ID: " + requestBody.get("groupID"));
            }

            System.out.println(user);
            if (user.getUserGroup() == null) {
//            System.out.println("null");
                List<String> userGroup = new ArrayList<>();
                ;
                userGroup.add(requestBody.get("groupID"));
                user.setUserGroup(userGroup);
                userRepository.save(user);
//            System.out.println(userGroup);
            } else {
                List<String> userGroup = user.getUserGroup();
                userGroup.add(requestBody.get("groupID"));
                user.setUserGroup(userGroup);
                userRepository.save(user);
                System.out.println(userGroup);
            }

            return ResponseEntity.ok().body(Collections.singletonMap("code", "Completed"));
        }
        else  {

            // If an error occurs
            return ResponseEntity.ok().body(Collections.singletonMap("code", "Error"));
        }
    }

    //list the group details based on group id
    @CrossOrigin
    @GetMapping("/groups/userID/{groupID}")
    @ResponseBody
    public HashMap listGroup(@PathVariable String groupID ){
        Optional<Group> group=groupRepository.findById(groupID);
        HashMap allDetailsGroup = new HashMap();
        List<HashMap<String ,String>> allUserAmountsName = new ArrayList<HashMap<String ,String>>();
//        System.out.println(group.get().getUserAmounts());
        for (Useramount eachUserBalance:group.get().getUserAmounts()){
            String userName=(userRepository.findById(eachUserBalance.getUserID()).get().getUserName());
            HashMap userAmountsName = new HashMap();
            userAmountsName.put("userID",eachUserBalance.getUserID());
            userAmountsName.put("userBalance",eachUserBalance.getUserBalance());
            userAmountsName.put("userName",userName);
            allUserAmountsName.add(userAmountsName);
        }
        allDetailsGroup.put("_id",group.get().getGroupName());
        allDetailsGroup.put("groupName",group.get().getGroupName());
        allDetailsGroup.put("userAmounts",allUserAmountsName);
        allDetailsGroup.put("groupOwner",group.get().getGroupOwner());
        allDetailsGroup.put("groupDescription",group.get().getGroupOwner());
        System.out.println(allDetailsGroup);
//        System.out.println(groupRepository.findById(groupID));
//        return groupRepository.findById(groupID);
        return allDetailsGroup;
    }


    //Display all groups for given user ID
    @CrossOrigin
    @GetMapping("/groups/{userID}")
    @ResponseBody

    public List<Object> displayGroup(@PathVariable String userID) {

//        System.out.println(userID);
        User user=(userRepository.findById(userID).get());
//        System.out.println(user);
        List<Object> allGroupUser = new ArrayList<>();
//        System.out.println(groupRepository.findByuserAmounts(userID));
        for (Group i:groupRepository.findByuserAmounts(userID)) {
            Map<String, String> singleGroupUser = new HashMap<>();
            singleGroupUser.put("name", i.getGroupName());
            singleGroupUser.put("id", i.get_id());
            for (Useramount userAmount : i.getUserAmounts()) {
                if (userID.equals(userAmount.getUserID())) {
                    singleGroupUser.put("balance", userAmount.getUserBalance().toString());
                }
            }
            allGroupUser.add(singleGroupUser);
        }
        return allGroupUser;
    }

    @CrossOrigin
    @PostMapping("/groups1")
    @ResponseBody
    public Object groupDetails(@RequestBody Map<String, String> requestBody){
        System.out.println("request body");
        System.out.println(requestBody);
        String id = requestBody.get("id");
        Group group = groupRepository.findById(id).orElse(null); // Use orElse(null) to handle cases where the group is not found
        return group;
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


