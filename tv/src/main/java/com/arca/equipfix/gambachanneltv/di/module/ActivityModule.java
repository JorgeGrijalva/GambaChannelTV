package com.arca.equipfix.gambachanneltv.di.module;

import android.app.Activity;
import android.content.Context;

import com.arca.equipfix.gambachanneltv.di.ActivityContext;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gabri on 6/14/2018.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }


    @Provides
    DefaultBandwidthMeter providesBandwidthMeter() {return new DefaultBandwidthMeter(); }

    @Provides
    TrackSelection.Factory providesTrackSelectionFactory() { return new FixedTrackSelection.Factory();}

}

