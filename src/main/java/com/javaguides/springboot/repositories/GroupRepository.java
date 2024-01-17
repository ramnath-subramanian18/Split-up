package com.javaguides.springboot.repositories;

import com.javaguides.springboot.beans.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
//    Group findByuserGroup(String userID);
//Group findByUsersAmount(String userEmail);
//Group findBygroupNameanduserID(String groupName, String userID);

//List<Group> findByUserGroupAndUsersAmount(String userId, int usersAmount);

}


