package com.example.fadi.Model;

public class Tests {
    private String Name;
    private String Image;


    public Tests() {
    }

    public Tests(String name, String image) {
        this.Name = name;
        this.Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}
