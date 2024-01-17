package com.javaguides.springboot.repositories;

import com.javaguides.springboot.beans.Transactions;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transactions, ObjectId> {
}
