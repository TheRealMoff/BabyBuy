package com.example.babybuy;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class BabyItems {
    int id;
    Bitmap itemImage;
    String description;
    Float price;

    public BabyItems(BabyItems babyItems) {
    }

    public BabyItems() {
    }

    public BabyItems(int id) {
        this.id = id;
    }

    public BabyItems(Bitmap itemImage, String description, Float price) {
        this.itemImage = itemImage;
        this.description = description;
        this.price = price;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getItemImage() {
        return itemImage;
    }

    public void setItemImage(Bitmap itemImage) {
        this.itemImage = itemImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "BabyItems{" +
                "id=" + id +
                ", itemImage=" + itemImage +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

}
