package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

/**
 * Created by gabri on 7/10/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Episode implements Parcelable {
    @Expose
    private int id;

    @Expose
    private String title;

    @Expose
    private long lastPosition;

    @Expose
    private  int chapterNumber;

    @Expose
    private  long length;

    @Expose
    private  String director;

    @Expose
    private  String qualitiesString;



    public Episode(Parcel episodeInformation)
    {
        this.id = episodeInformation.readInt();
        this.title = episodeInformation.readString();
        this.lastPosition = episodeInformation.readLong();
        this.chapterNumber = episodeInformation.readInt();
        this.length = episodeInformation.readLong();
        this.director = episodeInformation.readString();
        this.qualitiesString = episodeInformation.readString();
    }


    public Episode()
    {

    }



    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeLong(lastPosition);
        parcel.writeInt(chapterNumber);
        parcel.writeLong(length);
        parcel.writeString(director);
        parcel.writeString(qualitiesString);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title= title;
    }

    public long getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(long lastPosition) {
        this.lastPosition = lastPosition;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getQualitiesString() {
        return qualitiesString;
    }

    public void setQualitiesString(String qualitiesString) {
        this.qualitiesString = qualitiesString;
    }

    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>()
    {
        @Override
        public Episode createFromParcel(Parcel parcel) {
            return new Episode(parcel);
        }

        @Override
        public Episode[] newArray(int i) {
            return new Episode[i];
        }
    };

}
