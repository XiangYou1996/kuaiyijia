package com.example.kuaiyijia.Entity;

import java.io.Serializable;

/*
Author by: xy
Coding On 2021/4/20;
*/
public class UserEntity implements Serializable {
    private String UserID;
    private String account;
    private String password;
    private String HYB_ID;
    private String C_ID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHYB_ID() {
        return HYB_ID;
    }

    public void setHYB_ID(String HYB_ID) {
        this.HYB_ID = HYB_ID;
    }

    public String getC_ID() {
        return C_ID;
    }

    public void setC_ID(String c_ID) {
        C_ID = c_ID;
    }
}
