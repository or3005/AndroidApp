package com.example.finalproject;

import android.widget.ImageView;

public class Show {

    private String bandname;

    private String date;
    private String time;
    private String city;
    private String area;
    private String type;
    private String price;
    private String idInDb;
    private ImageView imageView;

    public Show() {}
    public Show(String bandname, String date, String time, String city, String area, String type, String price) {
        this.bandname = bandname;
        this.date = date;
        this.time = time;
        this.city = city;
        this.area = area;
        this.type = type;
        this.price = price;

    }

    public String getIdInDb() {
        return idInDb;
    }

    public void setIdInDb(String idInDb) {
        this.idInDb = idInDb;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public void setBandname(String bandname) {
        this.bandname = bandname;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBandname() {
        return bandname;
    }



    public String getTime() {
        return time;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }
}
