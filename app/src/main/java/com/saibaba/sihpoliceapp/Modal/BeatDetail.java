package com.saibaba.sihpoliceapp.Modal;

public class BeatDetail {
    private String uid;
    private String name;
    private double latitude;
    private double longitude;
    private String mobile;
    private String userImg;

    public BeatDetail(String uid, String name, double latitude, double longitude, String mobile, String userImg) {
        this.uid = uid;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mobile = mobile;
        this.userImg = userImg;
    }

    public BeatDetail(){
        uid="";
        name="";
        latitude=25.5941;
        longitude=85.1376;
        mobile="";
        userImg="";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    @Override
    public String toString() {
        return "BeatDetail{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", mobile='" + mobile + '\'' +
                ", userImg='" + userImg + '\'' +
                '}';
    }
}
