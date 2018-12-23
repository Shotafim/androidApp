package com.example.gal.shotafim;

public class AuthenticatedUserHolder {

    private User appUser = null;

    private AuthenticatedUserHolder() { }

    public static final AuthenticatedUserHolder instance = new AuthenticatedUserHolder();

    public User getAppUser() { return this.appUser; }

    public void setAppUser(User user) { this.appUser = user; }

}

