package com.javaguides.springboot.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@ToString
public class Transaction {
    private String transactionName;
    private float transactionAmount;
    private Timestamp transactionDate;
    private String transactionPayee;
    private List<UserSplit> users;

    private Transaction() {
    }

    public Transaction(String transactionName, float transactionAmount, Timestamp transactionDate, String transactionPayee, List users) {
        this.transactionAmount = transactionAmount;
        this.transactionName = transactionName;
        this.transactionDate = transactionDate;
        this.transactionPayee = transactionPayee;
        this.users = users;
    }
}