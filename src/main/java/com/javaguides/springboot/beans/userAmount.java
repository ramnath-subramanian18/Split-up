package com.javaguides.springboot.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class userAmount {
    private String userID;
    private Float userBalance;

    @Override
    public String toString() {
        return "userAmount{" +
                "userID='" + userID + '\'' +
                ", userBalance=" + userBalance +
                '}';
    }

    public userAmount(){}
    public userAmount(String userID,Float userBalance){
        this.userID=userID;
        this.userBalance=userBalance;
    }


}
