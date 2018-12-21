package com.example.gal.shotafim;

import java.util.Date;

public class Group {

    private String groupID;
    private String groupName;
    private String adminUserID;
    private int numberOfUsers;
    private Date date;
    private Address address;


    /**
     * @param GroupId, groupName
     */

    public Group(String groupID, String groupName, String adminUserID, int numberOfUsers, Address address) {
        this.groupID = groupID;//Need to be automatically generate
        this.groupName = groupName;
        this.adminUserID = adminUserID;//Need to be the creator of the group
        this.numberOfUsers = numberOfUsers; // Need to be at least 2 at most 6
        this.address = address;
        this.date = new Date();

    }
    public Date getDate() {
        return date;
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
