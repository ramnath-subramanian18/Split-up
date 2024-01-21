package com.javaguides.springboot.repositories;

import com.javaguides.springboot.beans.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

//    Group upsert(String _id,Group group);

}


