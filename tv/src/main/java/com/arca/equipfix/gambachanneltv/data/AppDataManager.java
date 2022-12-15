package com.arca.equipfix.gambachanneltv.data;

import com.arca.equipfix.gambachanneltv.data.db.DbHelper;
import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.network.ApiHelper;
import com.arca.equipfix.gambachanneltv.data.network.model.Actor;
import com.arca.equipfix.gambachanneltv.data.network.model.EPGLine;
import com.arca.equipfix.gambachanneltv.data.network.model.Episode;
import com.arca.equipfix.gambachanneltv.data.network.model.Genre;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemCover;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemUrls;
import com.arca.equipfix.gambachanneltv.data.network.model.LastVersionInformation;
import com.arca.equipfix.gambachanneltv.data.network.model.LinkedDeviceInfo;
import com.arca.equipfix.gambachanneltv.data.network.model.MovieDetails;
import com.arca.equipfix.gambachanneltv.data.network.model.PlayEpisode;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.network.model.RegistrationResponse;
import com.arca.equipfix.gambachanneltv.data.network.model.Season;
import com.arca.equipfix.gambachanneltv.data.network.model.SerieDetails;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.data.prefs.PreferencesHelper;
import com.arca.equipfix.gambachanneltv.data.prefs.model.LoginType;
import com.arca.equipfix.gambachanneltv.utils.device_information.AppDeviceInformationHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;


public class AppDataManager implements DataManager {

    private DbHelper dbHelper;
    private PreferencesHelper preferencesHelper;
    private ApiHelper apiHelper;
    private AppDeviceInformationHelper deviceInformationHelper;

    @Inject
    AppDataManager(DbHelper dbHelper, PreferencesHelper preferencesHelper, ApiHelper apiHelper, AppDeviceInformationHelper appDeviceInformationHelper)
    {
        this.apiHelper = apiHelper;
        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
        this.deviceInformationHelper = appDeviceInformationHelper;
    }

    //Device Methods

    @Override
    public String getDeviceSerialNumber() {
        return deviceInformationHelper.getDeviceSerialNumber();
    }


    //Database Methods:

    @Override
    public boolean insertChannelProgram(ChannelProgram channelProgram) {
        //return dbHelper.insertChannelProgram(channelProgram);
        return  true;
    }

    @Override
    public List<ChannelProgram> getProgramsByChannel(int channelId,  Date afterNow) {
        //return  dbHelper.getProgramsByChannel(channelId, afterNow);
        return  null;
    }

    @Override
    public Date getLastDateChannel(int channelId) {
        //return dbHelper.getLastDateChannel(channelId);
        return Calendar.getInstance().getTime();
    }

    @Override
    public List<ChannelProgram> getReminders() {

        //return dbHelper.getReminders();
        return  null;
    }

    @Override
    public List<Profile> getLocalProfileList() {
        return dbHelper.getLocalProfileList();
    }

    @Override
    public boolean saveProfileList(List<Profile> profiles) {
        return dbHelper.saveProfileList(profiles);
    }

    @Override
    public Profile getSelectedProfile() {
        return dbHelper.getSelectedProfile();
    }

    @Override
    public boolean setSelectedProfile(long id) {
        return  dbHelper.setSelectedProfile(id);
    }

    @Override
    public boolean addProgramReminder(int channelId, String title, boolean unique) {
    //    return dbHelper.addProgramReminder(channelId, title, unique);
        return  true;
    }

    @Override
    public boolean deleteProgramReminder(int channelId, String title) {
        //return dbHelper.deleteProgramReminder(channelId, title);
        return  true;
    }

    //Api Methods

    @Override
    public Single<RegistrationResponse> getRegistrationStatus(boolean includeProfiles) {
        return apiHelper.getRegistrationStatus(includeProfiles);
    }

    @Override
    public Single<SessionInformation> startSession(Profile profile) {
        return  apiHelper.startSession(profile);
    }

    @Override
    public Single<LinkedDeviceInfo> getLinkedDeviceInfo() {
        return apiHelper.getLinkedDeviceInfo();
    }

    @Override
    public Single<Boolean> deleteLinkedDevice() {
        return  apiHelper.deleteLinkedDevice();
    }

    @Override
    public Observable<List<Genre>> getMoviesGenres() {
        return apiHelper.getMoviesGenres();
    }

    @Override
    public Observable<List<Actor>> getMoviesActors() {
        return apiHelper.getMoviesActors();
    }

    @Override
    public Observable<List<ItemCover>> getMovies(int contentType, String filter, int integerParameter, boolean isKids)
    {
        return apiHelper.getMovies(contentType, filter, integerParameter, isKids);

    }

    @Override
    public Single<MovieDetails> getMovieDetails(int movieId) {
        return  apiHelper.getMovieDetails(movieId);
    }

    @Override
    public Observable<List<Actor>> getMovieActors(int movieId) {
        return  apiHelper.getMovieActors(movieId);
    }

    @Override
    public Observable<List<Integer>> getMoviesYears() {
        return  apiHelper.getMoviesYears();
    }

    @Override
    public Observable<List<ItemCover>> getRelatedMovies(int movieId) {
        return  apiHelper.getRelatedMovies(movieId);
    }


    @Override
    public Single<ItemUrls> getMovieUrl(int movieId) {
        return  apiHelper.getMovieUrl(movieId);
    }

    @Override
    public void postMoviePlayed(int movieId, long duration) {
        apiHelper.postMoviePlayed(movieId, duration);
    }

    @Override
    public void setLastPositionMovie(int movieId, long lastPosition) {
        apiHelper.setLastPositionMovie(movieId, lastPosition);
    }

    @Override
    public void addFavoriteMovie(int movieId) {
        apiHelper.addFavoriteMovie(movieId);
    }

    @Override
    public void removeFavoriteMovie(int movieId) {
        apiHelper.removeFavoriteMovie(movieId);
    }

    @Override
    public Single<Boolean> validateAccessCode(String accessCode) {
        return apiHelper.validateAccessCode(accessCode);
    }

    @Override
    public void setLastPositionAndRateMovie(int movieId, int rating, long lastPosition) {
        apiHelper.setLastPositionAndRateMovie(movieId, rating, lastPosition);

    }

    @Override
    public Observable<List<EPGLine>> getChannelList(int category) {
        return  apiHelper.getChannelList(category);
    }

    @Override
    public Observable<List<EPGLine>> getKidsChannels() {
        return  apiHelper.getKidsChannels();
    }

    @Override
    public Observable<List<ChannelProgram>> getChannelPrograms(int channelId) {
        return apiHelper.getChannelPrograms(channelId);
    }

    @Override
    public Observable<List<Genre>> getLiveCategories() {
        return  apiHelper.getLiveCategories();
    }

/*    @Override
    public Single<Integer> addProgramReminder(int channelId, String titie, boolean unique) {
        return apiHelper.addProgramReminder(channelId, titie, unique);
    }

    @Override
    public Single<Boolean> deleteProgramReminder(int reminderId) {
        return  apiHelper.deleteProgramReminder(reminderId);
    }

    @Override
    public Observable<List<Reminder>> getReminders() {
        return apiHelper.getReminders();
    }

    */

    @Override
    public Single<ChannelProgram> getCurrentProgram(int channelId) {
        return apiHelper.getCurrentProgram(channelId);
    }

    @Override
    public Single<String> getLiveUrl(int channelId) {
        return apiHelper.getLiveUrl(channelId);
    }

    @Override
    public Observable<List<Actor>> getSerieActors(int serieId) {
        return apiHelper.getSerieActors(serieId);
    }

    @Override
    public Observable<List<Integer>> getSeriesYears() {
        return apiHelper.getSeriesYears();
    }

    @Override
    public Observable<List<String>> getSeriesLetters() {
        return apiHelper.getSeriesLetters();
    }

    @Override
    public Observable<List<Genre>> getSeriesGenres() {
        return apiHelper.getSeriesGenres();
    }

    @Override
    public Observable<List<ItemCover>> getRelatedSeries(int serieId) {
        return apiHelper.getRelatedSeries(serieId);
    }

    @Override
    public Observable<List<ItemCover>> getSeries(int contentType, String filter, int integerParameter, boolean isKids) {
        return apiHelper.getSeries(contentType, filter, integerParameter, isKids);
    }

    @Override
    public Single<SerieDetails> getSerieDetails(int serieId) {
        return apiHelper.getSerieDetails(serieId);
    }

    @Override
    public Observable<List<Season>> getSerieSeasons(int serieId) {
        return apiHelper.getSerieSeasons(serieId);
    }

    @Override
    public Observable<List<Episode>> getSeasonEpisodes(int seasonId) {
        return apiHelper.getSeasonEpisodes(seasonId);
    }

    @Override
    public Single<PlayEpisode> getNextEpisodeInformation(int serieId, int seasonId, int episodeId) {
        return  apiHelper.getNextEpisodeInformation(serieId, seasonId, episodeId);
    }

    @Override
    public Single<PlayEpisode> getEpisodeInformation(int episodeId) {
        return  apiHelper.getEpisodeInformation(episodeId);
    }

    @Override
    public void postEpisodePlayed(int episodeId, long duration) {
        apiHelper.postEpisodePlayed(episodeId,duration);
    }

    @Override
    public void setLastPositionEpisode(int episodeId, long lastPosition) {
        apiHelper.setLastPositionEpisode(episodeId, lastPosition);
    }

    @Override
    public void addFavoriteSerie(int serieId) {
        apiHelper.addFavoriteSerie(serieId);
    }

    @Override
    public void removeFavoriteSerie(int serieId) {
        apiHelper.removeFavoriteSerie(serieId);
    }

    @Override
    public Observable<List<Profile>> getProfileList() {
        return  apiHelper.getProfileList();
    }


    //Preferences Methods

    @Override
    public String getPreferredLanguage() {
        return preferencesHelper.getPreferredLanguage();
    }

    @Override
    public boolean setPreferredLanguage(String language) {
        return preferencesHelper.setPreferredLanguage(language);
    }

    @Override
    public boolean setSessionInformation(String accessToken) {
        return preferencesHelper.setSessionInformation(accessToken);
    }

    @Override
    public String getAccessToken() {
        return preferencesHelper.getAccessToken();
    }

    @Override
    public boolean setRegistrationCode(String registrationCode) {
        return  preferencesHelper.setRegistrationCode(registrationCode);
    }

    @Override
    public String getRegistrationCode() {
        return  preferencesHelper.getRegistrationCode();
    }


    @Override
    public String getSubtitlesLanguage() {
        return preferencesHelper.getSubtitlesLanguage();
    }

    @Override
    public boolean setSubtitlesLanguage(String subtitlesLanguage) {
        return  preferencesHelper.setSubtitlesLanguage(subtitlesLanguage);
    }

    @Override
    public String getAudioLanguage() {
        return preferencesHelper.getAudioLanguage();
    }

    @Override
    public boolean setAudioLanguage(String audioLanguage) {
        return  preferencesHelper.setAudioLanguage(audioLanguage);
    }

    @Override
    public boolean getSubtitlesEnabled() {
        return  preferencesHelper.getSubtitlesEnabled();
    }

    @Override
    public boolean setSubtitlesEnabled(boolean enabled) {
        return preferencesHelper.setSubtitlesEnabled(enabled);
    }

    @Override
    public LoginType getLoginType() {
        return preferencesHelper.getLoginType();
    }

    @Override
    public boolean setLoginType(LoginType loginType) {
        return preferencesHelper.setLoginType(loginType);
    }

    @Override
    public String getPreferredQuality() {
        return preferencesHelper.getPreferredQuality();
    }

    @Override
    public boolean setPreferredQuality(String quality) {
        return preferencesHelper.setPreferredQuality(quality);
    }


    @Override
    public Single<MovieDetails> getAdultMovieDetails(int movieId) {
        return apiHelper.getAdultMovieDetails(movieId);
    }

    @Override
    public Single<ItemUrls> getAdultMovieUrl(int movieId) {
        return  apiHelper.getAdultMovieUrl(movieId);
    }

    @Override
    public Observable<List<ItemCover>> getAdultRelatedMovies(int movieId) {
        return  apiHelper.getAdultRelatedMovies(movieId);
    }

    @Override
    public Observable<List<Genre>> getAdultMoviesGenres(boolean isGay) {
        return apiHelper.getAdultMoviesGenres(isGay);
    }

    @Override
    public Observable<List<Actor>> getAdultMoviesActors(boolean isGay) {
        return  apiHelper.getAdultMoviesActors(isGay);
    }

    @Override
    public Observable<List<Integer>> getAdultMoviesYears(boolean isGay) {
        return  apiHelper.getAdultMoviesYears(isGay);
    }

    @Override
    public Observable<List<Actor>> getAdultMovieActors(int movieId) {
        return apiHelper.getAdultMovieActors(movieId);
    }

    @Override
    public Observable<List<ItemCover>> getAdultMovies(int contentType, String filter, int integerParameter, boolean isGay) {
        return  apiHelper.getAdultMovies(contentType, filter, integerParameter, isGay);
    }

    @Override
    public void addAdultFavoriteMovie(int movieId) {
        apiHelper.addAdultFavoriteMovie(movieId);
    }

    @Override
    public void removeAdultFavoriteMovie(int movieId) {
        apiHelper.removeAdultFavoriteMovie(movieId);
    }

    @Override
    public void postAdultMoviePlayed(int movieId, long duration) {
        apiHelper.postAdultMoviePlayed(movieId, duration);
    }

    @Override
    public void setLastPositionAndRateAdultMovie(int movieId, int rating, long lastPosition) {
        apiHelper.setLastPositionAndRateAdultMovie(movieId, rating, lastPosition);
    }

    @Override
    public void setAdultLastPositionMovie(int movieId, long lastPosition) {
        apiHelper.setAdultLastPositionMovie(movieId,lastPosition);
    }

    @Override
    public Single<Boolean> validateProfilePassword(int profileId, String accessCode) {
        return apiHelper.validateProfilePassword(profileId, accessCode);
    }

    @Override
    public Single<Integer> createProfile(Profile profile) {
        return  apiHelper.createProfile(profile);
    }

    @Override
    public Single<Boolean> updateProfile(Profile profile) {
        return  apiHelper.updateProfile(profile);
    }

    @Override
    public Single<Boolean> updateAccessCode(String accessCode) {
        return  apiHelper.updateAccessCode(accessCode);
    }

    @Override
    public boolean reminderShowed(int channelId, int programId, String startTime) {
        return  preferencesHelper.reminderShowed(channelId, programId, startTime);
    }

    @Override
    public boolean setReminderShowed(int channelId, int programId, String startTime) {
        return  preferencesHelper.setReminderShowed(channelId, programId, startTime);
    }

    @Override
    public Single<LastVersionInformation> getLastVersion() {
        return apiHelper.getLastVersion();
    }

    @Override
    public void downloadLastVersion(String url, String dirPath, String fileName) {
        apiHelper.downloadLastVersion(url, dirPath, fileName);
    }




}
