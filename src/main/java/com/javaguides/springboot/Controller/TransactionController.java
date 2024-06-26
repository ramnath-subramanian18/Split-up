package com.javaguides.springboot.Controller;
import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.Transaction;
import com.javaguides.springboot.beans.User;

import com.javaguides.springboot.beans.Useramount;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.TransactionRepository;
import com.javaguides.springboot.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/health")
    public String health() {
        System.out.println("Ok");
        return "OK";
    }
    //Send post request to save a transaction
    @CrossOrigin
    @PostMapping(value="/transactions",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

        public ResponseEntity<?> transaction (@RequestBody Transaction transaction){
        try {
            System.out.println("transaction adding");
            Map<String, Float> transactionAmount = new HashMap<>();
            Map<String, Float> transactionAmountForm = new HashMap<>();
            List<Transaction> transactionOptional = transactionRepository.findBygroup(transaction.getGroupID());
            if (!transactionOptional.isEmpty()) {

                for (int i = 0; i < transactionRepository.findBygroup(transaction.getGroupID()).get(0).getUserSplit().size(); i++) {
                    System.out.println("into first for ");
                    System.out.println(transactionRepository.findBygroup(transaction.getGroupID()).get(0).getUserSplit().get(i).getUserID());
                    transactionAmount.put(transactionRepository.findBygroup(transaction.getGroupID()).get(0).getUserSplit().get(i).getUserID(), 0.0f);
                }
                for (int i = 0; i < transaction.getUserSplit().size(); i++) {

                    transactionAmountForm.put(transaction.getUserSplit().get(i).getUserID(), transaction.getUserSplit().get(i).getUserBalance());
                }
                for (Map.Entry<String, Float> entry : transactionAmount.entrySet()) {

                    String key = entry.getKey();
                    Float value = entry.getValue();
                    transactionAmountForm.merge(key, value, Float::sum);
                }

                List<Useramount> userSplit = new ArrayList<>();
                for (Map.Entry<String, Float> entry : transactionAmountForm.entrySet()) {

                    String userName = entry.getKey();
                    Float userBalance = entry.getValue();
                    userSplit.add(new Useramount(userName, userBalance));
                }

                transaction.setUserSplit(userSplit);
                transactionRepository.save(transaction);
                return ResponseEntity.ok(transaction);
            }
            else {
                transactionRepository.save(transaction);

                return ResponseEntity.ok(transaction);
//                JSONObject jsonResponse = new JSONObject();
//                jsonResponse.put("message", "group id is not valid");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());

            }
        }
        catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }

    @CrossOrigin
    @PutMapping(value="/transactions")
    public ResponseEntity<?> deleteTransaction(@RequestBody Transaction transaction){
        try {
//        System.out.println(transaction.toString());
            transaction.setTransactionStatus(0);
            transactionRepository.save(transaction);
           return ResponseEntity.ok(transaction);
        }
        catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }
        //display all the details for given transactionID
        @CrossOrigin
    @GetMapping("/transactions/{transactionID}")
    @ResponseBody
    public ResponseEntity<?> getTransaction(@PathVariable String transactionID ){
        try {
            Optional<Transaction> transactionOptional = transactionRepository.findById(transactionID);
            if (!transactionOptional.isEmpty()) {
                return ResponseEntity.ok(transactionRepository.findById(transactionID));
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "transactionID is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse.toString());
            }
        }
        catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }
    //Display the transactions for given group ID
    @CrossOrigin
    @GetMapping("/transactions1/{groupID}")
    @ResponseBody
    public ResponseEntity<?> getTransactionGroup(@PathVariable String groupID){
        System.out.println("into transaction1");
        try {
            List<Transaction> transactions = transactionRepository.findBygroup(groupID);
            System.out.println("into try");
            if (transactions.size() != 0) {
                List<Transaction> undeletedTransaction = new ArrayList<>();
                Collections.reverse(transactions); // Reverse the list
                for (int i = 0; i < transactions.size(); i++) {
                    if (transactions.get(i).getTransactionStatus() == 1) {
                        undeletedTransaction.add(transactions.get(i));
                    }
                }
                return ResponseEntity.ok(undeletedTransaction);
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("message", "transactionID is empty");
                return ResponseEntity.ok(jsonResponse.toString());
            }
        }
        catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }
    //display all the balances
    @CrossOrigin
    @GetMapping("/listUserPrice/{groupID}")
    @ResponseBody
    public ResponseEntity<?> listUserPrice(@PathVariable String groupID) {
        System.out.println(groupID);
        List<Transaction> transaction1 = transactionRepository.findBygroupTransactionStatus(groupID);
        System.out.println("transaction");
        System.out.println(transaction1);
        if(transaction1.isEmpty())
        {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "transaction is empty");
            return ResponseEntity.ok(jsonResponse.toString());
        }
        try {

            ArrayList<Map<String, Float>> userBalance = new ArrayList<>();

            List<Transaction> transaction = transactionRepository.findBygroupTransactionStatus(groupID);
            System.out.println("transaction");
            System.out.println(transaction);
//        System.out.println(transaction);
            Map<String, Float> dict1 = new HashMap<>();
            Map<String, Float> transactionPayeeIDAmount = new HashMap<>();
            for (int i = 0; i < transaction.size(); i++) {
                float existingValue1 = 0;
                if (transactionPayeeIDAmount.containsKey(transaction.get(i).getTransactionPayeeID())) {
                    existingValue1 = transactionPayeeIDAmount.get(transaction.get(i).getTransactionPayeeID());
                }
                transactionPayeeIDAmount.put(transaction.get(i).getTransactionPayeeID(), existingValue1 + transaction.get(i).getTransactionAmount());
                for (Useramount person : transaction.get(i).getUserSplit()) {
//                System.out.println(person.getUserBalance());
                    float existingValue = 0;
                    if (dict1.containsKey(person.getUserID())) {
                        existingValue = dict1.get(person.getUserID());
                        dict1.put(person.getUserID(), existingValue + person.getUserBalance() * -1);
                    } else {
                        dict1.put(person.getUserID(), person.getUserBalance() * -1);
                    }
                }
            }
//        System.out.println(dict1);
//        System.out.println(transactionPayeeIDAmount);
            Map<String, Float> total_sum = new HashMap<>(dict1);
            for (Map.Entry<String, Float> entry : transactionPayeeIDAmount.entrySet()) {
                String key = entry.getKey();
                float value = entry.getValue();
                //this is merge method in java
                total_sum.merge(key, value, Float::sum);
            }
//        System.out.println("total_sum");
//        System.out.println(total_sum);

            ArrayList<Useramount> useramountBeforeNewValue = new ArrayList<>();

            for (int i = 0; i < groupRepository.findById(groupID).get().getUserAmounts().size(); i++) {
                useramountBeforeNewValue.add(new Useramount(groupRepository.findById(groupID).get().getUserAmounts().get(i).getUserID(), total_sum.get(groupRepository.findById(groupID).get().getUserAmounts().get(i).getUserID())));
            }
            Optional<Group> optionalGroup = groupRepository.findById(groupID);
            Group group = optionalGroup.get();
            group.setUserAmounts(useramountBeforeNewValue);
            groupRepository.save(group);

            while (total_sum.size() != 1) {
                List<String> keyList = new ArrayList<>(total_sum.keySet());
                List<Float> valList = new ArrayList<>(total_sum.values());

                float smallest = Collections.min(valList);
                float largest = Collections.max(valList);
                float value = largest + smallest;

                int positionLarge = valList.indexOf(largest);
                int positionSmall = valList.indexOf(smallest);
//            System.out.println("positionSmall");
//            System.out.println(positionSmall);
//            System.out.println("positionLarge");
//            System.out.println(positionLarge);
                Map<String, Float> singleSplit = new HashMap<>();
                if (total_sum.get(keyList.get(positionLarge)) > -1 * total_sum.get(keyList.get(positionSmall))) {
                    singleSplit.put(keyList.get(positionLarge), total_sum.get(keyList.get(positionSmall)) * -1);
                    singleSplit.put(keyList.get(positionSmall), total_sum.get(keyList.get(positionSmall)));
                } else {
                    singleSplit.put(keyList.get(positionLarge), total_sum.get(keyList.get(positionLarge)));
                    singleSplit.put(keyList.get(positionSmall), total_sum.get(keyList.get(positionLarge)) * -1);
                }

//            System.out.println("user split");
                userBalance.add(singleSplit);
//            System.out.println(userBalance);
                total_sum.remove(keyList.get(positionLarge));
                total_sum.remove(keyList.get(positionSmall));
                if (total_sum.size() == 1) {
                    String onlyKey = total_sum.keySet().iterator().next();
                    Float value1 = total_sum.get(onlyKey);
                    if (value1 == 0) {
//                    System.out.println("into this");
                        break;
                    }
                }
                if (total_sum.size() == 0) {
                    break;
                }
                if (largest > smallest * -1) {
                    total_sum.put(keyList.get(positionLarge), value);
                } else if (largest < smallest * -1) {
                    total_sum.put(keyList.get(positionSmall), value);
                }
//            System.out.println(total_sum);
            }
            List<Map<String, String>> finalList = new ArrayList<>();
            for (Map<String, Float> map : userBalance) {
                Map<String, String> userBalanceMap = new HashMap<>();
                for (Map.Entry<String, Float> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Float value = entry.getValue();
                    if (value < 0) {
                        userBalanceMap.put("borrower", userRepository.findById(key).get().getUserName());


                    } else {
                        userBalanceMap.put("amount", value.toString());
                        userBalanceMap.put("lender", userRepository.findById(key).get().getUserName());
                    }

                }
//            System.out.println(userBalanceMap);
                finalList.add(userBalanceMap);
//            System.out.println(finalList);
            }
            return ResponseEntity.ok(finalList);
//            return (finalList);
        }
        catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }
}