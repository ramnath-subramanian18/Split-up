package com.javaguides.springboot.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@ToString
public class Transactions extends Transaction {
    @Id
    private String _id;
    private String groupID;

    public Transactions(String transactionName, float transactionAmount, Timestamp transactionDate, String transactionPayee, List users) {
        super(transactionName, transactionAmount, transactionDate, transactionPayee, users);
    }
//    private Transaction transaction;
//    public Transactions(){}
    /*public Transactions(String groupID,Transaction T){
        this.groupID=groupID;
        this.transaction=T;
    }*/

}

