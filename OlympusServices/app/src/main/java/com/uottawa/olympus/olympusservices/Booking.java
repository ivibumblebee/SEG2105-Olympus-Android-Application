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
    private Status status;

    int rating; //out of 5

    public Booking(int starth, int startmin, int endh, int endmin, int day, int month, int year,
                   String serviceprovider, String homeowner){
        this.starth = starth;
        this.startmin = startmin;
        this.endh = endh;
        this.endmin = endmin;
        this.day = day;
        this.month = month;
        this.year = year;
        this.serviceprovider = serviceprovider;
        this.homeowner = homeowner;
        this.status = Status.PENDING;
    }


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

    public void setStatus(Status status){
        this.status = status;
    }

    public Status getStatus(){
        return this.status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


}
