package com.example.finalproject;

public class Reservations {
    String email;
    String fullName;
    String bandname;
    String date;
    String time;
    String numberOfTickets;
    String price;
    String Showid;
    String city;

    public Reservations(){}

    public Reservations(String email, String fullName, String bandname, String date, String time, String numberOfTickets, String price, String Showid, String city) {
        this.email = email;
        this.fullName = fullName;
        this.bandname = bandname;
        this.date = date;
        this.time = time;
        this.numberOfTickets = numberOfTickets;
        this.price = price;
        this.Showid = Showid;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String get_teams_string() {
        return bandname;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNumberOfTickets() {
        return numberOfTickets;
    }

    public String getPrice() {
        return price;
    }

    public String getShowId() {
        return Showid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void set_teams_string(String teams_string) {
        this.bandname = teams_string;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNumberOfTickets(String numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setShowId(String gameId) {
        this.Showid = gameId;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
