package com.javaguides.springboot.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSplit {
    public String userId;
    public Float percentage;

    public UserSplit() {
    }

    public UserSplit(String userId, Float percentage) {
        this.userId = userId;
        this.percentage = percentage;
    }
}