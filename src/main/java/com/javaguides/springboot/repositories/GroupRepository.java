package com.javaguides.springboot.repositories;

import com.javaguides.springboot.beans.Group;
import com.javaguides.springboot.beans.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    @Query(" {'userAmounts.userID':?0}")
    List<Group> findByuserAmounts(String id);

    Group findBygroupName(String groupName);


//    Group upsert(String _id,Group group);

}


