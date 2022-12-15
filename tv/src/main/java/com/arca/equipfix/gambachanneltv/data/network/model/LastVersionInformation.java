package com.arca.equipfix.gambachanneltv.data.network.model;

import com.google.gson.annotations.Expose;

/**
 * Created by gabri on 9/6/2018.
 */

public class LastVersionInformation {
    @Expose
    private  String url;

    @Expose
    private  String lastVersion;


    public LastVersionInformation() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }
}
