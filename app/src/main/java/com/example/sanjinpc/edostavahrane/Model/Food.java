package com.example.sanjinpc.edostavahrane.Model;

/**
 * Created by SanjinPc on 4/13/2018.
 */

public class Food
{
    private String Description , Name , MenuId , Image , Price , Discount;

    public Food(String description, String name, String menuId, String image, String price, String discount) {
        Description = description;
        Name = name;
        MenuId = menuId;
        Image = image;
        Price = price;
        Discount = discount;
    }

    public Food() {
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
