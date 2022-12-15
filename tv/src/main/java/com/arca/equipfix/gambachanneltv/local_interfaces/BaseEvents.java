package com.arca.equipfix.gambachanneltv.local_interfaces;

import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;

/**
 * Created by gabri on 7/3/2018.
 */

public interface BaseEvents {

    void onRegistration();
    void onSessionStarted(SessionInformation sessionInformation);
    void onSessionError();

}
