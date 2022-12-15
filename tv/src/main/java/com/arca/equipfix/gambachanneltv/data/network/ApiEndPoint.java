package com.arca.equipfix.gambachanneltv.data.network;

import com.arca.equipfix.gambachanneltv.BuildConfig;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
class ApiEndPoint {

    static final String BASE_URL = "https://api.gambachannel.com/api/";
    static final String SERIAL_NUMBER_SECRET =  "HyvpKeSl7EX98";
    static final String DEVICE_SECRET = "3915psgGSfZ2dP";


    static  final  String CONTENT_TYPE = "Content-Type";
    static  final  String APPLICATION_JSON = "application/json; charset=utf-8";
    static  final  String APPLICATION_FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    static  final  String PASSWORD_STRING = "password";

    static  final  String SERIAL_NUMBER= "serialNumber";
    static  final  String DEVICE_TYPE= "deviceType";
    static  final  String CODE_VERSION= "codeVersion";
    static  final  String HASH= "hash";
    static  final  String INCLUDE_PROFILES= "includeProfiles";
    static  final  String REGISTRATION_CODE= "registrationCode";
    static  final  String PROFILE_TOKEN= "profileToken";
    static  final  String ACCESS_TYPE = "accessType";
    static  final  String HEADER_AUTHORIZATION = "Authorization";
    static  final  String BEARER_HEADER= "Bearer ";
    static  final  String PROFILE_ID= "id";
    static  final  String PROFILE_NAME= "name";
    static  final  String ENABLE_ADULTS= "enableAdults";
    static  final  String PASSWORD_PROTECTED= "passwordProtected";
    static  final  String PROFILE_PASSWORD= "profilePassword";
    static  final  String ACCESS_CODE= "accessCode";



    //Registration
    static  final  String ENDPOINT_GET_REGISTRATION_STATUS =BASE_URL+"Devices/GetRegistrationNewStatus";
    static  final  String ENDPOINT_POST_ACCESS= BASE_URL+"Devices/Access";

    //Linked Devices
    static  final  String ENDPOINT_GET_LINKED_DEVICE =BASE_URL+"Devices/GetLinkedDeviceInfo";
    static  final  String ENDPOINT_DELETE_LINKED_DEVICE =BASE_URL+"Devices/DeleteLinkedDevice";




    //Movies
    static final String ENDPOINT_GET_LAST_ADDED_MOVIES= BASE_URL + "Movies/LastAdded/{language}/{isKids}";
    static final String ENDPOINT_GET_NEW_RELEASES_MOVIES= BASE_URL + "Movies/NewReleases/{language}/{isKids}";
    static final String ENDPOINT_GET_VIEWED_MOVIES= BASE_URL + "Movies/Viewed/{language}/{isKids}";
    static final String ENDPOINT_GET_FAVORITES_MOVIES= BASE_URL + "Movies/Favorites/{language}/{isKids}";
    static final String ENDPOINT_GET_KIDS_MOVIES = BASE_URL + "Movies/GetMovies/Kids/{language}";

    static final String ENDPOINT_GET_MOVIES_GENRES= BASE_URL + "Categories/GetMoviesCategories/{language}";
    static final String ENDPOINT_GET_MOVIES_ACTORS= BASE_URL + "Actors/GetMoviesActors";
    static final String ENDPOINT_GET_MOVIES_YEARS= BASE_URL + "Movies/GetMoviesYears";

    static final String ENDPOINT_GET_MOVIE_ACTORS=  BASE_URL + "Actors/GetActorsByMovie/Movies/{id}";
    static final String ENDPOINT_GET_MOVIE_DETAILS=  BASE_URL + "Movies/GetMovieDetails/{id}/{language}";
    static final String ENDPOINT_GET_RELATED_MOVIES= BASE_URL + "Movies/GetRelatedMovies/{id}/{language}";
    static final String ENDPOINT_GET_MOVIES_BY_ACTOR= BASE_URL + "Movies/GetMoviesByActor/{id}/{language}";
    static final String ENDPOINT_GET_MOVIES_BY_GENRE= BASE_URL + "Movies/GetMoviesByGenre/{id}/{language}";
    static final String ENDPOINT_GET_MOVIES_BY_YEAR= BASE_URL + "Movies/GetMoviesByYear/{id}/{language}";

    static final String ENDPOINT_GET_MOVIE_URL= BASE_URL + "Movies/GetMovieUrl/{id}/{quality}";
    static final String ENDPOINT_POST_SET_MOVIE_PLAYED= BASE_URL + "Movies/PostMoviePlayed/{id}/{duration}";
    static final String ENDPOINT_POST_SET_LAST_POSITION_MOVIE= BASE_URL + "Movies/PostLastPosition/{id}/{lastPosition}";
    static final String ENDPOINT_POST_ADD_FAVORITE_MOVIE= BASE_URL + "Movies/AddFavorite/{id}";
    static final String ENDPOINT_POST_REMOVE_FAVORITE_MOVIE= BASE_URL + "Movies/RemoveFavorite/{id}";
    static final String ENDPOINT_POST_RATE_MOVIE= BASE_URL + "Movies/RateMovie/{id}/{rating}/{lastPosition}";




    //Series
    static final String ENDPOINT_GET_LAST_ADDED_SERIES= BASE_URL + "TVShows/LastAdded/{language}/{isKids}";
    static final String ENDPOINT_GET_NEW_RELEASES_SERIES= BASE_URL + "TVShows/NewReleases/{language}/{isKids}";
    static final String ENDPOINT_GET_VIEWED_SERIES= BASE_URL + "TVShows/Viewed/{language}/{isKids}";
    static final String ENDPOINT_GET_FAVORITES_SERIES= BASE_URL + "TVShows/Favorites/{language}/{isKids}";
    static final String ENDPOINT_GET_SERIES_GENRES= BASE_URL + "Categories/GetSeriesCategories/{language}";
    static final String ENDPOINT_GET_SERIES_YEARS= BASE_URL + "TVShows/GetSeriesYears";
    static final String ENDPOINT_GET_SERIES_LETTERS= BASE_URL + "TVShows/GetSeriesLetters";
    static final String ENDPOINT_GET_KIDS_SERIES= BASE_URL + "TVShows/GetKidsSeries/{language}";


    static final String ENDPOINT_GET_SERIE_ACTORS=  BASE_URL + "Actors/GetActorsByTVShow/{id}";
    static final String ENDPOINT_GET_SERIE_DETAILS=  BASE_URL + "TVShows/GetSerieDetails/{id}/{language}";
    static final String ENDPOINT_GET_RELATED_SERIES= BASE_URL + "TVShows/GetRelatedSeries/{id}/{language}";
    static final String ENDPOINT_GET_SERIES_BY_GENRE= BASE_URL + "TVShows/GetSeriesByGenre/{id}/{language}";
    static final String ENDPOINT_GET_SERIES_BY_YEAR= BASE_URL + "TVShows/GetSeriesByYear/{id}/{language}";
    static final String ENDPOINT_GET_SERIES_BY_LETTER= BASE_URL + "TVShows/GetSeriesByLetter/{letter}/{language}";

    static final String ENDPOINT_GET_SERIE_SEASONS= BASE_URL + "TVShows/GetSerieSeasons/{id}/{language}";
    static final String ENDPOINT_GET_SEASON_EPISODES= BASE_URL + "TVShows/GetSeasonEpisodes/{id}/{language}";
    static final String ENDPOINT_GET_NEXT_EPISODE_SERIE= BASE_URL + "TVShows/GetNextEpisode/SERIE/{id}/{language}";
    static final String ENDPOINT_GET_NEXT_EPISODE_SEASON= BASE_URL + "TVShows/GetNextEpisode/SEASON/{id}/{language}";
    static final String ENDPOINT_GET_NEXT_EPISODE= BASE_URL + "TVShows/GetNextEpisode/EPISODE/{id}/{language}";
    static final String ENDPOINT_GET_EPISODE_INFORMATION= BASE_URL + "TVShows/GetEpisode/{id}/{language}";

    static final String ENDPOINT_POST_SET_EPISODE_PLAYED= BASE_URL + "TVShows/PostEpisodePlayed/{id}/{duration}";
    static final String ENDPOINT_POST_SET_LAST_POSITION_EPISODE= BASE_URL + "TVShows/PostLastPosition/{id}/{lastPosition}";
    static final String ENDPOINT_POST_ADD_FAVORITE_SERIE= BASE_URL + "TVShows/AddFavorite/{id}";
    static final String ENDPOINT_POST_REMOVE_FAVORITE_SERIE= BASE_URL + "TVShows/RemoveFavorite/{id}";



    //Live Channels
    //static final String ENDPOINT_GET_CHANNEL_LIST= BASE_URL + "EPG/GetEPG/{category}";
    static final String ENDPOINT_GET_CHANNEL_LIST= BASE_URL + "EPG/GetChannelsWithEPG/{category}";
    static final String ENDPOINT_GET_LIVE_GENRES= BASE_URL + "Categories/GetLiveCategories/{language}";
//    static final String ENDPOINT_GET_PROGRAMS_BY_CHANNEL= BASE_URL + "EPG/GetProgramsChannel/{channel}";
    static final String ENDPOINT_ADD_REMINDER= BASE_URL + "EPG/AddReminder/{channelId}/{title}/{unique}";
    static final String ENDPOINT_DELETE_REMINDER= BASE_URL + "EPG/DeleteReminder/{reminderId}";
    static final String ENDPOINT_GET_REMINDERS= BASE_URL + "EPG/GetReminders";
    static final String ENDPOINT_GET_CURRENT_PROGRAM= BASE_URL + "EPG/GetCurrentProgram/{channelId}";
    static final String GET_KIDS_CHANNELS= BASE_URL + "EPG/GetKidsChannels";
    static final String ENDPOINT_GET_LIVE_CHANNEL_URL= BASE_URL + "EPG/GetChannelUrl/{channelId}";




    //Generic
    static final String ENDPOINT_GET_VALIDATE_ACCESS_CODE= BASE_URL + "Devices/ValidateAccessCode/{accessCode}";
    static final String ENDPOINT_GET_PROFILES_DEVICE= BASE_URL + "Devices/GetProfileList";
    static final String ENDPOINT_GET_VALIDATE_PROFILE_PASSWORD= BASE_URL + "Devices/ValidateProfilePassword/{profileId}/{accessCode}";


    //Adults
    static final String ENDPOINT_GET_LAST_ADDED_ADULTS_MOVIES= BASE_URL + "Adults/LastAdded/{language}/{isGay}";
    static final String ENDPOINT_GET_NEW_RELEASES_ADULTS_MOVIES= BASE_URL + "Adults/NewReleases/{language}/{isGay}";
    static final String ENDPOINT_GET_VIEWED_ADULTS_MOVIES= BASE_URL + "Adults/Viewed/{language}/{isGay}";
    static final String ENDPOINT_GET_FAVORITES_ADULTS_MOVIES= BASE_URL + "Adults/Favorites/{language}/{isGay}";

    static final String ENDPOINT_GET_ADULTS_MOVIES_GENRES= BASE_URL + "Categories/GetAdultsMoviesCategories/{language}/{isGay}";
    static final String ENDPOINT_GET_ADULTS_MOVIES_ACTORS= BASE_URL + "Actors/GetAdultMoviesActors/{isGay}";
    static final String ENDPOINT_GET_ADULTS_MOVIES_YEARS= BASE_URL + "Adults/GetMoviesYears/{isGay}";

    static final String ENDPOINT_GET_ADULTS_MOVIE_ACTORS=  BASE_URL + "Actors/GetActorsByMovie/Adults/{id}";
    static final String ENDPOINT_GET_ADULTS_MOVIE_DETAILS=  BASE_URL + "Adults/GetMovieDetails/{id}/{language}";
    static final String ENDPOINT_GET_ADULTS_RELATED_MOVIES= BASE_URL + "Adults/GetRelatedMovies/{id}/{language}";
    static final String ENDPOINT_GET_ADULTS_MOVIES_BY_ACTOR= BASE_URL + "Adults/GetMoviesByActor/{id}/{language}";
    static final String ENDPOINT_GET_ADULTS_MOVIES_BY_GENRE= BASE_URL + "Adults/GetMoviesByGenre/{id}/{language}/{isGay}";
    static final String ENDPOINT_GET_ADULTS_MOVIES_BY_YEAR= BASE_URL + "Adults/GetMoviesByYear/{id}/{language}/{isGay}";

    static final String ENDPOINT_GET_ADULTS_MOVIE_URL= BASE_URL + "Adults/GetMovieUrl/{id}/{quality}";
    static final String ENDPOINT_POST_SET_ADULTS_MOVIE_PLAYED= BASE_URL + "Adults/PostMoviePlayed/{id}/{duration}";
    static final String ENDPOINT_POST_SET_LAST_POSITION_ADULTS_MOVIE= BASE_URL + "Adults/PostLastPosition/{id}/{lastPosition}";
    static final String ENDPOINT_POST_ADD_FAVORITE_ADULTS_MOVIE= BASE_URL + "Adults/AddFavorite/{id}";
    static final String ENDPOINT_POST_REMOVE_FAVORITE_ADULTS_MOVIE= BASE_URL + "Adults/RemoveFavorite/{id}";
    static final String ENDPOINT_POST_RATE_ADULTS_MOVIE= BASE_URL + "Adults/RateMovie/{id}/{rating}/{lastPosition}";

    //Profiles
    static final String ENDPOINT_CREATE_PROFILE= BASE_URL + "Devices/CreateProfile";
    static final String ENDPOINT_UPDATE_PROFILE= BASE_URL + "Devices/UpdateProfile";
    static final String ENDPOINT_UPDATE_ACCESS_CODE= BASE_URL + "Devices/UpdateAccessCode";

    static final String ENDPOINT_GET_LAST_VERSION= BASE_URL + "Info/GetLastVersion";



}

