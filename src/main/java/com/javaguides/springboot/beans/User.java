package com.javaguides.springboot.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("user")
@Setter
@Getter
public class User {

    @Id
    private String _id;
    private String userName;
    private String userEmail;
    private String userPassword;
    private List<String> userGroup;
    public String stringValue;


    //private int id;
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userAmount=" + userGroup +
                '}';
    }

    public User(String userName, String userEmail, String userPassword, ArrayList<String> userGroup) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userGroup=userGroup;
    }
    public User(String value){
        this.stringValue=value;
    }


}
