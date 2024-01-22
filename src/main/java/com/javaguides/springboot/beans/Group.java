package com.javaguides.springboot.beans;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;

@Setter
@Getter
@Document(collection = "group")
public class Group {
    @Id
    private String _id;
    private String groupName;
    private ArrayList<userAmount> userAmounts;
    private String groupOwner;
    private String groupDescription;

    @Override
    public String toString() {
        return "Group{" +
                "_id='" + _id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", userAmounts=" + userAmounts +
                ", groupOwner='" + groupOwner + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                '}';
    }
    public Group() {
    }

    public Group( String _id, String groupName, String groupOwner,String groupDescription,ArrayList<userAmount> userAmounts){
        this._id=_id;
        this.groupName=groupName;
        this.groupOwner=groupOwner;
        this.groupDescription=groupDescription;
        this.userAmounts = userAmounts;
    }
}
