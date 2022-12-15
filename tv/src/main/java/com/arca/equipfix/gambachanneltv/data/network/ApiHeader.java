package com.arca.equipfix.gambachanneltv.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * Created by gabri on 6/13/2018.
 */

public class ApiHeader {


    private PublicApiHeader mPublicApiHeader;

    public PublicApiHeader getPublicApiHeader() {
        return mPublicApiHeader;
    }

    public static final class PublicApiHeader {

        @Expose
        @SerializedName("bearer")
        private String accessToken;

        @Inject
        public PublicApiHeader(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

}

