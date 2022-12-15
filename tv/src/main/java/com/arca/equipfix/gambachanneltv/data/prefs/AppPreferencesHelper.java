package com.arca.equipfix.gambachanneltv.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.arca.equipfix.gambachanneltv.data.prefs.model.LoginType;
import com.arca.equipfix.gambachanneltv.di.ApplicationContext;
import com.arca.equipfix.gambachanneltv.di.PreferencesName;

import javax.inject.Inject;
import javax.inject.Singleton;


import static com.arca.equipfix.gambachanneltv.utils.AppConstants.DEFAULT_LANGUAGE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.DEFAULT_QUALITY;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;

/**
 * Created by gabri on 6/14/2018.
 */

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_DEFAULT_LANGUAGE = "PREF_KEY_DEFAULT_LANGUAGE";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_REGISTRATION_CODE = "PREF_KEY_REGISTRATION_CODE";
    private static final String PREF_KEY_SUBTITLES_ENABLED = "prefs_subtitles_enabled";
    public static final String PREF_KEY_AUDIO_LANGUAGE = "prefs_audio_language_key";


    public static final String PREF_KEY_SUBTITLES_LANGUAGE = "prefs_subtitles_language_key";

    public static final String PREF_KEY_LOGIN_TYPE = "PREF_KEY_LOGIN_TYPE";
    public static final String PREF_KEY_PREFERRED_QUALITY = "PREF_KEY_PREFERRED_QUALITY";
    public static final String PREF_KEY_PROFILFES ="PREF_KEY_PROFILFES";
    public static final String PREF_KEY_REMINDER_SHOWED = "PREF_KEY_REMINDER_SHOWED";


    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferencesName String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }


    @Override
    public String getPreferredLanguage() {
        return mPrefs.getString(PREF_KEY_DEFAULT_LANGUAGE, DEFAULT_LANGUAGE);
    }

    @Override
    public boolean setPreferredLanguage(String language) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_KEY_DEFAULT_LANGUAGE, language);
        return editor.commit();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, EMPTY_STRING);
    }

    @Override
    public boolean setSessionInformation(String accessToken) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_KEY_ACCESS_TOKEN, accessToken);
        return editor.commit();
    }

    @Override
    public String getRegistrationCode() {
        return mPrefs.getString(PREF_KEY_REGISTRATION_CODE, EMPTY_STRING);
    }

    @Override
    public boolean setRegistrationCode(String registrationCode) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_KEY_REGISTRATION_CODE, registrationCode);
        return editor.commit();

    }

    @Override
    public boolean getSubtitlesEnabled() {
        return mPrefs.getBoolean(PREF_KEY_SUBTITLES_ENABLED, false);
    }

    @Override
    public boolean setSubtitlesEnabled(boolean enabled) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(PREF_KEY_SUBTITLES_ENABLED, enabled);
        return editor.commit();
    }

    @Override
    public String getAudioLanguage() {
        return mPrefs.getString(PREF_KEY_AUDIO_LANGUAGE, DEFAULT_LANGUAGE);
    }

    @Override
    public boolean setAudioLanguage(String audioLanguage) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_KEY_AUDIO_LANGUAGE, audioLanguage);
        return editor.commit();
    }

    @Override
    public String getSubtitlesLanguage() {
        return mPrefs.getString(PREF_KEY_SUBTITLES_LANGUAGE, DEFAULT_LANGUAGE);
    }

    @Override
    public boolean setSubtitlesLanguage(String subtitlesLanguage) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_KEY_SUBTITLES_LANGUAGE, subtitlesLanguage);
        return editor.commit();
    }

    @Override
    public LoginType getLoginType() {
        return LoginType.values()[mPrefs.getInt(PREF_KEY_LOGIN_TYPE, 0)];
    }

    public boolean setLoginType(LoginType loginType)
    {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(PREF_KEY_LOGIN_TYPE, loginType.getValue());
        return editor.commit();
    }

    @Override
    public String getPreferredQuality() {
        return mPrefs.getString(PREF_KEY_PREFERRED_QUALITY, DEFAULT_QUALITY);
    }

    @Override
    public boolean setPreferredQuality(String quality) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_KEY_PREFERRED_QUALITY, quality);
        return editor.commit();
    }

    @Override
    public boolean setReminderShowed(int channelId, int programId, String startTime) {
        SharedPreferences.Editor editor = mPrefs.edit();
        String reminderString = String.format("%d-%d-%s", channelId, programId, startTime);
        editor.putString(PREF_KEY_REMINDER_SHOWED, reminderString);
        return editor.commit();
    }

    @Override
    public boolean reminderShowed(int channelId, int programId, String startTime) {
        String lastReminder = mPrefs.getString(PREF_KEY_REMINDER_SHOWED, EMPTY_STRING);
        String reminderString = String.format("%d-%d-%s", channelId, programId, startTime);
        return reminderString.equals(lastReminder);
    }
}

