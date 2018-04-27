package com.example.sanjinpc.edostavahrane.Model;

/**
 * Created by SanjinPc on 4/10/2018.
 */

public class Category {
    private String Image;
    private String Name;

    public Category(String image, String name) {
        Image = image;
        Name = name;
    }

    public Category()
    {

    }

    public String getImage() {
        return Image;
    }

    public String getName() {
        return Name;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setName(String name) {
        Name = name;
    }
}
