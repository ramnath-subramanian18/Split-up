package com.javaguides.springboot.Service;

import com.javaguides.springboot.beans.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TransactionService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Transaction> fetchTransactions(LocalDateTime startDate, LocalDateTime endDate, String userID) {
        Query query = new Query()
                .addCriteria(Criteria.where("transactionDate").gte(startDate).lte(endDate))
                .addCriteria(Criteria.where("userSplit.userID").is(userID));

        return mongoTemplate.find(query, Transaction.class);
    }
}
