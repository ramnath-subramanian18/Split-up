package com.javaguides.springboot.Service;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class StringLog {


    public String convertString(Exception message){
        String logMessage = String.format(
                "{\"timestamp\": \"%s\", \"level\": \"ERROR\", \"message\": \"%s\", \"exception\": \"%s\"}",
                LocalDateTime.now(),
                message.getMessage(),
                message.toString() // This can give you a more detailed exception description
        );
        return logMessage;
    }
}
