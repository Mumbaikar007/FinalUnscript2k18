package com.example.optimus.tezattendance;

/**
 * Created by optimus on 25/3/18.
 */

public class News {

    String newsImage, description, name, type ;

    public News(){

    }

    public News(String newsImage, String description, String name, String type) {
        this.newsImage = newsImage;
        this.description = description;
        this.name = name;
        this.type = type;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
