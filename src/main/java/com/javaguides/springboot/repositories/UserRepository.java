package com.javaguides.springboot.repositories;


import com.javaguides.springboot.beans.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByuserEmail(String userEmail);
}
