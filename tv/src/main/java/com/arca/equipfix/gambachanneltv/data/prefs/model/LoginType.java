package com.arca.equipfix.gambachanneltv.data.prefs.model;

/**
 * Created by gabri on 6/21/2018.
 */

public enum LoginType {
    DEMO(0), CLIENT(1);

    private final int value;

    public int getValue() {
        return value;
    }

    private LoginType(int value) { this.value = value;
    }
}
