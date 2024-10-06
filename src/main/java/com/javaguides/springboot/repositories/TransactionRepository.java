package com.javaguides.springboot.repositories;

import com.javaguides.springboot.beans.Transaction;

import com.javaguides.springboot.beans.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String>{
    @Query("{'groupID': ?0}")
    List<Transaction> findBygroup(String id);
    @Query("{'groupID':?0,'transactionStatus':1}")
    List<Transaction> findBygroupTransactionStatus(String id);
    @Query("{'userSplit.userID':?0}")
    List<Transaction> findBygroupTransactionUserId(String id);
    @Query("{'transactionDate': {'$gt': ?0, '$lt': ?1,'userSplit.userID':?2}}")
    List<Transaction> findByTransactionDateBetweenAndUserSplitUserID(LocalDateTime startDate, LocalDateTime endDate,String id);




}
