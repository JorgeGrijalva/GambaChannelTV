package com.arca.equipfix.gambachanneltv.di.component;

import android.app.Application;
import android.content.Context;

import com.arca.equipfix.gambachanneltv.GambaChannelApp;
import com.arca.equipfix.gambachanneltv.data.DataManager;
import com.arca.equipfix.gambachanneltv.di.ApplicationContext;
import com.arca.equipfix.gambachanneltv.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

/**
 * Created by gabri on 6/14/2018.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(GambaChannelApp app);

    @ApplicationContext
    Context context();

    Application application();
    DataManager getDataManager();



}
