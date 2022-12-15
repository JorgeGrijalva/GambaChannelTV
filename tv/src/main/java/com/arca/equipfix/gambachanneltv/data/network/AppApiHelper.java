package com.arca.equipfix.gambachanneltv.data.network;


import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.arca.equipfix.gambachanneltv.BuildConfig;
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
import com.arca.equipfix.gambachanneltv.data.prefs.PreferencesHelper;
import com.arca.equipfix.gambachanneltv.utils.Utility;
import com.arca.equipfix.gambachanneltv.utils.device_information.AppDeviceInformationHelper;
import com.rx2androidnetworking.Rx2ANRequest;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.List;

import javax.inject.Inject;

import io.michaelrocks.paranoid.Obfuscate;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Response;


import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.ACCESS_CODE;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.ACCESS_TYPE;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.APPLICATION_FORM_URL_ENCODED;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.APPLICATION_JSON;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.BEARER_HEADER;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.CODE_VERSION;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.CONTENT_TYPE;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.DEVICE_SECRET;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.DEVICE_TYPE;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.ENABLE_ADULTS;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.ENDPOINT_GET_EPISODE_INFORMATION;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.ENDPOINT_GET_NEXT_EPISODE;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.ENDPOINT_GET_NEXT_EPISODE_SEASON;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.ENDPOINT_GET_NEXT_EPISODE_SERIE;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.HASH;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.HEADER_AUTHORIZATION;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.INCLUDE_PROFILES;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.PASSWORD_PROTECTED;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.PASSWORD_STRING;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.PROFILE_ID;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.PROFILE_NAME;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.PROFILE_PASSWORD;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.PROFILE_TOKEN;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.REGISTRATION_CODE;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.SERIAL_NUMBER;
import static com.arca.equipfix.gambachanneltv.data.network.ApiEndPoint.SERIAL_NUMBER_SECRET;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_FAVORITES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_NEW_RELEASES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_VIEWED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_MOVIES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_MOVIES_FAVORITES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_MOVIES_NEW_RELEASES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_MOVIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_MOVIES_VIEWED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_SERIES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_SERIES_FAVORITES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_SERIES_NEW_RELEASES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_SERIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_SERIES_VIEWED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_FAVORITES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_NEW_RELEASES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_VIEWED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_BY_LETTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_FAVORITES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_NEW_RELEASES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_VIEWED;

@Obfuscate
public class AppApiHelper implements  ApiHelper{



    private AppDeviceInformationHelper appDeviceInformationHelper;
    private PreferencesHelper preferencesHelper;


    private String[] getAccessInformation(Profile profile)
    {
        String serialNumber = appDeviceInformationHelper.getDeviceSerialNumber();
        String registrationCode = preferencesHelper.getRegistrationCode();
        String codeVersion = String.valueOf(BuildConfig.VERSION_CODE);
        String stringToHash = String.format("%s%s%s%s%s",serialNumber,  registrationCode, profile.getProfileToken(), codeVersion, DEVICE_SECRET);
        String hash = Utility.md5(stringToHash);

        return new String[]
        {
                serialNumber, registrationCode, codeVersion, hash, PASSWORD_STRING
        };

    }

    private String[] getRegistrationInformation()
    {
        String serialNumber = appDeviceInformationHelper.getDeviceSerialNumber();
        String deviceType = appDeviceInformationHelper.getDeviceType();
        String codeVersion = String.valueOf(BuildConfig.VERSION_CODE);
        String stringToHash = String.format("%s%s%s%s",serialNumber,  deviceType, codeVersion, SERIAL_NUMBER_SECRET);
        String hash = Utility.md5(stringToHash);

        return new String[]
                {
                        serialNumber, deviceType, codeVersion, hash
                };

    }


    @Inject
    AppApiHelper(AppDeviceInformationHelper appDeviceInformationHelper,
                         PreferencesHelper preferencesHelper)
    {
        this.appDeviceInformationHelper = appDeviceInformationHelper;
        this.preferencesHelper = preferencesHelper;

    }

    @Override
    public Single<LinkedDeviceInfo> getLinkedDeviceInfo() {
        String serialNumber = appDeviceInformationHelper.getDeviceSerialNumber();
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_LINKED_DEVICE)
                .addHeaders(CONTENT_TYPE,APPLICATION_JSON )
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addQueryParameter(SERIAL_NUMBER, serialNumber)
                .build()
                .getObjectSingle(LinkedDeviceInfo.class);
    }

    @Override
    public Single<Boolean> deleteLinkedDevice() {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_DELETE_LINKED_DEVICE)
                .addHeaders(CONTENT_TYPE,APPLICATION_JSON )
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .build()
                .getObjectSingle(Boolean.class);
    }

    @Override
    public Single<RegistrationResponse> getRegistrationStatus(boolean includeProfiles) {
        String[] accessInformation = getRegistrationInformation();
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_REGISTRATION_STATUS)
                .addHeaders(CONTENT_TYPE,APPLICATION_JSON )
                .addQueryParameter(SERIAL_NUMBER, accessInformation[0])
                .addQueryParameter(DEVICE_TYPE, accessInformation[1])
                .addQueryParameter(CODE_VERSION, accessInformation[2])
                .addQueryParameter(HASH, accessInformation[3])
                .addQueryParameter(INCLUDE_PROFILES, String.valueOf(includeProfiles))
                .build()
                .getObjectSingle(RegistrationResponse.class);

    }

    @Override
    public Single<SessionInformation> startSession(Profile profile) {
        String[] accessInformation = getAccessInformation(profile);

        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_ACCESS)
                .addHeaders(CONTENT_TYPE, APPLICATION_FORM_URL_ENCODED)
                .addUrlEncodeFormBodyParameter(SERIAL_NUMBER, accessInformation[0])
                .addUrlEncodeFormBodyParameter(REGISTRATION_CODE, accessInformation[1])
                .addUrlEncodeFormBodyParameter(PROFILE_TOKEN, profile.getProfileToken())
                .addUrlEncodeFormBodyParameter(CODE_VERSION, accessInformation[2])
                .addUrlEncodeFormBodyParameter(HASH, accessInformation[3])
                .addUrlEncodeFormBodyParameter(ACCESS_TYPE, accessInformation[4])
                .build()
                .getObjectSingle(SessionInformation.class);
    }

    @Override
    public Observable<List<Genre>> getMoviesGenres() {
         return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_MOVIES_GENRES)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectListObservable(Genre.class);
    }

    @Override
    public Observable<List<Actor>> getMoviesActors() {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_MOVIES_ACTORS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .build()
                .getObjectListObservable(Actor.class);
    }

    @Override
    public Observable<List<Integer>> getMoviesYears() {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_MOVIES_YEARS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .build()
                .getObjectListObservable(Integer.class);
    }

    @Override
    public Observable<List<ItemCover>> getMovies(int contentType, String filter, int integerParameter, boolean isKids) {
        String url = EMPTY_STRING;

        switch (contentType)
        {
            case GET_MOVIES_NEW_RELEASES:
            case GET_KIDS_MOVIES_NEW_RELEASES:
                url = ApiEndPoint.ENDPOINT_GET_NEW_RELEASES_MOVIES;
                break;
            case GET_MOVIES_RECENT_ADDED:
            case GET_KIDS_MOVIES_RECENT_ADDED:
                url = ApiEndPoint.ENDPOINT_GET_LAST_ADDED_MOVIES;
                break;
            case GET_MOVIES_VIEWED:
            case GET_KIDS_MOVIES_VIEWED:
                url = ApiEndPoint.ENDPOINT_GET_VIEWED_MOVIES;
                break;
            case GET_MOVIES_FAVORITES:
            case GET_KIDS_MOVIES_FAVORITES:
                url = ApiEndPoint.ENDPOINT_GET_FAVORITES_MOVIES;
                break;
            case GET_KIDS_MOVIES:
                url = ApiEndPoint.ENDPOINT_GET_KIDS_MOVIES;
                break;
            case GET_MOVIES_BY_GENRE:
                url = ApiEndPoint.ENDPOINT_GET_MOVIES_BY_GENRE;
                break;
            case GET_MOVIES_BY_ACTOR:
                url = ApiEndPoint.ENDPOINT_GET_MOVIES_BY_ACTOR;
                break;
            case GET_MOVIES_BY_YEAR:
                url = ApiEndPoint.ENDPOINT_GET_MOVIES_BY_YEAR;
                break;

        }

        Rx2ANRequest.GetRequestBuilder getRequestBuilder = Rx2AndroidNetworking.get(url)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .addPathParameter("isKids", String.valueOf(isKids))
                .addQueryParameter("filter", filter );
        if(integerParameter>0)
        {
            switch (contentType)
            {
                case GET_MOVIES_BY_GENRE:
                case GET_MOVIES_BY_ACTOR:
                case GET_MOVIES_BY_YEAR:
                    getRequestBuilder = getRequestBuilder.addPathParameter("id", String.valueOf(integerParameter));
                    break;
            }
        }

        return getRequestBuilder.build().getObjectListObservable(ItemCover.class);
    }

    @Override
    public Single<MovieDetails> getMovieDetails(int movieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_MOVIE_DETAILS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectSingle(MovieDetails.class);

    }

    @Override
    public Observable<List<Actor>> getMovieActors(int movieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_MOVIE_ACTORS)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .build()
                .getObjectListObservable(Actor.class);
    }

    @Override
    public Observable<List<ItemCover>> getRelatedMovies(int movieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_RELATED_MOVIES)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .addPathParameter("id", String.valueOf(movieId))
                .build()
                .getObjectListObservable(ItemCover.class);

    }


    @Override
    public Single<ItemUrls> getMovieUrl(int movieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_MOVIE_URL)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("quality", preferencesHelper.getPreferredQuality())
                .build()
                .getObjectSingle(ItemUrls.class);
    }

    @Override
    public void postMoviePlayed(int movieId, long duration) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_SET_MOVIE_PLAYED)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("duration", String.valueOf(duration))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    @Override
    public void setLastPositionMovie(int movieId, long lastPosition) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_SET_LAST_POSITION_MOVIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("lastPosition", String.valueOf(lastPosition))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    @Override
    public void addFavoriteMovie(int movieId) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_ADD_FAVORITE_MOVIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });

    }

    @Override
    public void removeFavoriteMovie(int movieId) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_REMOVE_FAVORITE_MOVIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    @Override
    public Single<Boolean> validateAccessCode(String accessCode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_VALIDATE_ACCESS_CODE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("accessCode", accessCode)
                .build()
                .getObjectSingle(Boolean.class);
    }

    @Override
    public void setLastPositionAndRateMovie(int movieId, int rating, long lastPosition) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_RATE_MOVIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("rating", String.valueOf(rating))
                .addPathParameter("lastPosition", String.valueOf(lastPosition))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }


    @Override
    public Observable<List<EPGLine>> getChannelList(int category) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_CHANNEL_LIST)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("category", String.valueOf(category))
                .build()
                .getObjectListObservable(EPGLine.class);

    }

    @Override
    public Observable<List<EPGLine>> getKidsChannels() {
        return Rx2AndroidNetworking.get(ApiEndPoint.GET_KIDS_CHANNELS)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .build()
                .getObjectListObservable(EPGLine.class);
    }

    @Override
    public Observable<List<ChannelProgram>> getChannelPrograms(int channelId) {
/*        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_PROGRAMS_BY_CHANNEL)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("channel", String.valueOf(channelId))
                .build()
                .getObjectListObservable(ChannelProgram.class);
                */
        return null;

    }

    @Override
    public Observable<List<Genre>> getLiveCategories() {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_LIVE_GENRES)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectListObservable(Genre.class);
    }

    /*@Override
    public Single<Integer> addProgramReminder(int channelId, String titie, boolean unique) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_ADD_REMINDER)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("channelId", String.valueOf( channelId))
                .addPathParameter("title", titie)
                .addPathParameter("unique", String.valueOf(unique))
                .build().getObjectSingle(Integer.class);
    }

    @Override
    public Single<Boolean> deleteProgramReminder(int reminderId) {
        return Rx2AndroidNetworking.delete(ApiEndPoint.ENDPOINT_DELETE_REMINDER)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("reminderId", String.valueOf( reminderId))
                .build().getObjectSingle(Boolean.class);
    }

    @Override
    public Observable<List<Reminder>> getReminders() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_REMINDERS)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .build().getObjectListObservable(Reminder.class);
    }*/

    @Override
    public Single<ChannelProgram> getCurrentProgram(int channelId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_CURRENT_PROGRAM)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("channelId", String.valueOf( channelId))
                .build().getObjectSingle(ChannelProgram.class);
    }

    @Override
    public Single<String> getLiveUrl(int channelId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_LIVE_CHANNEL_URL   )
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("channelId", String.valueOf( channelId))
                .build().getObjectSingle(String.class);
    }

    @Override
    public Observable<List<Genre>> getSeriesGenres() {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_SERIES_GENRES)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectListObservable(Genre.class);
    }

    @Override
    public Observable<List<Integer>> getSeriesYears() {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_SERIES_YEARS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectListObservable(Integer.class);
    }

    @Override
    public Observable<List<String>> getSeriesLetters() {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_SERIES_LETTERS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectListObservable(String.class);
    }

    @Override
    public Observable<List<Actor>> getSerieActors(int serieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_SERIE_ACTORS)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(serieId))
                .build()
                .getObjectListObservable(Actor.class);
    }

    @Override
    public Observable<List<ItemCover>> getRelatedSeries(int serieId) {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_RELATED_SERIES)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectListObservable(ItemCover.class);
    }

    @Override
    public Observable<List<ItemCover>> getSeries(int contentType, String filter, int integerParameter, boolean isKids) {
        String url = EMPTY_STRING;

        switch (contentType)
        {
            case GET_SERIES_NEW_RELEASES:
            case GET_KIDS_SERIES_NEW_RELEASES:
                url = ApiEndPoint.ENDPOINT_GET_NEW_RELEASES_SERIES;
                break;
            case GET_SERIES_RECENT_ADDED:
            case GET_KIDS_SERIES_RECENT_ADDED:
                url = ApiEndPoint.ENDPOINT_GET_LAST_ADDED_SERIES;
                break;
            case GET_SERIES_VIEWED:
            case GET_KIDS_SERIES_VIEWED:
                url = ApiEndPoint.ENDPOINT_GET_VIEWED_SERIES;
                break;
            case GET_SERIES_FAVORITES:
            case GET_KIDS_SERIES_FAVORITES:
                url = ApiEndPoint.ENDPOINT_GET_FAVORITES_SERIES;
                break;
            case GET_KIDS_SERIES:
                url = ApiEndPoint.ENDPOINT_GET_KIDS_SERIES;
                break;
            case GET_SERIES_BY_GENRE:
                url = ApiEndPoint.ENDPOINT_GET_SERIES_BY_GENRE;
                break;
            case GET_SERIES_BY_YEAR:
                url = ApiEndPoint.ENDPOINT_GET_SERIES_BY_YEAR;
                break;
            case GET_SERIES_BY_LETTER:
                url = ApiEndPoint.ENDPOINT_GET_SERIES_BY_LETTER;
                break;

        }

        Rx2ANRequest.GetRequestBuilder getRequestBuilder = Rx2AndroidNetworking.get(url)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .addPathParameter("isKids", String.valueOf(isKids))
                .addQueryParameter("filter", filter );
        if(integerParameter>0)
        {
            switch (contentType)
            {
                case GET_SERIES_BY_GENRE:
                case GET_SERIES_BY_YEAR:
                    getRequestBuilder = getRequestBuilder.addPathParameter("id", String.valueOf(integerParameter));
                    break;
            }
        }

        if(contentType == GET_SERIES_BY_LETTER)
        {
            getRequestBuilder = getRequestBuilder.addPathParameter("letter", filter);
        }


        return getRequestBuilder.build().getObjectListObservable(ItemCover.class);
    }

    @Override
    public Single<SerieDetails> getSerieDetails(int serieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_SERIE_DETAILS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("id", String.valueOf(serieId))
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectSingle(SerieDetails.class);

    }

    @Override
    public Observable<List<Season>> getSerieSeasons(int serieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_SERIE_SEASONS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("id", String.valueOf(serieId))
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectListObservable(Season.class);
    }

    @Override
    public Observable<List<Episode>> getSeasonEpisodes(int seasonId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_SEASON_EPISODES)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("id", String.valueOf(seasonId))
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectListObservable(Episode.class);
    }

    @Override
    public Single<PlayEpisode> getNextEpisodeInformation(int serieId, int seasonId, int episodeId) {

        String url = EMPTY_STRING;
        int id = 0;
        if(episodeId > 0)
        {
            url = ENDPOINT_GET_NEXT_EPISODE;
            id = episodeId;
        }
        else
        {
            if(seasonId > 0)
            {
                url = ENDPOINT_GET_NEXT_EPISODE_SEASON;
                id = seasonId;
            }
            else
            {
                if(serieId> 0)
                {
                    url = ENDPOINT_GET_NEXT_EPISODE_SERIE;
                    id = serieId;
                }
            }
        }

        return Rx2AndroidNetworking.get(url)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("id", String.valueOf(id))
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectSingle(PlayEpisode.class);
    }

    @Override
    public Single<PlayEpisode> getEpisodeInformation(int episodeId) {
        return Rx2AndroidNetworking.get(ENDPOINT_GET_EPISODE_INFORMATION)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("id", String.valueOf(episodeId))
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectSingle(PlayEpisode.class);
    }

    @Override
    public void postEpisodePlayed(int episodeId, long duration) {

        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_SET_EPISODE_PLAYED)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(episodeId))
                .addPathParameter("duration", String.valueOf(duration))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    @Override
    public void addFavoriteSerie(int serieId) {

        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_ADD_FAVORITE_SERIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(serieId))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    @Override
    public void removeFavoriteSerie(int serieId) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_REMOVE_FAVORITE_SERIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(serieId))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    @Override
    public void setLastPositionEpisode(int episodeId, long lastPosition) {

        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_SET_LAST_POSITION_EPISODE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(episodeId))
                .addPathParameter("lastPosition", String.valueOf(lastPosition))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    @Override
    public Observable<List<ItemCover>> getAdultMovies(int contentType, String filter, int integerParameter, boolean isGay) {
        String url = EMPTY_STRING;

        switch (contentType)
        {
            case GET_ADULTS_MOVIES_NEW_RELEASES:
                url = ApiEndPoint.ENDPOINT_GET_NEW_RELEASES_ADULTS_MOVIES;
                break;
            case GET_ADULTS_MOVIES_RECENT_ADDED:
                url = ApiEndPoint.ENDPOINT_GET_LAST_ADDED_ADULTS_MOVIES;
                break;
            case GET_ADULTS_MOVIES_VIEWED:
                url = ApiEndPoint.ENDPOINT_GET_VIEWED_ADULTS_MOVIES;
                break;
            case GET_ADULTS_MOVIES_FAVORITES:
                url = ApiEndPoint.ENDPOINT_GET_FAVORITES_ADULTS_MOVIES;
                break;
            case GET_ADULTS_MOVIES_BY_GENRE:
                url = ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIES_BY_GENRE;
                break;
            case GET_ADULTS_MOVIES_BY_ACTOR:
                url = ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIES_BY_ACTOR;
                break;
            case GET_ADULTS_MOVIES_BY_YEAR:
                url = ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIES_BY_YEAR;
                break;

        }

        Rx2ANRequest.GetRequestBuilder getRequestBuilder = Rx2AndroidNetworking.get(url)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .addPathParameter("isGay", String.valueOf(isGay))
                .addQueryParameter("filter", filter );
        if(integerParameter>0)
        {
            switch (contentType)
            {
                case GET_ADULTS_MOVIES_BY_GENRE:
                case GET_ADULTS_MOVIES_BY_YEAR:
                case GET_ADULTS_MOVIES_BY_ACTOR:
                    getRequestBuilder = getRequestBuilder.addPathParameter("id", String.valueOf(integerParameter));
                    break;
            }
        }

        return getRequestBuilder.build().getObjectListObservable(ItemCover.class);
    }

    @Override
    public Observable<List<Actor>> getAdultMovieActors(int movieId) {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIE_ACTORS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .build()
                .getObjectListObservable(Actor.class);
    }

    @Override
    public Observable<List<Integer>> getAdultMoviesYears(boolean isGay) {

        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIES_YEARS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("isGay", String.valueOf(isGay))
                .build()
                .getObjectListObservable(Integer.class);
    }

    @Override
    public Observable<List<Actor>> getAdultMoviesActors(boolean isGay)
    {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIES_ACTORS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("isGay", String.valueOf(isGay))
                .build()
                .getObjectListObservable(Actor.class);
    }

    @Override
    public Observable<List<Genre>> getAdultMoviesGenres(boolean isGay) {
        return  Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIES_GENRES)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER + preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .addPathParameter("isGay", String.valueOf(isGay))
                .build()
                .getObjectListObservable(Genre.class);
    }

    @Override
    public Observable<List<ItemCover>> getAdultRelatedMovies(int movieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ADULTS_RELATED_MOVIES)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .addPathParameter("id", String.valueOf(movieId))
                .build()
                .getObjectListObservable(ItemCover.class);
    }

    @Override
    public Single<ItemUrls> getAdultMovieUrl(int movieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIE_URL)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("quality", preferencesHelper.getPreferredQuality())
                .build()
                .getObjectSingle(ItemUrls.class);
    }

    @Override
    public Single<MovieDetails> getAdultMovieDetails(int movieId) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_ADULTS_MOVIE_DETAILS)
                .addHeaders(HEADER_AUTHORIZATION, BEARER_HEADER+ preferencesHelper.getAccessToken() )
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("language", preferencesHelper.getPreferredLanguage())
                .build()
                .getObjectSingle(MovieDetails.class);
    }

    @Override
    public void addAdultFavoriteMovie(int movieId) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_ADD_FAVORITE_ADULTS_MOVIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });

    }

    @Override
    public void removeAdultFavoriteMovie(int movieId) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_REMOVE_FAVORITE_ADULTS_MOVIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });

    }

    @Override
    public void postAdultMoviePlayed(int movieId, long duration) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_SET_ADULTS_MOVIE_PLAYED)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("duration", String.valueOf(duration))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });

    }

    @Override
    public void setAdultLastPositionMovie(int movieId, long lastPosition) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_SET_LAST_POSITION_ADULTS_MOVIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("lastPosition", String.valueOf(lastPosition))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });


    }

    @Override
    public void setLastPositionAndRateAdultMovie(int movieId, int rating, long lastPosition) {
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_POST_RATE_ADULTS_MOVIE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("id", String.valueOf(movieId))
                .addPathParameter("rating", String.valueOf(rating))
                .addPathParameter("lastPosition", String.valueOf(lastPosition))
                .build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onError(ANError anError) {

            }
        });

    }

    @Override
    public Observable<List<Profile>> getProfileList() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_PROFILES_DEVICE)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .build()
                .getObjectListObservable(Profile.class);
    }

    @Override
    public Single<Boolean> validateProfilePassword(int profileId, String accessCode) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_VALIDATE_PROFILE_PASSWORD)
                .addHeaders(CONTENT_TYPE, APPLICATION_JSON)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addPathParameter("profileId", String.valueOf(profileId))
                .addPathParameter("accessCode", accessCode)
                .build()
                .getObjectSingle(Boolean.class);
    }

    @Override
    public Single<Integer> createProfile(Profile profile) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_CREATE_PROFILE)
                .addHeaders(CONTENT_TYPE, APPLICATION_FORM_URL_ENCODED)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addUrlEncodeFormBodyParameter(PROFILE_NAME, profile.getName())
                .addUrlEncodeFormBodyParameter(ENABLE_ADULTS, String.valueOf(profile.getEnableAdults()))
                .addUrlEncodeFormBodyParameter(PASSWORD_PROTECTED, String.valueOf(profile.getPasswordProtected()))
                .addUrlEncodeFormBodyParameter(PROFILE_PASSWORD, profile.getProfilePassword())
                .build()
                .getObjectSingle(Integer.class);

    }

    @Override
    public Single<Boolean> updateProfile(Profile profile) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_PROFILE)
                .addHeaders(CONTENT_TYPE, APPLICATION_FORM_URL_ENCODED)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addUrlEncodeFormBodyParameter(PROFILE_ID, String.valueOf(profile.getId()))
                .addUrlEncodeFormBodyParameter(PROFILE_NAME, profile.getName())
                .addUrlEncodeFormBodyParameter(ENABLE_ADULTS, String.valueOf(profile.getEnableAdults()))
                .addUrlEncodeFormBodyParameter(PASSWORD_PROTECTED, String.valueOf(profile.getPasswordProtected()))
                .addUrlEncodeFormBodyParameter(PROFILE_PASSWORD, profile.getProfilePassword())
                .build()
                .getObjectSingle(Boolean.class);

    }

    @Override
    public Single<Boolean> updateAccessCode(String accessCode) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_ACCESS_CODE)
                .addHeaders(CONTENT_TYPE, APPLICATION_FORM_URL_ENCODED)
                .addHeaders(HEADER_AUTHORIZATION , BEARER_HEADER+ preferencesHelper.getAccessToken())
                .addUrlEncodeFormBodyParameter(ACCESS_CODE, accessCode)
                .build()
                .getObjectSingle(Boolean.class);

    }

    @Override
    public Single<LastVersionInformation> getLastVersion() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_LAST_VERSION)
                .addHeaders(CONTENT_TYPE, APPLICATION_FORM_URL_ENCODED)
                .build()
                .getObjectSingle(LastVersionInformation.class);
    }

    @Override
    public void downloadLastVersion(String url, String dirPath, String fileName) {
        Rx2AndroidNetworking.download(url,dirPath,fileName)
                .setTag("Last Version")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {

                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}
