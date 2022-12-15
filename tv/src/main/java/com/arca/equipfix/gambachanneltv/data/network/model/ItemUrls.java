package com.arca.equipfix.gambachanneltv.data.network.model;

import com.google.gson.annotations.Expose;

/**
 * Created by gabri on 7/2/2018.
 */

public class ItemUrls {
    @Expose
    private  String url;

    @Expose
    private  String thumbnails;

    @Expose
    private  String trailer;

    public ItemUrls() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}
