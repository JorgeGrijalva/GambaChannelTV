package com.arca.equipfix.gambachanneltv.data.network;

import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
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

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by gabri on 6/13/2018.
 */

public interface ApiHelper {

    //Registration
    Single<RegistrationResponse> getRegistrationStatus(boolean includeProfiles);
    Single<SessionInformation>startSession(Profile profile);

    //Linked Device
    Single<LinkedDeviceInfo>getLinkedDeviceInfo();
    Single<Boolean>deleteLinkedDevice();

    //Movies
    Observable<List<Genre>> getMoviesGenres();
    Observable<List<Actor>> getMoviesActors();
    Observable<List<Integer>> getMoviesYears();

    Observable<List<ItemCover>> getMovies(int contentType, String filter, int integerParameter, boolean isKids);
    Single<MovieDetails> getMovieDetails(int movieId);
    Observable<List<Actor>> getMovieActors(int movieId);
    Observable<List<ItemCover>> getRelatedMovies(int movieId);


    Single<ItemUrls> getMovieUrl(int movieId);
    void postMoviePlayed(int movieId, long duration);
    void setLastPositionMovie(int movieId, long lastPosition);
    void addFavoriteMovie(int movieId);
    void removeFavoriteMovie(int movieId);
    void setLastPositionAndRateMovie(int movieId, int rating, long lastPosition);


    //Series
    Observable<List<Genre>> getSeriesGenres();
    Observable<List<Integer>> getSeriesYears();
    Observable<List<String>> getSeriesLetters();
    Observable<List<ItemCover>> getSeries(int contentType, String filter, int integerParameter, boolean isKids);
    Single<SerieDetails> getSerieDetails(int serieId);
    Observable<List<Actor>> getSerieActors(int serieId);
    Observable<List<ItemCover>> getRelatedSeries(int serieId);
    Observable<List<Season>> getSerieSeasons(int serieId);
    Observable<List<Episode>> getSeasonEpisodes(int seasonId);
    Single<PlayEpisode> getNextEpisodeInformation(int serieId, int seasonId, int episodeId);
    Single<PlayEpisode> getEpisodeInformation(int episodeId);


    void postEpisodePlayed(int episodeId, long duration);
    void setLastPositionEpisode(int episodeId , long lastPosition);
    void addFavoriteSerie(int seridId);
    void removeFavoriteSerie(int seridId);



    //Live Channels
    Observable<List<EPGLine>> getChannelList(int category);
    Observable<List<ChannelProgram>> getChannelPrograms(int channelId);
    Observable<List<Genre>> getLiveCategories();
/*    Single<Integer> addProgramReminder(int channelId, String titie, boolean unique);
    Single<Boolean> deleteProgramReminder(int reminderId);
    Observable<List<Reminder>> getReminders();*/
    Single<ChannelProgram> getCurrentProgram(int channelId);
    Observable<List<EPGLine>> getKidsChannels();
    Single<String>getLiveUrl(int channelId);


    //Adults

    Observable<List<Genre>> getAdultMoviesGenres(boolean isGay);
    Observable<List<Actor>> getAdultMoviesActors(boolean isGay);
    Observable<List<Integer>> getAdultMoviesYears(boolean isGay);

    Observable<List<ItemCover>> getAdultMovies(int contentType, String filter, int integerParameter, boolean isGay);
    Single<MovieDetails> getAdultMovieDetails(int movieId);
    Observable<List<Actor>> getAdultMovieActors(int movieId);
    Observable<List<ItemCover>> getAdultRelatedMovies(int movieId);


    Single<ItemUrls> getAdultMovieUrl(int movieId);
    void postAdultMoviePlayed(int movieId, long duration);
    void setAdultLastPositionMovie(int movieId, long lastPosition);
    void addAdultFavoriteMovie(int movieId);
    void removeAdultFavoriteMovie(int movieId);
    void setLastPositionAndRateAdultMovie(int movieId, int rating, long lastPosition);

    //Profiles

    Single<Integer> createProfile(Profile profile);
    Single<Boolean> updateProfile(Profile profile);
    Single<Boolean> updateAccessCode(String accessCode);



    //Generic
    Single<Boolean> validateAccessCode(String accessCode);
    Observable<List<Profile>> getProfileList();
    Single<Boolean> validateProfilePassword(int profileId, String accessCode);
    Single<LastVersionInformation> getLastVersion();
    void downloadLastVersion(String url, String dirPath, String fileName);

}
