package com.javaguides.springboot.Controller;

import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.Transactions;
import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.TransactionRepository;
import com.javaguides.springboot.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class TransactionController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TransactionRepository transactionRepository;



    @GetMapping(value = "/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Group getGroupById(@PathVariable String groupId) {
        System.out.println("Group ID: " + groupId);
//        // TODO:: set owner to the user creating the group, read from the token
        return groupRepository.findById(groupId).get();
//        groupRepository.save(group);
//        return "OK";
    }

    /**
     * API to update user to a group.
     *
     * @param group
     * @return
     */
//    @PutMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
////    public String change (@RequestBody Group group){
//    public String change(@RequestBody HashMap<String, Object> group) {
//
//        // TODO:: not working...
//        System.out.println("Enter the Trans123");
////        ObjectId groupId = new ObjectId(String.valueOf(group.get("groupId")));
//        String groupId = String.valueOf(group.get("groupId"));
//        System.out.println("groupId " + groupId);
//        List<String> userEmail = (List<String>) group.get("users");
//        System.out.println(groupId);
//        System.out.println(userEmail);
//
//
//        Optional<Group> groupDetailsOptional = groupRepository.findById(groupId);
//        Group groupDetails = null;
//        if (!groupDetailsOptional.isEmpty()) {
//            groupDetails = groupDetailsOptional.get();
//        }
//
//
//        System.out.println(groupDetails.toString());
//        Set<String> existingUsers = groupDetails.getUsersAmount().keySet();
//        System.out.println(existingUsers);
//
//        for (String email : userEmail) {
//            User userGroup = userRepository.findByuserEmail(email);
//            System.out.println(userGroup.toString());
//        }
//
//
//        return "Test";
//    }

    //    TODO: change to plural, and make it as no caps.
    @PostMapping(value = "/Transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Transactions Transaction(@RequestBody Transactions transaction) {
        System.out.println(transaction.toString());
        Transactions saved = transactionRepository.save(transaction);
        return saved;
    }

    @GetMapping(value = "/transactions/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Transactions getTransactionById(@PathVariable String transactionId) {
        System.out.println(transactionId);
        Transactions saved = transactionRepository.findById(new ObjectId(transactionId)).get();
        return saved;
    }

    @GetMapping("/health")
    public String health() {
        System.out.println("Ok");
        return "OK";
    }
}
