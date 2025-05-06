package com.eventticket.booking.model;

public class Admin {
    private String adminId;
    private String userName;
    private String password;

    public Admin(){

    }

    public Admin(String adminId,String userName,String password){
        this.adminId=adminId;
        this.userName=userName;
        this.password=password;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(){
        this.userName=userName;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(){
        this.password=password;
    }
}
