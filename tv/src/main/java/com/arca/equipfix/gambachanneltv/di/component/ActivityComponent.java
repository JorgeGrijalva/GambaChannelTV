package com.arca.equipfix.gambachanneltv.di.component;

import com.arca.equipfix.gambachanneltv.di.PerActivity;
import com.arca.equipfix.gambachanneltv.di.module.ActivityModule;
import com.arca.equipfix.gambachanneltv.local_interfaces.BaseEvents;
import com.arca.equipfix.gambachanneltv.ui.activities.BaseActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.EPGActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.EpisodesPlayerActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.LivePlayerActivity;
import com.arca.equipfix.gambachanneltv.ui.fragments.ContentCategoryActivityFragment;

import com.arca.equipfix.gambachanneltv.ui.activities.MainMenuActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.MovieDetailsActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.PlayerActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.SplashActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.StartActivity;
import com.arca.equipfix.gambachanneltv.ui.fragments.SettingsActivityFragment;

import dagger.Component;

/**
 * Created by gabri on 6/14/2018.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface  ActivityComponent {

    void inject(BaseActivity activity);
    void inject(PlayerActivity activity);
    void inject(LivePlayerActivity activity);
    void inject(EpisodesPlayerActivity activity);
    void inject(EPGActivity activity);


    void inject(ContentCategoryActivityFragment fragment);
    void inject(SettingsActivityFragment fragment);
}
