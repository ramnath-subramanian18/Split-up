package com.javaguides.springboot.Controller;

import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.UserRepository;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Group createGroup(@RequestBody Group group) {
        System.out.println("Create a group");
        System.out.println(group.toString());
        // TODO:: set owner to the user creating the group, read from the token
        groupRepository.save(group);
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
    @GetMapping("/groups")
    @ResponseBody

    public List displayGroup(@RequestParam String userID ) {
        System.out.println(userID);
        User user=(userRepository.findById(userID).get());
        List<Object> allGroup = new ArrayList<>();
        for (Object i:user.getUserGroup()){
            allGroup.add(groupRepository.findById(i.toString()).get());
        }
        return allGroup;
    }
}


