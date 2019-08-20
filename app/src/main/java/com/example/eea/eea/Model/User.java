package com.example.eea.eea.Model;

import java.util.List;


public class  User {

    private int uid;
    private String username;
    private String password;
    private String emailAddress;
    private String firstName;
    private String residentAddress;
    private String profileimg;


    public User() {
    }

    public User(String username, String password, String emailAddress, String firstName, String residentAddress, String profileimg) {

        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.residentAddress = residentAddress;
        this.profileimg = profileimg;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getResidentAddress() {
        return residentAddress;
    }

    public void setResidentAddress(String residentAddress) {
        this.residentAddress = residentAddress;
    }


}