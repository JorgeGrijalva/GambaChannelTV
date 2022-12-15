package com.arca.equipfix.common.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabri on 6/13/2018.
 */

public class SubmenuItem implements Parcelable {

    private int id;
    private String title;

    public SubmenuItem()
    {

    }

    public SubmenuItem(int id, String title)
    {
        this.id = id;
        this.title = title;
    }

    public SubmenuItem(Parcel parcel)
    {
        this.id = parcel.readInt();
        this.title = parcel.readString();
    }

    public int getId()
    {
        return id;
    }

    public  String getTitle()
    {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);

    }

    public static final Parcelable.Creator<SubmenuItem> CREATOR = new Parcelable.Creator<SubmenuItem>()
    {
        @Override
        public SubmenuItem createFromParcel(Parcel parcel) {
            return new SubmenuItem(parcel);
        }

        @Override
        public SubmenuItem[] newArray(int i) {
            return new SubmenuItem[i];
        }
    };


}


