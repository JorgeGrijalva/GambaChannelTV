package com.arca.equipfix.gambachanneltv.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gabri on 7/5/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(nameInDb = "programs")
public class ChannelProgram implements Parcelable {

    @Expose
    @Id()
    private  Long id;

    @Expose
    @Property(nameInDb = "title")
    private  String title;

    @Expose
    @Property(nameInDb = "start_date")
    private  Date startDate;

    @Expose
    @Property(nameInDb = "end_date")
    private   Date endDate;

    @Expose
    @Property(nameInDb = "description")
    private   String description;

    @Expose
    @Property(nameInDb = "actors")
    private   String actors;

    @Expose
    @Property(nameInDb = "categories")
    private  String categories;

    @Expose
    @Property(nameInDb = "rating")
    private   String rating;

    @Expose
    @Property(nameInDb = "startRating")
    private   String startRating;

    @Expose
    @Property(nameInDb = "subTitle")
    private   String subTitle;

    @Expose
    @Property(nameInDb = "year")
    private   int year;

    @Expose
    @Property(nameInDb = "episodeNumber")
    private   String episodeNumber;


    @Property(nameInDb = "channel_id")
    private   int channelId;

    @Transient
    private boolean isFirstProgram;

    @Property(nameInDb = "reminder")
    private boolean reminder;

    @Property(nameInDb = "channelName")
    private String channelName;



    public ChannelProgram() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeSerializable(startDate);
        parcel.writeSerializable(endDate);
        parcel.writeString(description);
        parcel.writeString(actors);
        parcel.writeString(categories);
        parcel.writeString(rating);
        parcel.writeString(startRating);
        parcel.writeString(subTitle);
        parcel.writeInt(year);
        parcel.writeString(episodeNumber);
    }

    public ChannelProgram(Parcel parcel)
    {
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.startDate = (java.util.Date) parcel.readSerializable(); ;
        this.endDate = (java.util.Date) parcel.readSerializable(); ;
        this.description = parcel.readString();
        this.actors = parcel.readString();
        this.categories = parcel.readString();
        this.rating = parcel.readString();
        this.startRating = parcel.readString();
        this.subTitle = parcel.readString();
        this.year = parcel.readInt();
        this.episodeNumber = parcel.readString();
        this.channelId = parcel.readInt();
    }


    public ChannelProgram(String title, Date startDate, Date endDate) {

        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Generated(hash = 1099327997)
    public ChannelProgram(Long id, String title, Date startDate, Date endDate, String description,
            String actors, String categories, String rating, String startRating, String subTitle,
            int year, String episodeNumber, int channelId, boolean reminder, String channelName) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.actors = actors;
        this.categories = categories;
        this.rating = rating;
        this.startRating = startRating;
        this.subTitle = subTitle;
        this.year = year;
        this.episodeNumber = episodeNumber;
        this.channelId = channelId;
        this.reminder = reminder;
        this.channelName = channelName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStartRating() {
        return startRating;
    }

    public void setStartRating(String startRating) {
        this.startRating = startRating;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public boolean isFirstProgram() {
        return isFirstProgram;
    }

    public void setFirstProgram(boolean firstProgram) {
        isFirstProgram = firstProgram;
    }

    public boolean getReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public int getChannelId() {
        return this.channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public static Parcelable.Creator<ChannelProgram> CREATOR = new Parcelable.Creator<ChannelProgram>()
    {
        @Override
        public ChannelProgram createFromParcel(Parcel parcel) {
            return new ChannelProgram(parcel);
        }

        @Override
        public ChannelProgram[] newArray(int i) {
            return new ChannelProgram[i];
        }
    };
}
