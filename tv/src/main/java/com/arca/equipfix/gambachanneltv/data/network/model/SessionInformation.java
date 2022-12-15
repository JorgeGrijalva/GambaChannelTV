package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by gabri on 6/20/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionInformation implements Parcelable {

    @Expose
    private String accessToken;

    @Expose
    private String[] roles;

    public SessionInformation(String accessToken, String[] roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }


    public SessionInformation(Parcel sessionInformation)
    {
        this.accessToken = sessionInformation.readString();
        sessionInformation.readStringArray(this.roles );
    }


    public  SessionInformation()
    {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accessToken);
        parcel.writeStringArray(roles);
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public static final Parcelable.Creator<SessionInformation> CREATOR = new Parcelable.Creator<SessionInformation>()
    {
        @Override
        public SessionInformation createFromParcel(Parcel parcel) {
            return new SessionInformation(parcel);
        }

        @Override
        public SessionInformation[] newArray(int i) {
            return new SessionInformation[i];
        }
    };
}
