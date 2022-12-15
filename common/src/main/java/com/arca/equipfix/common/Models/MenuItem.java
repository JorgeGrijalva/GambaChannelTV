package com.arca.equipfix.common.Models;

/**
 * Created by gabri on 6/7/2018.
 */

public class MenuItem {
    private int id;
    private String title;
    private int imageOn;
    private int imageOff;

    public MenuItem()
    {

    }

    public MenuItem(int id, String title, int imageOn, int imageOff)
    {
        this.id = id;
        this.imageOn = imageOn;
        this.title = title;
        this.imageOff = imageOff;

    }

    public int getId()
    {
        return id;
    }

    public  String getTitle()
    {
        return title;
    }

    public int getImageOn()
    {
        return imageOn;
    }

    public int getImageOff()
    {
        return imageOff;
    }
}


