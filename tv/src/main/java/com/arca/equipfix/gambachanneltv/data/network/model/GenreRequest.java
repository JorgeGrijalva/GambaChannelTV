package com.arca.equipfix.gambachanneltv.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gabri on 6/13/2018.
 */

public class GenreRequest {
    @Expose
    @SerializedName("language")
    private String language;

    public GenreRequest(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
