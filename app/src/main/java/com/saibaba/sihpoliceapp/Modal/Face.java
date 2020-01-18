package com.saibaba.sihpoliceapp.Modal;

import java.io.Serializable;

public class Face implements Serializable {
    private String faceID,personID;
    private final int top;
    private final int left;
    private final int width;
    private final int height;
    private double probability;
    private byte[] image;

    public Face(String faceID, int top, int left, int width, int height) {
        this.faceID = faceID;
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getFaceID() {
        return faceID;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getProbability() {
        return round(probability,2);
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
