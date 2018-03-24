package com.example.optimus.tezattendance;

/**
 * Created by optimus on 24/3/18.
 */

public class Users {

    public String type, idd, password, className, fireUID;

    public Users(){

    }

    public Users(String type, String idd, String password, String className, String fireUID) {
        this.type = type;
        this.idd = idd;
        this.password = password;
        this.className = className;
        this.fireUID = fireUID;
    }
}
