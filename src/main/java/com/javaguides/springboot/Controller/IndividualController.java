package com.javaguides.springboot.Controller;

import com.javaguides.springboot.Service.IndividualService;
import com.javaguides.springboot.Service.TransactionService;
import com.javaguides.springboot.beans.Individual;
import com.javaguides.springboot.beans.Transaction;
import com.javaguides.springboot.beans.Useramount;
import com.javaguides.springboot.repositories.IndividualRepository;
import com.javaguides.springboot.repositories.TransactionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.javaguides.springboot.Service.LogglyService;
import com.javaguides.springboot.Service.StringLog;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IndividualController {
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private IndividualService individualService;
    @Autowired
    private LogglyService logglyService;
    @Autowired
    private StringLog stringLog;
    @Autowired
    private TransactionService transactionService;
    @CrossOrigin
    @PostMapping(value="/individualTransaction",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> transaction (@RequestBody Individual individual){
        try {
            individualRepository.save(individual);
            return ResponseEntity.ok(individual);
        }
        catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }
    }


    @CrossOrigin
    @GetMapping("/dashboard/individual/{userId}")
    @ResponseBody
    public ResponseEntity<?> transactionsDashboard(@PathVariable String userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate ) {
        try {
            List<Individual> IndividualTransaction = (individualService.fetchIndividualTransactions(startDate, endDate, userId));
            Map<String, Double> dict1 = new HashMap<>();

            for (Individual individualTransaction : IndividualTransaction) {
                // Initialize the map entry if it does not exist
                if (!dict1.containsKey(individualTransaction.getTransactionDescription())) {
                    dict1.put(individualTransaction.getTransactionDescription(), 0.0);
                }


                if (individualTransaction.getUserId().equals(userId)) {
                    // Update the map with the accumulated balance
                    dict1.put(individualTransaction.getTransactionDescription(),
                            dict1.get(individualTransaction.getTransactionDescription()) + individualTransaction.getTransactionAmount());
                }
            }

            // Log the dictionary to verify the results

            return ResponseEntity.ok(dict1);
        } catch (Exception e) {
            String logMessage = stringLog.convertString(e);
            logglyService.sendLog(logMessage);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Try Later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse.toString());
        }

    }
}
