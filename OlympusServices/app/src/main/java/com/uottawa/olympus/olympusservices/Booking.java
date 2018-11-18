package com.uottawa.olympus.olympusservices;

public class Booking {
    private int starth;
    private int startmin;
    private int endh;
    private int endmin;
    private int day;
    private int month;
    private int year;
    private String serviceprovider; //username
    private String homeowner; //username
    public enum Status {
        PENDING, CONFIRMED, CANCELLED
    }
    int rating; //out of 5


    public int getStarth() {
        return starth;
    }

    public void setStarth(int starth) {
        this.starth = starth;
    }

    public int getStartmin() {
        return startmin;
    }

    public void setStartmin(int startmin) {
        this.startmin = startmin;
    }

    public int getEndh() {
        return endh;
    }

    public void setEndh(int endh) {
        this.endh = endh;
    }

    public int getEndmin() {
        return endmin;
    }

    public void setEndmin(int endmin) {
        this.endmin = endmin;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getServiceprovider() {
        return serviceprovider;
    }

    public void setServiceprovider(String serviceprovider) {
        this.serviceprovider = serviceprovider;
    }

    public String getHomeowner() {
        return homeowner;
    }

    public void setHomeowner(String homeowner) {
        this.homeowner = homeowner;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


}
