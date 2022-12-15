package com.arca.equipfix.gambachanneltv;

import android.app.Application;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.ConnectionQualityChangeListener;
import com.arca.equipfix.gambachanneltv.di.component.ApplicationComponent;
import com.arca.equipfix.gambachanneltv.di.component.DaggerApplicationComponent;
import com.arca.equipfix.gambachanneltv.di.module.ApplicationModule;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.jacksonandroidnetworking.JacksonParserFactory;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import static com.androidnetworking.AndroidNetworking.*;

/**
 * Created by gabri on 6/14/2018.
 */

public class GambaChannelApp extends Application {

    private static final String TAG = "GambaChannelApp";

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        applicationComponent.inject(this);
        initialize(getApplicationContext());

        setParserFactory(new JacksonParserFactory());

        if(BuildConfig.DEBUG)
        {
            enableLogging(HttpLoggingInterceptor.Level.BODY);
        }

        setConnectionQualityChangeListener((currentConnectionQuality, currentBandwidth) ->
                Log.d(TAG, "onChange: currentConnectionQuality : " + currentConnectionQuality + " currentBandwidth : " + currentBandwidth));

        userAgent = Util.getUserAgent(this, "GambaChannelTV");

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        MultiDex.install(this);

    }

    protected String userAgent;


    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return com.google.android.exoplayer2.BuildConfig.FLAVOR.equals("withExtensions");
    }

    public ApplicationComponent getApplicationComponent() { return applicationComponent ;}

    public void setApplicationComponent(ApplicationComponent component) { this.applicationComponent = component;}
}
