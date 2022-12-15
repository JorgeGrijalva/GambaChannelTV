package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

/**
 * Created by gabri on 6/18/2018.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDetails implements Parcelable {
    @Expose
    private int id;

    @Expose
    private String title;

    @Expose
    private String thumbnail;

    @Expose
    private String synopsis;

    @Expose
    private  int year;

    @Expose
    private String rating;

    @Expose
    private String categories;


    @Expose
    private String director;

    @Expose
    private String qualitiesString;

    @Expose
    private  long length;

    @Expose
    private  long lastPosition;

    @Expose
    private boolean isFavorite;

    @Expose
    private boolean restrictedContent;

    @Expose
    private float usersRating;


    public MovieDetails(Parcel movieInformation)
    {
        this.id = movieInformation.readInt();
        this.title = movieInformation.readString();
        this.thumbnail = movieInformation.readString();
        this.year = movieInformation.readInt();
        this.rating = movieInformation.readString();
        this.categories = movieInformation.readString();
        this.director = movieInformation.readString();
        this.qualitiesString = movieInformation.readString();
        this.length = movieInformation.readLong();
        this.lastPosition = movieInformation.readLong();
        this.isFavorite = movieInformation.readInt()==1;
        this.restrictedContent = movieInformation.readInt()==1;
        this.usersRating = movieInformation.readFloat();

    }


    public MovieDetails()
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
        parcel.writeInt(year);
        parcel.writeString(rating);
        parcel.writeString(categories);
        parcel.writeString(director);
        parcel.writeString(qualitiesString);
        parcel.writeLong(length);
        parcel.writeLong(lastPosition);
        parcel.writeInt(isFavorite ? 1 : 0);
        parcel.writeInt(restrictedContent ? 1 : 0);
        parcel.writeFloat(usersRating);
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

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
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

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(long lastPosition) {
        this.lastPosition = lastPosition;
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

    public void setQualitiesString(String qualitiesString) {
        this.qualitiesString = qualitiesString;
    }

    public float getUsersRating() {
        return usersRating;
    }

    public void setUsersRating(float usersRating) {
        this.usersRating = usersRating;
    }

    public static final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>()
    {
        @Override
        public MovieDetails createFromParcel(Parcel parcel) {
            return new MovieDetails(parcel);
        }

        @Override
        public MovieDetails[] newArray(int i) {
            return new MovieDetails[i];
        }
    };

}
