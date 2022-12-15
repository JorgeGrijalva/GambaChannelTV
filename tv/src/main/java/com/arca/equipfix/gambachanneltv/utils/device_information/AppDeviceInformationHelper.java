package com.arca.equipfix.gambachanneltv.utils.device_information;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.arca.equipfix.gambachanneltv.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by gabri on 6/14/2018.
 */

@Singleton
public class AppDeviceInformationHelper implements DeviceInformationHelper {

    private Context context;

    @Inject
    public AppDeviceInformationHelper(@ApplicationContext Context context)
    {
        this.context = context;
    }

    public String getDeviceSerialNumber()
    {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        //return  "951427e2ac0ccec8";
    }

    public String getDeviceType()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
