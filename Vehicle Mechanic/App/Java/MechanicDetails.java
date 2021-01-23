package com.example.admin.vehiclemechanic;

public class MechanicDetails {
    private  String fname,lname,mnum,email,password;

    public MechanicDetails(String fname, String lname, String mnum, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.mnum = mnum;
        this.email = email;
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getMnum() {
        return mnum;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
