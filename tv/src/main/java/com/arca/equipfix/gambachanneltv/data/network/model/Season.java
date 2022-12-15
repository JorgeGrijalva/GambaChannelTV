package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by gabri on 7/10/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Season implements Parcelable {
    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String thumbnail;

    @Expose
    private int episodeCount;

    @Expose
    private int lastEpisode;

    private List<Episode> episodes;




    public Season(Parcel serieInformation)
    {
        this.id = serieInformation.readInt();
        this.name = serieInformation.readString();
        this.thumbnail = serieInformation.readString();
        this.episodeCount = serieInformation.readInt();
        this.lastEpisode = serieInformation.readInt();
    }


    public Season()
    {

    }

    public Season(int id, String name, int lastEpisode)
    {
        this.id = id;
        this.name = name;
        this.lastEpisode = lastEpisode;
    }



    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(thumbnail);
        parcel.writeInt(episodeCount);
        parcel.writeInt(lastEpisode);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name= name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }


    public int getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(int lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public static final Parcelable.Creator<Season> CREATOR = new Parcelable.Creator<Season>()
    {
        @Override
        public Season createFromParcel(Parcel parcel) {
            return new Season(parcel);
        }

        @Override
        public Season[] newArray(int i) {
            return new Season[i];
        }
    };

}
