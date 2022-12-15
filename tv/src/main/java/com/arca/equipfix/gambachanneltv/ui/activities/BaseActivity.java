package com.arca.equipfix.gambachanneltv.ui.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.arca.equipfix.gambachanneltv.GambaChannelApp;
import com.arca.equipfix.gambachanneltv.data.DataManager;
import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.network.model.RegistrationResponse;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.data.network.model.enums.RegistrationStatus;
import com.arca.equipfix.gambachanneltv.di.component.ActivityComponent;
import com.arca.equipfix.gambachanneltv.di.component.DaggerActivityComponent;
import com.arca.equipfix.gambachanneltv.di.module.ActivityModule;
import com.arca.equipfix.gambachanneltv.local_interfaces.BaseEvents;
import com.arca.equipfix.gambachanneltv.utils.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;

/**
 * Created by gabri on 6/14/2018.
 */

public abstract class BaseActivity extends Activity implements BaseEvents {

    private ActivityComponent mActivityComponent;

    private Unbinder mUnBinder;

    @Inject
    protected DataManager dataManager;

    protected List<ChannelProgram> reminders;

    protected RegistrationResponse registrationResponse;
    Profile currentProfile;


    protected  Handler registrationHandler ;
    protected  Runnable  registrationStatus = () -> {
        callRegistrationStatus(false); };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((GambaChannelApp) getApplication()).getApplicationComponent())
                .build();

        mActivityComponent.inject(this);



    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }


    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void openActivityOnTokenExpire() {
        Intent splashActivity = SplashActivity.getStartIntent(this);
        splashActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        splashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(splashActivity);
        finish();
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }


    protected void callRegistrationStatus(boolean includeProfiles)
    {
        dataManager.getRegistrationStatus(includeProfiles)
            .onErrorReturn(error ->
            {
                RegistrationResponse registrationResponse = new RegistrationResponse();
                registrationResponse.setStatus(RegistrationStatus.ERROR);
                return registrationResponse;
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(registrationResponse ->
            {
                dataManager.setRegistrationCode(registrationResponse.getRegistrationCode());
                this.registrationResponse = registrationResponse;
                onRegistration();
            });
    }

    protected void callStartSession( Profile profile)
    {

        currentProfile = profile;
        dataManager.startSession(profile)
                .onErrorReturn(error->
                {
                    SessionInformation sessionInformation = new SessionInformation();
                    sessionInformation.setAccessToken(EMPTY_STRING);
                    return sessionInformation;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sessionInformation -> {
                    if(! (sessionInformation.getAccessToken()==null) && !sessionInformation.getAccessToken().equals(EMPTY_STRING)) {
                        if(dataManager.setSessionInformation(sessionInformation.getAccessToken())) {
                            onSessionStarted(sessionInformation);
                        }
                        else
                        {
                            onSessionError();
                        }
                    }
                    else
                    {
                        onSessionError();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if(registrationHandler != null) {
            registrationHandler.removeCallbacks(registrationStatus);
            registrationHandler = null;
        }
        super.onDestroy();


    }




}
