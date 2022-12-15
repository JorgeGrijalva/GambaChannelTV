package com.arca.equipfix.gambachanneltv.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gabri on 7/9/2018.
 */
@Entity(nameInDb = "reminders")
public class Reminder implements Parcelable{

    @Expose
    @Id(autoincrement = true)
    private   Long id;

    @Expose
    @Property(nameInDb = "title")
    private String title;

    @Expose
    @Property(nameInDb = "channel_id")
    private int channelId;

    @Expose
    @Property(nameInDb = "channel_name")
    private String channelName;

    @Expose
    @Property(nameInDb = "start_date")
    private Date startDate ;

    @Expose
    @Property(nameInDb = "unique")
    private boolean unique;

    @Expose
    @Property(nameInDb = "played")
    private boolean played;

    public Reminder()
    {

    }

    public Reminder(Parcel parcel)
    {
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.channelId = parcel.readInt();
        this.channelName = parcel.readString();
        this.played = parcel.readInt()== 1;
        this.unique = parcel.readInt() == 1;
    }

    @Generated(hash = 168634445)
    public Reminder(Long id, String title, int channelId, String channelName, Date startDate,
            boolean unique, boolean played) {
        this.id = id;
        this.title = title;
        this.channelId = channelId;
        this.channelName = channelName;
        this.startDate = startDate;
        this.unique = unique;
        this.played = played;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeInt(channelId);
        parcel.writeString( channelName);
        parcel.writeInt( played ? 1 : 0 );
        parcel.writeInt(unique ? 1 : 0);
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

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean getUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean getPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public static Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>()
    {
        @Override
        public Reminder createFromParcel(Parcel parcel) {
            return new Reminder(parcel);
        }

        @Override
        public Reminder[] newArray(int i) {
            return new Reminder[i];
        }
    };

}
