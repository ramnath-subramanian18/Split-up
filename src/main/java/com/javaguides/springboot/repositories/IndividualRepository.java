package com.javaguides.springboot.repositories;

import com.javaguides.springboot.beans.Individual;
import com.javaguides.springboot.beans.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

//public class IndividualRepository {
//}



public interface IndividualRepository extends MongoRepository<Individual, String> {
}
