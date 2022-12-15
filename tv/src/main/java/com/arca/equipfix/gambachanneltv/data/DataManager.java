package com.arca.equipfix.gambachanneltv.data;

import com.arca.equipfix.gambachanneltv.data.db.DbHelper;
import com.arca.equipfix.gambachanneltv.data.network.ApiHelper;
import com.arca.equipfix.gambachanneltv.data.prefs.PreferencesHelper;

/**
 * Created by gabri on 6/14/2018.
 */

public interface DataManager extends ApiHelper, DbHelper, PreferencesHelper {
    String getDeviceSerialNumber();




}
