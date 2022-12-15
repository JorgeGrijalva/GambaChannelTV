package com.arca.equipfix.gambachanneltv.data;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by gabri on 6/22/2018.
 */

public class Card  implements Parcelable {

    @SerializedName("title")
    private String mTitle = "";
    @SerializedName("description")
    private String mDescription = "";
    @SerializedName("extraText")
    private String mExtraText = "";
    @SerializedName("extraText2")
    private String mExtraText2 = "";
    @SerializedName("extraText3")
    private String mExtraText3 = "";
    @SerializedName("viewed")
    private boolean viewed;
    @SerializedName("card")
    private String mImageUrl;
    @SerializedName("footerColor")
    private String mFooterColor = null;
    @SerializedName("selectedColor")
    private String mSelectedColor = null;
    @SerializedName("localImageResource")
    private String mLocalImageResource = null;
    @SerializedName("footerIconLocalImageResource")
    private String mFooterResource = null;
    @SerializedName("type")
    private Card.Type mType;
    @SerializedName("id")
    private int mId;
    @SerializedName("width")
    private int mWidth;
    @SerializedName("height")
    private int mHeight;
    @SerializedName("url")
    private String mUrl;
    private String mCategoryType;

    private int primaryImageResource;
    private int secondaryImageResource;
    private boolean selected;
    private  int subType;


    public Card(int id, String title, int primaryImageResource, int secondaryImageResource, Card.Type type)
    {
        this.mId = id;
        this.mTitle = title;
        this.primaryImageResource = primaryImageResource;
        this.secondaryImageResource = secondaryImageResource;
        this.mType = type;
    }

    public Card(int id, String title, Card.Type type)
    {
        this.mId = id;
        this.mTitle = title;
        this.mType = type;
    }

    public Card() {
    }

    public Card(Parcel card) {
        this.mTitle = card.readString();
        this.mDescription = card.readString();
        this.mExtraText = card.readString();
        this.mExtraText2 = card.readString();
        this.mExtraText3 = card.readString();
        this.mUrl = card.readString();
        this.mImageUrl = card.readString();
        this.mFooterColor = card.readString();
        this.mSelectedColor = card.readString();
        this.mLocalImageResource = card.readString();
        this.mFooterResource = card.readString();
        this.mId = card.readInt();
        this.mWidth = card.readInt();
        this.mHeight = card.readInt();
        this.mCategoryType = card.readString();
        this.mType = Type.values()[card.readInt()];
        this.primaryImageResource = card.readInt();
        this.secondaryImageResource = card.readInt();
        this.subType = card.readInt();
    }


    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLocalImageResource() {
        return mLocalImageResource;
    }

    public void setLocalImageResource(String localImageResource) {
        mLocalImageResource = localImageResource;
    }

    public String getFooterResource() {
        return mFooterResource;
    }

    public void setFooterResource(String footerResource) {
        mFooterResource = footerResource;
    }

    public void setType(Type type) {
        mType = type;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setCategoryType(String categoryType) {
        this.mCategoryType = categoryType;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getId() {
        return mId;
    }

    public Card.Type getType() {
        return mType;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCategoryType() {
        return mCategoryType;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public String getExtraText() {
        return mExtraText;
    }

    public void setExtraText(String extraText) {
        mExtraText = extraText;
    }

    public String getExtraText2() {
        return mExtraText2;
    }

    public void setExtraText2(String extraText2) {
        this.mExtraText2 = extraText2;
    }

    public String getExtraText3() {
        return mExtraText3;
    }

    public void setExtraText3(String extraText3) {
        this.mExtraText3 = extraText3;
    }

    public int getFooterColor() {
        if (mFooterColor == null) return -1;
        return Color.parseColor(mFooterColor);
    }

    public void setFooterColor(String footerColor) {
        mFooterColor = footerColor;
    }

    public int getSelectedColor() {
        if (mSelectedColor == null) return -1;
        return Color.parseColor(mSelectedColor);
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setSelectedColor(String selectedColor) {
        mSelectedColor = selectedColor;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public URI getImageURI() {
        if (getImageUrl() == null) return null;
        try {
            return new URI(getImageUrl());
        } catch (URISyntaxException e) {
            Log.d("URI exception: ", getImageUrl());
            return null;
        }
    }

    public int getLocalImageResourceId(Context context) {
        return context.getResources().getIdentifier(getLocalImageResourceName(), "drawable",
                context.getPackageName());
    }

    public String getLocalImageResourceName() {
        return mLocalImageResource;
    }

    public String getFooterLocalImageResourceName() {
        return mFooterResource;
    }

    public int getPrimaryImageResource() {
        return primaryImageResource;
    }

    public void setPrimaryImageResource(int primaryImageResource) {
        this.primaryImageResource = primaryImageResource;
    }

    public int getSecondaryImageResource() {
        return secondaryImageResource;
    }

    public void setSecondaryImageResource(int secondaryImageResource) {
        this.secondaryImageResource = secondaryImageResource;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mExtraText);
        parcel.writeString(mExtraText2);
        parcel.writeString(mExtraText3);
        parcel.writeString(mUrl);
        parcel.writeString(mImageUrl);
        parcel.writeString(mFooterColor);
        parcel.writeString(mSelectedColor);
        parcel.writeString(mLocalImageResource);
        parcel.writeString(mFooterResource);
        parcel.writeInt(mId);
        parcel.writeInt(mWidth);
        parcel.writeInt(mHeight);
        parcel.writeString(mCategoryType);
        parcel.writeInt(mType.getValue());
        parcel.writeInt(primaryImageResource);
        parcel.writeInt(secondaryImageResource);
        parcel.writeInt(subType);
    }

    public enum Type {
        MOVIE(0),
        SERIE(1),
        EPISODE(2),
        SINGLE_LINE(3),
        DEFAULT(4),
        MOVIE_BASE(5),
        MAIN_MENU(6),
        SUBMENU(7)
        ;

        private final int value;

        public int getValue() {
            return value;
        }

        private Type(int value) {
            this.value = value;
        }
    }

    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>()
    {
        @Override
        public Card createFromParcel(Parcel parcel) {
            return new Card(parcel);
        }

        @Override
        public Card[] newArray(int i) {
            return new Card[i];
        }
    };





}
