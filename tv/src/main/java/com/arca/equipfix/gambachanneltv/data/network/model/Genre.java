package com.arca.equipfix.gambachanneltv.data.network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gabri on 6/13/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Genre {

    @Expose
    private int id;

    @Expose
    private String name;

    public  Genre()
    {

    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
