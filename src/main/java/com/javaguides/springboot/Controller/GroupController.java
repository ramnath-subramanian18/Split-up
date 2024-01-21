package com.javaguides.springboot.Controller;
import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.UserRepository;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Group createGroup(@RequestBody Group group) {
        System.out.println("Create a group");
        System.out.println(group.toString());
        // TODO:: set owner to the user creating the group, read from the token
        groupRepository.save(group);
        return group;
    }
    //Put request to add a user to a group
    // group id is added to the user table and group table
    @PutMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String editGroup(@RequestBody Map<String, String> requestBody) {
        System.out.println("Request Body: " + requestBody.get("userEmail"));
        User user=userRepository.findByuserEmail(requestBody.get("userEmail"));
        String userId=user.get_id().toString();
        String userBalance="0";
        ArrayList<Map<String, Object>> userAmountBeforeAppending = groupRepository.findById(requestBody.get("groupID")).get().getUserAmount();
        System.out.println(userAmountBeforeAppending);
        Map<String, Object> newUserEntry = new HashMap<>();
        newUserEntry.put("userID", userId);
        newUserEntry.put("userBalance", userBalance);
        userAmountBeforeAppending.add(newUserEntry);
        System.out.println(userAmountBeforeAppending);
        Query query=new Query().addCriteria(Criteria.where("_id").is(requestBody.get("groupID")));
        Update updateDefinition = new Update().set("userAmount", userAmountBeforeAppending);
        UpdateResult updateresult= mongoTemplate.upsert(query,updateDefinition,"group");
        ArrayList<String> userGroup = user.getUserGroup();
        userGroup.add(requestBody.get("groupID"));
        user.setUserGroup(userGroup);
        userRepository.save(user);
        System.out.println(userGroup);
        return "Done";
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

    //Display all groups for given user ID
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


