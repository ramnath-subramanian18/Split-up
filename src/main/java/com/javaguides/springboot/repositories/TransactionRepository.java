package com.javaguides.springboot.repositories;

import com.javaguides.springboot.beans.Transaction;

import com.javaguides.springboot.beans.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String>{
//    Transaction findByuserGroupID(String id);
}
