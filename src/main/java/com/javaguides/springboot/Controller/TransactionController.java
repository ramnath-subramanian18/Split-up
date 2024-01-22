package com.javaguides.springboot.Controller;
import com.javaguides.springboot.beans.Transaction;
import com.javaguides.springboot.beans.User;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private GroupRepository groupRepository;
    @GetMapping("/health")
    public String health() {
        System.out.println("Ok");
        return "OK";
    }
    //Send post request to save a transaction
    @PostMapping(value="/transactions",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public Transaction transaction(@RequestBody Transaction transaction){
        System.out.println(transaction.getTransactionDescription());
        transactionRepository.save(transaction);
        return transaction;
        }
        //display all the details for given transactionID
    @GetMapping("/transactions")
    @ResponseBody
    public Optional<Transaction> getTransaction(@RequestParam String transactionID ){
        System.out.println(transactionID);
        System.out.println(transactionRepository.findById(transactionID));
        return transactionRepository.findById(transactionID);
    }
    //Display the transactions for given group ID

    @GetMapping("/transactions/{groupID}")
    @ResponseBody
    public List<Transaction>getTransactionGroup(@PathVariable String groupID){
        return transactionRepository.findByGroupID(groupID);

    }
}
