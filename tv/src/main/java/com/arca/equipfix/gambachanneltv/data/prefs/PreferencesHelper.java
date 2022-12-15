package com.arca.equipfix.gambachanneltv.data.prefs;

import com.arca.equipfix.gambachanneltv.data.prefs.model.LoginType;

/**
 * Created by gabri on 6/14/2018.
 */

public interface PreferencesHelper {

    String getPreferredLanguage();
    boolean setPreferredLanguage(String language);
    String getAccessToken();
    boolean setSessionInformation(String accessToken);
    String getRegistrationCode();
    boolean setRegistrationCode(String registrationCode);
    boolean getSubtitlesEnabled();
    boolean setSubtitlesEnabled(boolean enabled);
    String getSubtitlesLanguage();
    boolean setSubtitlesLanguage(String subtitlesLanguage);
    String getAudioLanguage();
    boolean setAudioLanguage(String audioLanguage);
    LoginType getLoginType();
    boolean setLoginType(LoginType loginType);
    String getPreferredQuality();
    boolean setPreferredQuality(String quality);
    boolean reminderShowed(int channelId, int programId, String startTime);
    boolean setReminderShowed(int channelId, int programId, String startTime);

}
