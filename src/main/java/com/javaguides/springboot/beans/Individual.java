package com.javaguides.springboot.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(collection = "individual")
public class Individual {
    @Id

    private String _id;
    private String transactionName;
    private String transactionDescription;
    private LocalDateTime timeStamp;
    private LocalDateTime transactionDate;
    private String userId;
    private Float transactionAmount;


    //default consurucotr
    private Individual()
    {

    }

    //paramter based used of initializes the object
    public Individual(String transactionName,  LocalDateTime transactionDate,  String transactionDescription, LocalDateTime timeStamp,String userId,Float transactionAmount) {
        this.transactionName = transactionName;
        this.transactionDescription=transactionDescription;
        this.timeStamp=timeStamp;
        this.transactionDate=transactionDate;
        this.userId=userId;
        this.transactionAmount=transactionAmount;
    }
}
