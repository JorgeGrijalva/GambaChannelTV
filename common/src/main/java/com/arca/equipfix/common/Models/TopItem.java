package com.arca.equipfix.common.Models;

/**
 * Created by gabri on 6/7/2018.
 */

public class TopItem {
    private int id;
    private String title;
    private int image;
    private String description;

    public TopItem()
    {

    }

    public TopItem(int id, String title, int image)
    {
        this.id = id;
        this.title = title;
        this.image = image;

    }

    public int getId()
    {
        return id;
    }

    public  String getTitle()
    {
        return title;
    }

    public int getImage()
    {
        return image;
    }

    public String getDescription()
    {
        return description;

    }

}


