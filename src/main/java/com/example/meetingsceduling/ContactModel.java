package com.example.meetingsceduling;

public class ContactModel {
    int img;
    String name, number;
    public ContactModel(int img, String name, String number){
        this.name = name;
        this.img= img;
        this.number = number;
    }

    public String getName() {
        return name;
    }
    public int getImg()
    {
        return img;
    }
    public String getNumber() {
        return number;
    }

}
