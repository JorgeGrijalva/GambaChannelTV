package com.arca.equipfix.gambachanneltv.data.network.model.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gabri on 6/14/2018.
 */

public enum RegistrationStatus {


    @SerializedName("0")
    NEW(0),

    @SerializedName("1")
    ACTIVE (1),

    @SerializedName("2")
    EXPIRED (2),

    @SerializedName("3")
    BLOCKED (3),

    @SerializedName("100")
    ERROR(100);

    private final int value;
    public int getValue() {
        return value;
    }

    private RegistrationStatus(int value) {
        this.value = value;
    }

}


