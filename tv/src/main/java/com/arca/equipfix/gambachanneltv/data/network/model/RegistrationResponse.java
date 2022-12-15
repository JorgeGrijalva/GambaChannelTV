package com.arca.equipfix.gambachanneltv.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.network.model.enums.RegistrationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationResponse implements Parcelable {

    @Expose
    private int id;

    @Expose
    private RegistrationStatus status;

    @Expose
    private String registrationCode;

    @Expose
    private boolean active;

    @Expose
    private String adultsCode;

    @Expose
    private String clientName;

    @Expose
    private Date expirationDate;

    @Expose
    private String expirationDateString;

    @Expose
    private String deviceName;

    @Expose
    private String message;

    @Expose
    private Profile[] profiles;

    public RegistrationResponse(Parcel registrationInformation)
    {
        this.id = registrationInformation.readInt();
        this.status = RegistrationStatus.values()[registrationInformation.readInt()];
        this.registrationCode = registrationInformation.readString();
        this.active = registrationInformation.readInt() == 1;
        this.adultsCode = registrationInformation.readString();
        this.clientName = registrationInformation.readString();
        this.expirationDate = (java.util.Date) registrationInformation.readSerializable(); ;
        this.message = registrationInformation.readString();
        this.deviceName = registrationInformation.readString();
        this.expirationDateString = registrationInformation.readString();

    }


    public  RegistrationResponse()
    {

    }



    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(status.getValue());
        parcel.writeString(registrationCode);
        parcel.writeInt(active ? 1 : 0);
        parcel.writeString(adultsCode);
        parcel.writeString(clientName);
        parcel.writeSerializable(expirationDate);
        parcel.writeString(message);
        parcel.writeString(deviceName);
        parcel.writeString(expirationDateString);

    }


    public int getId() { return id; }

    public  void setId(int id) { this.id = id; }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {

        this.status = status;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAdultsCode() {
        return adultsCode;
    }

    public void setAdultsCode(String adultsCode) {
        this.adultsCode = adultsCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDeviceName() { return deviceName; }

    public void setDeviceName(String deviceName ) { this.deviceName = deviceName; }

    public  String getExpirationDateString() { return expirationDateString; }

    public  void setExpirationDateString(String expirationDateString) { this.expirationDateString = expirationDateString; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Profile[] getProfiles() {
        return profiles;
    }

    public void setProfiles(Profile[] profiles) {
        this.profiles = profiles;
    }

    public static final Parcelable.Creator<RegistrationResponse> CREATOR = new Parcelable.Creator<RegistrationResponse>()
    {
        @Override
        public RegistrationResponse createFromParcel(Parcel parcel) {
            return new RegistrationResponse(parcel);
        }

        @Override
        public RegistrationResponse[] newArray(int i) {
            return new RegistrationResponse[i];
        }
    };
}
