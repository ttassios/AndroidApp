package com.themis.travelcompanion;

import java.io.Serializable;

public class UserModel implements Serializable {
    int sight_id;
    String name;
    String comments;
    String country;
    String address;
    String area;
    String image;

    public UserModel() {
        this.sight_id = this.sight_id;
        this.name = this.name;
        this.comments = this.comments;
        this.country = this.country;
        this.address = this.address;
        this.area = this.area;
        this.image = this.image;
    }

    public int getSight_id() {
        return sight_id;
    }

    public void setSight_id(int sight_id) {
        this.sight_id = sight_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}