package com.example.sanjinpc.edostavahrane.Model;

/**
 * Created by SanjinPc on 4/7/2018.
 */

public class User
{
    private String Name;
    private String Password;
    private String Phone;

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public User() {
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
