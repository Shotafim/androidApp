package com.example.gal.shotafim;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Group {

    private String groupID;
    private String groupName;
    private String adminUserID;
    private int numberOfUsers;
    private String date;
    private Address address;

    /**
     * @param groupID,groupName,adminUserID,numberOfUsers,address
     */

    public Group(String groupID, String groupName, String adminUserID, int numberOfUsers, Address address) {
        this.groupID = groupID;//Need to be automatically generate
        this.groupName = groupName;
        this.adminUserID = adminUserID;//Need to be the creator of the group
        this.numberOfUsers = numberOfUsers; // Need to be at least 2 at most 6
        this.address = address;
        this.date = createDate();

    }
    public String getDate() {
        return date;
    }

    private String createDate() {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E dd.mm.yyy HH:mm:ss");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return ft.format(d);
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdminUserID() {
        return adminUserID;
    }

    public void setAdminUserID(String adminUserID) {
        this.adminUserID = adminUserID;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
