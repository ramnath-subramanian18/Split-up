package com.javaguides.springboot.beans;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
public class Useramount {
    private String userID;
    private Float userBalance;

    @Override
    public String toString() {
        return "userAmount{" +
                "userID='" + userID + '\'' +
                ", userBalance=" + userBalance +

                '}';
    }

    public Useramount(){}
    public Useramount(String userID, Float userBalance){
        this.userID=userID;
        this.userBalance=userBalance;
    }

}
