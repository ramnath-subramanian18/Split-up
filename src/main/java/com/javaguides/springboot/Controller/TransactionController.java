package com.javaguides.springboot.Controller;
import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.Transaction;
import com.javaguides.springboot.beans.User;

import com.javaguides.springboot.beans.Useramount;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.TransactionRepository;
import com.javaguides.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
        public Transaction transaction(@RequestBody Transaction transaction){
        System.out.println(transaction.getTransactionDescription());
        transactionRepository.save(transaction);
        return transaction;
        }
    @CrossOrigin
    @PutMapping(value="/transactions",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteTransaction(@RequestBody Transaction transaction){
        System.out.println(transaction.toString());
        transaction.setTransactionStatus(0);
        transactionRepository.save(transaction);

        return "transaction";
    }
        //display all the details for given transactionID
    @GetMapping("/transactions/{transactionID}")
    @ResponseBody
    public Optional<Transaction> getTransaction(@PathVariable String transactionID ){
        System.out.println(transactionID);
        System.out.println(transactionRepository.findById(transactionID));
        return transactionRepository.findById(transactionID);
    }
    //Display the transactions for given group ID
    @GetMapping("/transactions1/{groupID}")
    @ResponseBody
    public List<Transaction>getTransactionGroup(@PathVariable String groupID){
        List<Transaction> transactions = transactionRepository.findBygroup(groupID);
        System.out.println(transactions);
        Collections.reverse(transactions); // Reverse the list
        System.out.println(transactions);
        return transactions;
    }
    //display all the balances
    @GetMapping("/listUserPrice/{groupID}")
    @ResponseBody
    public List listUserPrice(@PathVariable String groupID){

        ArrayList<Map<String,Float>> userBalance = new ArrayList<>();

        List<Transaction> transaction =transactionRepository.findBygroupTransactionStatus(groupID);
        System.out.println(transaction);
        Map<String, Float> dict1 = new HashMap<>();
        Map<String, Float>  transactionPayeeIDAmount= new HashMap<>();
        for (int i=0;i<transaction.size();i++) {
            float existingValue1=0;
            if(transactionPayeeIDAmount.containsKey(transaction.get(i).getTransactionPayeeID())){
                existingValue1 = transactionPayeeIDAmount.get(transaction.get(i).getTransactionPayeeID());
            }
            transactionPayeeIDAmount.put(transaction.get(i).getTransactionPayeeID(), existingValue1 + transaction.get(i).getTransactionAmount());
            for (Useramount person : transaction.get(i).getUserSplit()) {
//                System.out.println(person.getUserBalance());
                float existingValue=0;
                if(dict1.containsKey(person.getUserID())){
                    existingValue = dict1.get(person.getUserID());
                    dict1.put(person.getUserID(), existingValue + person.getUserBalance()*-1);
                }
                else{
                    dict1.put(person.getUserID(),person.getUserBalance()*-1);
                }
            }
        }
//        System.out.println(dict1);
//        System.out.println(transactionPayeeIDAmount);
        Map <String,Float> total_sum=new HashMap<>(dict1);
        for (Map.Entry<String, Float> entry : transactionPayeeIDAmount.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            //this is merge method in java
            total_sum.merge(key, value, Float::sum);
        }
        System.out.println(total_sum);

        ArrayList<Useramount> useramountBeforeNewValue= new ArrayList<>();

        for (int i=0;i<groupRepository.findById(groupID).get().getUserAmounts().size();i++){
            useramountBeforeNewValue.add(new Useramount(groupRepository.findById(groupID).get().getUserAmounts().get(i).getUserID(),total_sum.get(groupRepository.findById(groupID).get().getUserAmounts().get(i).getUserID())));
        }
        Optional<Group> optionalGroup = groupRepository.findById(groupID);
        Group group = optionalGroup.get();
        group.setUserAmounts(useramountBeforeNewValue);
        groupRepository.save(group);

        while(total_sum.size() != 1) {
            List<String> keyList = new ArrayList<>(total_sum.keySet());
            List<Float> valList = new ArrayList<>(total_sum.values());

            float smallest = Collections.min(valList);
            float largest = Collections.max(valList);
            float value = largest + smallest;

            int positionLarge = valList.indexOf(largest);
            int positionSmall = valList.indexOf(smallest);
            System.out.println(positionSmall);
            System.out.println(positionLarge);
            Map<String, Float> singleSplit = new HashMap<>();
            singleSplit.put(keyList.get(positionLarge),total_sum.get(keyList.get(positionLarge)));
            singleSplit.put(keyList.get(positionSmall),total_sum.get(keyList.get(positionSmall)));
            userBalance.add(singleSplit);
            System.out.println(userBalance);
            total_sum.remove(keyList.get(positionLarge));
            total_sum.remove(keyList.get(positionSmall));
            if(total_sum.size()==1 )
            {
                String onlyKey = total_sum.keySet().iterator().next();
                Float value1 = total_sum.get(onlyKey);
                if(value1==0) {
                    System.out.println("into this");
                    break;
                }
            }
            if(total_sum.size()==0 ){
                break;
            }
            if(largest > smallest*-1){
                total_sum.put(keyList.get(positionLarge), value);
            }
            else if(largest < smallest*-1){
                total_sum.put(keyList.get(positionSmall), value);
            }
            System.out.println(total_sum);
        }
        List<Map<String, String>> finalList = new ArrayList<>();
        for (Map<String, Float> map : userBalance) {
            Map<String, String> userBalanceMap = new HashMap<>();
            for (Map.Entry<String, Float> entry : map.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();
                if(value<0)
                {
                    userBalanceMap.put("borrower",userRepository.findById(key).get().getUserName());


                }
                else
                {
                    userBalanceMap.put("amount",value.toString());
                    userBalanceMap.put("lender",userRepository.findById(key).get().getUserName());
                }

            }
//            System.out.println(userBalanceMap);
            finalList.add(userBalanceMap);
//            System.out.println(finalList);
        }
        return (finalList);
    }
}