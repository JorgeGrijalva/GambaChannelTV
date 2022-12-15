package com.arca.equipfix.gambachanneltv.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Arrays;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "profiles")
public class Profile implements Parcelable {

    @Expose
    @Id()
    private  long id;

    @Expose
    @Property(nameInDb = "profileToken")
    private  String profileToken;

    @Expose
    @Property(nameInDb = "name")
    private  String name;

    @Expose
    @Property(nameInDb = "passwordProtected")
    private boolean passwordProtected;

    @Expose
    @Property(nameInDb = "profilePassword")
    private String profilePassword;

    @Expose
    @Property(nameInDb = "enableAdults")
    private  boolean enableAdults;

    private boolean selected;

    public Profile(Parcel profile)
    {
        this.id = profile.readLong();
        this.profileToken = profile.readString();
        this.name = profile.readString();
        this.passwordProtected = profile.readInt() == 1;
        this.enableAdults = profile.readInt() == 1;
    }



    public Profile() {
    }

    public Profile(String name, boolean passwordProtected, boolean enableAdults, String profilePassword) {
        this.name = name;
        this.passwordProtected = passwordProtected;
        this.enableAdults = enableAdults;
        this.profilePassword = profilePassword;
    }

    public Profile(long id, String profileToken, String name, boolean passwordProtected, boolean enableAdults) {
        this.id = id;
        this.profileToken = profileToken;
        this.name = name;
        this.passwordProtected = passwordProtected;
        this.enableAdults = enableAdults;
    }



    @Generated(hash = 1090911627)
    public Profile(long id, String profileToken, String name, boolean passwordProtected, String profilePassword,
            boolean enableAdults, boolean selected) {
        this.id = id;
        this.profileToken = profileToken;
        this.name = name;
        this.passwordProtected = passwordProtected;
        this.profilePassword = profilePassword;
        this.enableAdults = enableAdults;
        this.selected = selected;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(profileToken);
        parcel.writeString(name);
        parcel.writeInt(passwordProtected ? 1 : 0);
        parcel.writeInt(enableAdults ? 1 : 0);
    }

    public static Profile[] getProfileArray(Parcelable[] parcelables) {
        if (parcelables == null)
            return null;
        return Arrays.copyOf(parcelables, parcelables.length, Profile[].class);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfileToken() {
        return profileToken;
    }

    public void setProfileToken(String profileToken) {
        this.profileToken = profileToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean getPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public boolean getEnableAdults() {
        return enableAdults;
    }

    public void setEnableAdults(boolean enableAdults) {
        this.enableAdults = enableAdults;
    }

    public String getProfilePassword() {
        return profilePassword;
    }

    public void setProfilePassword(String profilePassword) {
        this.profilePassword = profilePassword;
    }



    public boolean getSelected() {
        return this.selected;
    }



    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>()
    {
        @Override
        public Profile createFromParcel(Parcel parcel) {
            return new Profile(parcel);
        }

        @Override
        public Profile[] newArray(int i) {
            return new Profile[i];
        }
    };
}
