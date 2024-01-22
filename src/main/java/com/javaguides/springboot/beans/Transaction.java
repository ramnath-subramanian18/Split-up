package com.javaguides.springboot.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class Transaction {
    private String groupID;
    private String transactionName;
    private float transactionAmount;
    private String transactionPayeeID;
    private List<Useramount> userSplit;
    private String transactionDescription;
    private String _id;
    private Transaction() {
    }
//    Timestamp transactionDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
    int transactionStatus=1;
//    Timestamp transactionUpdateDate=Timestamp.valueOf(LocalDate.now().atStartOfDay());
    public Transaction(String groupID, String transactionName, float transactionAmount, Timestamp transactionDate, String transactionPayee, ArrayList<Useramount> userSplit, String transactionDescription, Timestamp transactionUpdateDate, int transactionStatus) {
        this.groupID=groupID;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
//        this.transactionDate= transactionDate;
        this.transactionPayeeID=transactionPayee;
        this.userSplit = userSplit;
        this.transactionDescription=transactionDescription;
//        this.transactionUpdateDate=transactionUpdateDate;
        this.transactionStatus=transactionStatus;
    }
}