package com.arca.equipfix.gambachanneltv.data;

/**
 * Created by gabri on 6/21/2018.
 */

public enum ItemType
{
    MOVIE(0), EPISODE(1), ADULT_MOVIE(2), LIVE_CHANNEL(3), TRAILER(4),  DEMO(5), SERIE(6);

    private final int value;

    public int getValue() {
        return value;
    }

    private ItemType(int value) {
        this.value = value;
    }
}

