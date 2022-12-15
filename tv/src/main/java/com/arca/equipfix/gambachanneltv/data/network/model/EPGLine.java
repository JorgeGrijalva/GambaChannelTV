package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.models.PendingCall;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EPGLine implements Parcelable {

    @Expose
    private   int id;

    @Expose
    private String thumbnail;

    @Expose
    private String name;

    @Expose
    private String url;

    private long lastUpdate;

    @Expose
    private List<ChannelProgram> programs;

    public EPGLine()
    {

    }

    public EPGLine(Parcel parcel)
    {
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.thumbnail = parcel.readString();
        this.url = parcel.readString();
        this.lastUpdate = parcel.readLong();
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
        parcel.writeString(url);
        parcel.writeLong(lastUpdate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChannelProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(List<ChannelProgram> programs) {
        this.programs = programs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public static Parcelable.Creator<EPGLine> CREATOR = new Parcelable.Creator<EPGLine>()
    {
        @Override
        public EPGLine createFromParcel(Parcel parcel) {
            return new EPGLine(parcel);
        }

        @Override
        public EPGLine[] newArray(int i) {
            return new EPGLine[i];
        }
    };

}
