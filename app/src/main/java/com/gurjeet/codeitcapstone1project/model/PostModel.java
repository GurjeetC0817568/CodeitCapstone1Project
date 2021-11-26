package com.gurjeet.codeitcapstone1project.model;

//import java.security.Timestamp;
import android.app.Application;
import com.google.firebase.Timestamp;


public class PostModel{
    private String name;
    private String Userid;
    private String price;
    private String details;
    private String condition;
    private String imageURi;
    private Timestamp timeAdd;
    private static PostModel instance;

    public PostModel() {}

    public PostModel(String name,String Userid,String price,String details, String condition, String imageURi, Timestamp timeAdd) {
        this.name = name;
        this.condition = condition;
        this.imageURi = imageURi;
        this.timeAdd = timeAdd;
        this.price =price;
        this.details=details;
        this.Userid=Userid;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImageURi() {
        return imageURi;
    }

    public void setImageURi(String imageURi) {
        this.imageURi = imageURi;
    }

    public Timestamp getTimeAdd() {
        return timeAdd;
    }

    public void setTimeAdd(Timestamp timeAdd) {
        this.timeAdd = timeAdd;
    }


}