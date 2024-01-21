package com.javaguides.springboot.Controller;
import com.javaguides.springboot.beans.Transaction;
import com.javaguides.springboot.repositories.GroupRepository;
import com.javaguides.springboot.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
        //display all the transaction for given group
    @GetMapping("/transactions")
    @ResponseBody
    public List transaction(@RequestParam String groupID ){
        List <Object> totalgroup=new ArrayList<>();
        System.out.println(groupID);
        totalgroup.add(groupRepository.findById(groupID));
        System.out.println(groupRepository.findById(groupID));
        return totalgroup;
    }

}
