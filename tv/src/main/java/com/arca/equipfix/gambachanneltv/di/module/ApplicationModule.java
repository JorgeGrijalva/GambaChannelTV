package com.arca.equipfix.gambachanneltv.di.module;

import android.app.Application;
import android.content.Context;

import com.arca.equipfix.gambachanneltv.BuildConfig;
import com.arca.equipfix.gambachanneltv.data.AppDataManager;
import com.arca.equipfix.gambachanneltv.data.DataManager;
import com.arca.equipfix.gambachanneltv.data.db.AppDbHelper;
import com.arca.equipfix.gambachanneltv.data.db.DbHelper;
import com.arca.equipfix.gambachanneltv.data.network.ApiHelper;
import com.arca.equipfix.gambachanneltv.data.network.AppApiHelper;
import com.arca.equipfix.gambachanneltv.data.prefs.AppPreferencesHelper;
import com.arca.equipfix.gambachanneltv.data.prefs.PreferencesHelper;
import com.arca.equipfix.gambachanneltv.di.ApplicationContext;
import com.arca.equipfix.gambachanneltv.di.DatabaseInfo;
import com.arca.equipfix.gambachanneltv.di.PreferencesName;
import com.arca.equipfix.gambachanneltv.utils.AppConstants;
import com.arca.equipfix.gambachanneltv.utils.device_information.AppDeviceInformationHelper;
import com.arca.equipfix.gambachanneltv.utils.device_information.DeviceInformationHelper;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gabri on 6/14/2018.
 */

@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    DeviceInformationHelper provideDeviceInformationHelper(AppDeviceInformationHelper appDeviceInformationHelper) { return appDeviceInformationHelper; }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) { return  appDataManager; }

    @Provides
    @Singleton
    DbHelper provideDbHelper (AppDbHelper appDbHelper) { return  appDbHelper; }

    @Provides
    @Singleton
    PreferencesHelper providesPreferencesHelper(AppPreferencesHelper appPreferencesHelper) { return  appPreferencesHelper; }

    @Provides
    @Singleton
    ApiHelper providesApiHelper(AppApiHelper appApiHelper) { return appApiHelper; }

    @Provides
    @PreferencesName
    String providePreferencesName()
    {
        return mApplication.getPackageName() + "_preferences";

    }


}
