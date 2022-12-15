package com.arca.equipfix.gambachanneltv.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabri on 6/21/2018.
 */

public class ItemDetails implements Parcelable {
    private int id;
    private  ItemType itemType;
    private long lastPosition;
    private String title;
    private  String subtitle;
    private long length;


    public ItemDetails(Parcel itemDetails)
    {
        this.id = itemDetails.readInt();
        this.title = itemDetails.readString();
        this.subtitle = itemDetails.readString();
        this.itemType = ItemType.values()[itemDetails.readInt()];
        this.lastPosition = itemDetails.readLong();
    }


    public ItemDetails(int id, ItemType itemType, long lastPosition, String title, String subtitle, long length) {
        this.id = id;
        this.itemType = itemType;
        this.lastPosition = lastPosition;
        this.title = title;
        this.subtitle = subtitle;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public long getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(long lastPosition) {
        this.lastPosition = lastPosition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeInt(itemType.getValue());
        parcel.writeLong(lastPosition);

    }

    public static final Parcelable.Creator<ItemDetails> CREATOR = new Parcelable.Creator<ItemDetails>()
    {
        @Override
        public ItemDetails createFromParcel(Parcel parcel) {
            return new ItemDetails(parcel);
        }

        @Override
        public ItemDetails[] newArray(int i) {
            return new ItemDetails[i];
        }
    };
}

