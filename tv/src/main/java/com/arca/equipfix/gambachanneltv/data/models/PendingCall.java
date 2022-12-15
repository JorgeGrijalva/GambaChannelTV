package com.arca.equipfix.gambachanneltv.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by gabri on 6/30/2018.
 */

public class PendingCall {
    private int type;
    private String stringParameters;
    private int integerParameters;
    private  String categoryName;

    public PendingCall()
    {

    }

    public PendingCall(int type, String stringParameters, int integerParameters, String categoryName)
    {
        this.type = type;
        this.stringParameters = stringParameters;
        this.integerParameters = integerParameters;
        this.categoryName = categoryName;

    }

    public int getType() { return type; }
    public String getStringParameters() { return stringParameters; }
    public int getIntegerParameters() { return integerParameters; }
    public  String getCategoryName() { return  categoryName;}


}

