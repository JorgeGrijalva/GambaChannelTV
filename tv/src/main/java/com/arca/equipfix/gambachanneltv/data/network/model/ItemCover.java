package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemCover implements Parcelable{
    @Expose
    private int id;

    @Expose
    private String title;

    @Expose
    private String thumbnail;

    @Expose
    private  int year;

    @Expose
    private String rating;


    public ItemCover(Parcel itemInformation)
    {
        this.id = itemInformation.readInt();
        this.title = itemInformation.readString();
        this.thumbnail = itemInformation.readString();
        this.year = itemInformation.readInt();
        this.rating = itemInformation.readString();
    }


    public ItemCover()
    {

    }

    public ItemCover(int id, String title, String thumbnail, int year, String rating) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.year = year;
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(thumbnail);
        parcel.writeInt(year);
        parcel.writeString(rating);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public static final Parcelable.Creator<ItemCover> CREATOR = new Parcelable.Creator<ItemCover>()
    {
        @Override
        public ItemCover createFromParcel(Parcel parcel) {
            return new ItemCover(parcel);
        }

        @Override
        public ItemCover[] newArray(int i) {
            return new ItemCover[i];
        }
    };

}
