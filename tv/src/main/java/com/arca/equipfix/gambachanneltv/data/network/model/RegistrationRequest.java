package com.arca.equipfix.gambachanneltv.data.network.model;

import com.arca.equipfix.gambachanneltv.utils.device_information.AppDeviceInformationHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

/**
 * Created by gabri on 6/14/2018.
 */

public class RegistrationRequest {
    @Expose
    @SerializedName("serialNumber")
    private String serialNumber;

    @Inject
    public RegistrationRequest(AppDeviceInformationHelper deviceInformationHelper)
    {
        this.serialNumber = deviceInformationHelper.getDeviceSerialNumber();
    }

}
