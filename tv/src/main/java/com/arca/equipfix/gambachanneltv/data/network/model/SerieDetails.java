package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

/**
 * Created by gabri on 7/10/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SerieDetails implements Parcelable {
    @Expose
    private int id;

    @Expose
    private String title;

    @Expose
    private String thumbnail;

    @Expose
    private String synopsis;

    @Expose
    private String rating;

    @Expose
    private String categories;

    @Expose
    private boolean isFavorite;

    @Expose
    private boolean restrictedContent;

    @Expose
    private int episodeCount;

    @Expose
    private int seasonCount;

    @Expose
    private int lastEpisode;




    public SerieDetails(Parcel serieInformation)
    {
        this.id = serieInformation.readInt();
        this.title = serieInformation.readString();
        this.thumbnail = serieInformation.readString();
        this.rating = serieInformation.readString();
        this.categories = serieInformation.readString();
        this.isFavorite = serieInformation.readInt()==1;
        this.restrictedContent = serieInformation.readInt()==1;
        this.episodeCount = serieInformation.readInt();
        this.seasonCount = serieInformation.readInt();
        this.lastEpisode = serieInformation.readInt();

    }


    public SerieDetails()
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
        parcel.writeString(thumbnail);
        parcel.writeString(rating);
        parcel.writeString(categories);
        parcel.writeInt(isFavorite ? 1 : 0);
        parcel.writeInt(restrictedContent ? 1 : 0);
        parcel.writeInt(episodeCount);
        parcel.writeInt(seasonCount);
        parcel.writeInt(lastEpisode);
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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean getRestrictedContent() {
        return restrictedContent;
    }

    public void setRestrictedContent(boolean restrictedContent) {
        this.restrictedContent = restrictedContent;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public int getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(int lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    public static final Parcelable.Creator<SerieDetails> CREATOR = new Parcelable.Creator<SerieDetails>()
    {
        @Override
        public SerieDetails createFromParcel(Parcel parcel) {
            return new SerieDetails(parcel);
        }

        @Override
        public SerieDetails[] newArray(int i) {
            return new SerieDetails[i];
        }
    };

}
