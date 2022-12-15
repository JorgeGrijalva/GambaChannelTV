package com.arca.equipfix.gambachanneltv.utils;

/**
 * Created by gabri on 6/15/2018.
 */

public class AppConstants {
    private  AppConstants() {    }

    public static final String DB_NAME = "gamba.db";


    public static final int MOVIE_ID = -100;
    public static final int SERIE_ID = -101;
    public static final int KIDS_ID = -102;
    public static final int LIVE_ID = -103;
    public static final int CONTINUOUS_ID = -104;
    public static final int ADULTS_ID = -105;
    public static final int SPORTS_ID = -106;
    public static final int MUSIC_ID = -107;
    public static final int SEASONS_ID = -108;
    public static final int GAYS_ID = -109;
    public static final int SETTINGS_ID = -110;
    public static final int LINK_DEVICE_ID = -111;
    public static final int PROFILES_ID = -112;
    public static final int RADIO_ID = -113;

    public static final  int SUBTYPE_EMPTY= 0;


    public static final int LAST_ADDED_START_LIMIT = 15;
    public static final  String DEFAULT_LANGUAGE = "ES";
    public static final  String DEFAULT_QUALITY = "HD";
    public static final  String ZERO_HOURS = "00:00";
    public static final  String EMPTY_STRING = "";


    public static final  long REGISTRATION_STATUS_CHECK_TIME =  15000; //15 Seconds
    public static final  long REGISTRATION_STATUS_CHECK_TIME_WORKING_ACTIVITIES =  1000 * 60 * 15; //15 minutes
    public static final int MIN_DURATION_BEFORE_MOVE_EPG = 200;
    public static final int HALF_HOUR = 30*60*1000;
    public static final int FIVE_MINUTES = 5*60*1000;
    public static final int TWO_MINUTES = 2*60*1000;
    public static final int TWENTY_MINUTES = 20*60*1000;
    public static final int HALF_DAY = 12*60*60*1000;




    public static final String REGISTRATION_INFORMATION= "registration_information";
    public static final String ITEM_COVER_EXTRA = "itemCoverExtra";
    public static final String ITEM_TYPE_EXTRA = "itemTypeExtra";
    public static final String ITEM_ID_EXTRA = "itemIdExtra";
    public static final String PLAY_CONTINUOUS_EXTRA = "playContinuousExtra";




    public static final int ITEM_TYPE_EXTRA_EPISODE = 0;
    public static final int ITEM_TYPE_EXTRA_SEASON = 1;
    public static final int ITEM_TYPE_EXTRA_SERIE = 2;


    public static final String THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA ="thumbnailTransitionNameExtra";
    public static final  String ITEM_DETAILS_EXTRA = "itemDetailsExtra";
    public static final String TRANSITION_NAME = "t_for_transition";
    public static final String SUBMENU_EXTRA = "submenuExtra";
    public static final String CATEGORIES_EXTRA = "categoriesExtra";

    public static final String SEARCH_STRING_EXTRA = "searchStringExtra";
    public static final String DURATION_EXTRA = "durationExtra";
    public static final String LAST_POSITION_EXTRA = "lastPositionExtra";
    public static final String RATING_EXTRA = "ratingExtra";
    public static final  String CHANNEL_URL_EXTRA = "channelUrlExtra";
    public static final  String CHANNEL_LIST_EXTRA = "channelListExtra";
    public static final  String CHANNEL_INDEX_EXTRA = "channelIndexExtra";
    public static final  String IS_GAY_EXTRA = "isGayExtra";
    public static final  String CURRENT_PROFILE_EXTRA = "currentProfileExtra";


    public static final int SEARCH_CODE = 100;
    public static final int PLAY_CODE = 101;
    public static final int RATE_CODE = 102;
    public static final int SHOW_ITEM_CODE = 103;
    public static final int  UNINSTALL_OLD_VERSION = 104;



    //MAIN SCREEN SUBMENUS
    public static final int MOVIES_BY_GENRE = 1;
    public static final int MOVIES_BY_ACTOR = 2;
    public static final int MOVIES_BY_YEAR = 3;

    public static final int SERIES_BY_GENRE = 5;
    public static final int SERIES_BY_YEAR = 6;
    public static final int SERIES_BY_LETTER = 7;




    public static final int KIDS_BY_GENRE= 8;

    public static final int ADULTS_BY_GENRE = 9;
    public static final int ADULTS_BY_ACTOR = 10;
    public static final int ADULTS_BY_YEAR = 11;

    public static final int RADIO_BY_GENRE = 12;
    public static final int RADIO_BY_COUNTRY = 13;


    //CONTENT CONSTANTS
    public static final int GET_MOVIES_NEW_RELEASES = 1;
    public static final int GET_MOVIES_RECENT_ADDED = 2;
    public static final int GET_MOVIES_VIEWED = 3;
    public static final int GET_MOVIES_FAVORITES = 4;
    public static final int GET_MOVIES_GENRES= 5;
    public static final int GET_MOVIES_BY_GENRE = 6;
    public static final int GET_MOVIES_ACTORS= 7;
    public static final int GET_MOVIES_BY_ACTOR = 8;
    public static final int GET_MOVIES_YEARS= 9;
    public static final int GET_MOVIES_BY_YEAR = 10;

    public static final int GET_SERIES_NEW_RELEASES = 11;
    public static final int GET_SERIES_RECENT_ADDED = 12;
    public static final int GET_SERIES_VIEWED = 13;
    public static final int GET_SERIES_FAVORITES = 14;
    public static final int GET_SERIES_GENRES= 15;
    public static final int GET_SERIES_BY_GENRE = 16;
    public static final int GET_SERIES_YEARS= 17;
    public static final int GET_SERIES_BY_YEAR = 18;
    public static final int GET_SERIES_LETTERS= 19;
    public static final int GET_SERIES_BY_LETTER = 20;


    public static final int GET_KIDS_MOVIES_RECENT_ADDED = 21;
    public static final int GET_KIDS_MOVIES_NEW_RELEASES = 22;
    public static final int GET_KIDS_MOVIES_VIEWED = 23;
    public static final int GET_KIDS_MOVIES_FAVORITES = 24;
    public static final int GET_KIDS_MOVIES = 25;
    public static final int GET_KIDS_SERIES_RECENT_ADDED = 26;
    public static final int GET_KIDS_SERIES_NEW_RELEASES = 27;
    public static final int GET_KIDS_SERIES_VIEWED = 28;
    public static final int GET_KIDS_SERIES_FAVORITES = 29;
    public static final int GET_KIDS_SERIES = 30;
    public static final int GET_KIDS_LIVE_CHANNELS = 31;

    public static final int GET_ADULTS_MOVIES_NEW_RELEASES = 32;
    public static final int GET_ADULTS_MOVIES_RECENT_ADDED = 33;
    public static final int GET_ADULTS_MOVIES_VIEWED = 34;
    public static final int GET_ADULTS_MOVIES_FAVORITES = 35;
    public static final int GET_ADULTS_MOVIES_GENRES= 36;
    public static final int GET_ADULTS_MOVIES_BY_GENRE = 37;
    public static final int GET_ADULTS_MOVIES_ACTORS= 38;
    public static final int GET_ADULTS_MOVIES_BY_ACTOR = 39;
    public static final int GET_ADULTS_MOVIES_YEARS= 40;
    public static final int GET_ADULTS_MOVIES_BY_YEAR = 41;





}
