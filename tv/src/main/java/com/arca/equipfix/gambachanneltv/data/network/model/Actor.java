package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by gabri on 6/18/2018.
 */

public class Actor implements Parcelable {
    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String photo;


    public Actor(Parcel actor)
    {
        this.id = actor.readInt();
        this.name = actor.readString();
        this.photo = actor.readString();
    }


    public Actor()
    {

    }


    public Actor(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(photo);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static final Parcelable.Creator<Actor> CREATOR = new Parcelable.Creator<Actor>()
    {
        @Override
        public Actor createFromParcel(Parcel parcel) {
            return new Actor(parcel);
        }

        @Override
        public Actor[] newArray(int i) {
            return new Actor[i];
        }
    };

}
