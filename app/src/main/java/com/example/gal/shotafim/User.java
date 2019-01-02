package com.example.gal.shotafim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class User {

    private String userID;
    private String Name;
    private String Email;
    private String mGroupName;
    private String Password;//HashedPassword
    private String Date;
    private double Credit;

    public User(String name, String email, String password) {
        this.userID = UUID.randomUUID().toString();
        Name = name;
        Email = email.replace(".","|").toLowerCase();
        Password = password;
        Date = createDate();
        Credit = 0;
    }
    public User(String userID, String name, String email, String password, String groupName) {
        this.userID = userID;
        Name = name;
        Email = email.replace(".","|").toLowerCase();
        mGroupName = groupName;
        Password = password;
        Date = createDate();
        Credit = 0;

    }
    public User (){
    }
    /*
        Methods.
     */
    private String createDate() {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E dd/MM/YYYY HH:mm:ss");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return ft.format(d);
    }

    /*
        Getters & setters.
     */
    public String getUserID() {
        return userID;
    }

    public String getmGroupName() {
        return mGroupName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDate() {
        return Date;
    }

    public double getCredit() {
        return Credit;
    }

    public void setCredit(double credit) {
        Credit = credit;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", Name='" + Name + '\'' +
                ",GroupName= '" + mGroupName + '\'' +
                ", Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                ", Date=" + Date +
                ", Credit=" + Credit +
                '}';
    }
}
