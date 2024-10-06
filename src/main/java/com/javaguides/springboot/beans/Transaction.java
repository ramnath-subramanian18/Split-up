package com.javaguides.springboot.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private LocalDateTime timeStamp;
    private LocalDateTime transactionDate;


    private String splitType;
    private Transaction() {
    }
//    Timestamp transactionDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
    int transactionStatus=1;
//    Timestamp transactionUpdateDate=Timestamp.valueOf(LocalDate.now().atStartOfDay());
    public Transaction(String splitType, String groupID, String transactionName, float transactionAmount, LocalDateTime transactionDate, String transactionPayee, ArrayList<Useramount> userSplit, String transactionDescription, Timestamp transactionUpdateDate, int transactionStatus,LocalDateTime timeStamp) {
        this.groupID=groupID;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
//        this.transactionDate= transactionDate;
        this.transactionPayeeID=transactionPayee;
        this.userSplit = userSplit;
        this.transactionDescription=transactionDescription;
        this.timeStamp=timeStamp;
        this.transactionStatus=transactionStatus;
        this.splitType=splitType;
        this.transactionDate=transactionDate;
    }
}