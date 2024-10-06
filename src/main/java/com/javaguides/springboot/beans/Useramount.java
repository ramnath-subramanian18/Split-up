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
    private String userName;
    private Float userBalance;


    @Override
    public String toString() {
        return "Useramount{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", userBalance=" + userBalance +
                '}';
    }

    public Useramount(){}
    public Useramount(String userID, String userName,Float userBalance){
        this.userID=userID;
        this.userName=userName;
        this.userBalance=userBalance;

    }

}
