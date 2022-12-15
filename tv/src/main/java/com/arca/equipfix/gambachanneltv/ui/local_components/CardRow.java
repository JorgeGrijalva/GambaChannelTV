package com.arca.equipfix.gambachanneltv.ui.local_components;

import com.arca.equipfix.gambachanneltv.data.Card;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gabri on 6/22/2018.
 */

public class CardRow {
    // default is a list of cards
    public static final int TYPE_DEFAULT = 0;
    // section header
    public static final int TYPE_SECTION_HEADER = 1;
    // divider
    public static final int TYPE_DIVIDER = 2;

    @SerializedName("type")
    private int mType = TYPE_DEFAULT;
    // Used to determine whether the row shall use shadows when displaying its cards or not.
    @SerializedName("shadow")
    private boolean mShadow = true;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("cards")
    private List<Card> mCards;

    public int getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean useShadow() {
        return mShadow;
    }

    public List<Card> getCards() {
        return mCards;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void useShadow(Boolean useShadow) {
        mShadow = useShadow;
    }

    public void setCards(List<Card> cards) {
        mCards = cards;
    }
}
