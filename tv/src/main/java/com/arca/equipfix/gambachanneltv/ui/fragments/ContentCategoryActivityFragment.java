package com.arca.equipfix.gambachanneltv.ui.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.app.HeadersFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;
import com.arca.equipfix.gambachanneltv.data.DataManager;
import com.arca.equipfix.gambachanneltv.data.ItemType;
import com.arca.equipfix.gambachanneltv.data.models.PendingCall;
import com.arca.equipfix.gambachanneltv.data.network.model.Actor;
import com.arca.equipfix.gambachanneltv.data.network.model.EPGLine;
import com.arca.equipfix.gambachanneltv.data.network.model.Genre;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemCover;
import com.arca.equipfix.gambachanneltv.ui.activities.BaseActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.LivePlayerActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.MovieDetailsActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.SearchActivity;
import com.arca.equipfix.gambachanneltv.ui.activities.SerieDetailsActivity;
import com.arca.equipfix.gambachanneltv.ui.adapters.ShadowRowPresenterSelector;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardListRow;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardRow;
import com.arca.equipfix.gambachanneltv.ui.presenters.CardPresenterSelector;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ADULTS_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ADULTS_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ADULTS_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_INDEX_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_LIST_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_URL_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_ACTORS;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_FAVORITES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_GENRES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_NEW_RELEASES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_VIEWED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_YEARS;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_KIDS_LIVE_CHANNELS;
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
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_ACTORS;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_FAVORITES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_GENRES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_NEW_RELEASES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_VIEWED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_YEARS;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_BY_LETTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_FAVORITES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_GENRES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_LETTERS;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_NEW_RELEASES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_VIEWED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_SERIES_YEARS;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.IS_GAY_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_COVER_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.KIDS_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.MOVIES_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.MOVIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.MOVIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SEARCH_CODE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SEARCH_STRING_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SERIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SERIES_BY_LETTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SERIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SHOW_ITEM_CODE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SUBMENU_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.TRANSITION_NAME;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContentCategoryActivityFragment extends BrowseFragment {

    public ContentCategoryActivityFragment() {
    }

    private ArrayObjectAdapter mRowsAdapter;
    private int submenuSelected = 0;
    private  String filter = "";
    private ArrayList<PendingCall> pendingCalls;
    List<EPGLine> channels;

    private  boolean makingWebCall = false;
    private  boolean cancel = false;
    private  boolean isGay  = false;
    BackgroundManager backgroundManager;

    public boolean getMakingCal()
    {
        return  makingWebCall;
    }

    public void cancelCalls()
    {
        cancel = true;
    }




    @Inject
    DataManager dataManager;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUi();
    }



    @Override
    public void onDetach() {
        super.onDetach();
        if(pendingCalls != null)
        {
            pendingCalls.clear();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity)getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            submenuSelected = getArguments().getInt(SUBMENU_EXTRA);
            isGay = getArguments().get(IS_GAY_EXTRA) != null;
        }
        else
        {
            if(savedInstanceState != null && savedInstanceState.containsKey(SUBMENU_EXTRA))
            {
                submenuSelected = savedInstanceState.getInt(SUBMENU_EXTRA);
                isGay = savedInstanceState.getBoolean(IS_GAY_EXTRA, false);
            }
        }
        prepareEntranceTransition();
        setupRowAdapter();
    }

    @Override
    public HeadersFragment onCreateHeadersFragment() {
        HeadersFragment result = new CustomHeaderFragment();
        //return super.onCreateHeadersFragment();
        return  result;
    }

    private void setupUi() {
        setBrandColor(getActivity().getResources().getColor(R.color.transparentWhite));
        setSearchAffordanceColor(getActivity().getResources().getColor(R.color.gambaRed));
        setHeadersState(HEADERS_ENABLED);
        backgroundManager = BackgroundManager.getInstance(getActivity());
        backgroundManager.attach(getActivity().getWindow());
        backgroundManager.setDrawable(getActivity().getDrawable(R.drawable.bg_epg));

        setHeadersTransitionOnBackEnabled(true);
        switch (submenuSelected)
        {
            case MOVIES_BY_GENRE:
            case ADULTS_BY_GENRE:
             //   setTitle( getString(R.string.movies_by_genre));
                setBadgeDrawable(getActivity().getDrawable(R.drawable.movies_by_category));
                break;
            case MOVIES_BY_ACTOR:
            case ADULTS_BY_ACTOR:
                setBadgeDrawable(getActivity().getDrawable(R.drawable.movies_by_actor));
                break;
            case MOVIES_BY_YEAR:
            case ADULTS_BY_YEAR:
                setBadgeDrawable(getActivity().getDrawable(R.drawable.movies_by_year));;
                break;
            case SERIES_BY_GENRE:
                setBadgeDrawable(getActivity().getDrawable(R.drawable.series_by_category));
                break;
            case SERIES_BY_YEAR:
                setBadgeDrawable(getActivity().getDrawable(R.drawable.series_by_year));
                break;
            case SERIES_BY_LETTER:
                setBadgeDrawable(getActivity().getDrawable(R.drawable.series_by_abc));
                break;
            case KIDS_BY_GENRE:
                setBadgeDrawable(getActivity().getDrawable(R.drawable.kids_zone));
                break;

        }

        setOnSearchClickedListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivityForResult(intent, SEARCH_CODE );

        });
        setOnItemViewClickedListener((viewHolder, item, viewHolder1, row) -> {
            Intent intent = null;
            if (!(item instanceof Card)) return;
            if (!(viewHolder.view instanceof ImageCardView)) return;
            ImageView imageView = ((ImageCardView) viewHolder.view).getMainImageView();
            switch (submenuSelected) {
                case MOVIES_BY_GENRE:
                case MOVIES_BY_ACTOR:
                case MOVIES_BY_YEAR:
                    intent = MovieDetailsActivity.getStartIntent(getActivity());
                    intent.putExtra(ITEM_TYPE_EXTRA, ItemType.MOVIE.getValue());
                    break;
                case SERIES_BY_GENRE:
                case SERIES_BY_YEAR:
                case SERIES_BY_LETTER:
                    intent = SerieDetailsActivity.getStartIntent(getActivity());
                    intent.putExtra(ITEM_TYPE_EXTRA, ItemType.SERIE.getValue());
                    break;
                case KIDS_BY_GENRE:
                    switch (((Card) item).getSubType())
                    {
                        case GET_KIDS_MOVIES:
                        case GET_KIDS_MOVIES_FAVORITES:
                        case GET_KIDS_MOVIES_NEW_RELEASES:
                        case GET_KIDS_MOVIES_RECENT_ADDED:
                        case GET_KIDS_MOVIES_VIEWED:
                            intent = MovieDetailsActivity.getStartIntent(getActivity());
                            intent.putExtra(ITEM_TYPE_EXTRA, ItemType.MOVIE.getValue());
                            break;
                        case GET_KIDS_SERIES:
                        case GET_KIDS_SERIES_FAVORITES:
                        case GET_KIDS_SERIES_NEW_RELEASES:
                        case GET_KIDS_SERIES_RECENT_ADDED:
                        case GET_KIDS_SERIES_VIEWED:
                            intent = SerieDetailsActivity.getStartIntent(getActivity());
                            intent.putExtra(ITEM_TYPE_EXTRA, ItemType.SERIE.getValue());
                            break;
                        case GET_KIDS_LIVE_CHANNELS:

                            Intent intentLiveChannel = LivePlayerActivity.getStartIntent(getActivity());
                            intentLiveChannel.putParcelableArrayListExtra(CHANNEL_LIST_EXTRA, new ArrayList<>(channels));
                            int selectedRow = 0;
                            for (Card card: ((CardListRow)row).getCardRow().getCards() ) {
                                if(card.getId() != ((Card) item).getId())
                                {
                                    selectedRow++;
                                }
                                else
                                {
                                    break;
                                }
                            }

                            intentLiveChannel.putExtra(CHANNEL_INDEX_EXTRA, selectedRow);

                            intentLiveChannel.putExtra(CHANNEL_URL_EXTRA, ((Card) item).getExtraText()  );
                            startActivityForResult(intentLiveChannel, SHOW_ITEM_CODE);
                            return;

                    }
                    break;
                case ADULTS_BY_ACTOR:
                case ADULTS_BY_GENRE:
                case ADULTS_BY_YEAR:
                    intent = MovieDetailsActivity.getStartIntent(getActivity());
                    intent.putExtra(ITEM_TYPE_EXTRA, ItemType.ADULT_MOVIE.getValue());
                    break;
            }
            intent.putExtra(ITEM_COVER_EXTRA,  (Card) item);
            intent.putExtra(THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA, ViewCompat.getTransitionName(imageView));
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( getActivity(), Pair.create(imageView,    TRANSITION_NAME ));
            startActivityForResult(intent, SHOW_ITEM_CODE, options.toBundle());

        });
        prepareEntranceTransition();
    }




    private void setupRowAdapter() {
        mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());
        setAdapter(mRowsAdapter);

        new Handler().postDelayed(() -> {
            loadRows();
            startEntranceTransition();
        }, 500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SEARCH_CODE )
        {
            if(resultCode == RESULT_OK)
            {
                filter = data.getStringExtra(SEARCH_STRING_EXTRA);
                setupRowAdapter();
            }
        }
        if(backgroundManager == null) {
            backgroundManager = BackgroundManager.getInstance(getActivity());
            backgroundManager.attach(getActivity().getWindow());
        }

        backgroundManager.setDrawable(getActivity().getDrawable(R.drawable.bg_epg));
    }



    private void loadRows() {
        pendingCalls = new ArrayList<>();

        switch (submenuSelected) {
            case MOVIES_BY_GENRE:
            case MOVIES_BY_ACTOR:
            case MOVIES_BY_YEAR:
                pendingCalls.add(new PendingCall(GET_MOVIES_RECENT_ADDED, filter, 0, getActivity().getString(R.string.recently_added)));
                pendingCalls.add(new PendingCall(GET_MOVIES_NEW_RELEASES, filter, 0, getActivity().getString(R.string.new_releases)));
                pendingCalls.add(new PendingCall(GET_MOVIES_VIEWED, filter, 0, getActivity().getString(R.string.viewed)));
                pendingCalls.add(new PendingCall(GET_MOVIES_FAVORITES, filter, 0, getActivity().getString(R.string.favorites)));
                break;
            case SERIES_BY_GENRE:
            case SERIES_BY_YEAR:
            case SERIES_BY_LETTER:
                pendingCalls.add(new PendingCall(GET_SERIES_RECENT_ADDED, filter, 0, getActivity().getString(R.string.recently_added)));
                pendingCalls.add(new PendingCall(GET_SERIES_NEW_RELEASES, filter, 0, getActivity().getString(R.string.new_releases)));
                pendingCalls.add(new PendingCall(GET_SERIES_VIEWED, filter, 0, getActivity().getString(R.string.viewed)));
                pendingCalls.add(new PendingCall(GET_SERIES_FAVORITES, filter, 0, getActivity().getString(R.string.favorites)));
                break;
            case KIDS_BY_GENRE:
                pendingCalls.add(new PendingCall(GET_KIDS_MOVIES_RECENT_ADDED, filter, 0, getActivity().getString(R.string.recently_added)));
                pendingCalls.add(new PendingCall(GET_KIDS_MOVIES_NEW_RELEASES, filter, 0, getActivity().getString(R.string.new_releases)));
                pendingCalls.add(new PendingCall(GET_KIDS_MOVIES_VIEWED, filter, 0, getActivity().getString(R.string.viewed)));
                pendingCalls.add(new PendingCall(GET_KIDS_MOVIES_FAVORITES, filter, 0, getActivity().getString(R.string.favorites)));
                pendingCalls.add(new PendingCall(GET_KIDS_MOVIES, filter, 0, getActivity().getString(R.string.movies)));
                pendingCalls.add(new PendingCall(GET_KIDS_SERIES_RECENT_ADDED, filter, 0, getActivity().getString(R.string.recently_added)));
                pendingCalls.add(new PendingCall(GET_KIDS_SERIES_NEW_RELEASES, filter, 0, getActivity().getString(R.string.new_releases)));
                pendingCalls.add(new PendingCall(GET_KIDS_SERIES_VIEWED, filter, 0, getActivity().getString(R.string.viewed)));
                pendingCalls.add(new PendingCall(GET_KIDS_SERIES_FAVORITES, filter, 0, getActivity().getString(R.string.favorites)));
                pendingCalls.add(new PendingCall(GET_KIDS_SERIES, filter, 0, getActivity().getString(R.string.series)));
                pendingCalls.add(new PendingCall(GET_KIDS_LIVE_CHANNELS, filter, 0, getActivity().getString(R.string.live_channels)));
                break;
            case ADULTS_BY_GENRE:
            case ADULTS_BY_ACTOR:
            case ADULTS_BY_YEAR:
                pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_RECENT_ADDED, filter, 0, getActivity().getString(R.string.recently_added)));
                pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_NEW_RELEASES, filter, 0, getActivity().getString(R.string.new_releases)));
                pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_VIEWED, filter, 0, getActivity().getString(R.string.viewed)));
                pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_FAVORITES, filter, 0, getActivity().getString(R.string.favorites)));
                break;
        }

        switch (submenuSelected){
            case MOVIES_BY_GENRE:
                pendingCalls.add(new PendingCall(GET_MOVIES_GENRES, EMPTY_STRING, 0, EMPTY_STRING));
                break;
            case MOVIES_BY_ACTOR:
                pendingCalls.add(new PendingCall(GET_MOVIES_ACTORS, EMPTY_STRING, 0, EMPTY_STRING));
                break;
            case MOVIES_BY_YEAR:
                pendingCalls.add(new PendingCall(GET_MOVIES_YEARS, EMPTY_STRING, 0, EMPTY_STRING));
                break;
            case SERIES_BY_GENRE:
                pendingCalls.add(new PendingCall(GET_SERIES_GENRES, EMPTY_STRING, 0, EMPTY_STRING));
                break;
            case SERIES_BY_YEAR:
                pendingCalls.add(new PendingCall(GET_SERIES_YEARS, EMPTY_STRING, 0, EMPTY_STRING));
                break;
            case SERIES_BY_LETTER:
                pendingCalls.add(new PendingCall(GET_SERIES_LETTERS, EMPTY_STRING, 0, EMPTY_STRING));
                break;
            case ADULTS_BY_GENRE:
                pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_GENRES, EMPTY_STRING, 0, EMPTY_STRING));
                break;
            case ADULTS_BY_YEAR:
                pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_YEARS, EMPTY_STRING, 0, EMPTY_STRING));
                break;
            case ADULTS_BY_ACTOR:
                pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_ACTORS, EMPTY_STRING, 0, EMPTY_STRING));
                break;
        }

        callPendingCall();

    }

    private void callPendingCall()
    {
        if(cancel)
        {
            return;
        }
        try {
            if (pendingCalls != null && pendingCalls.size() > 0) {
                makingWebCall = true;
                PendingCall currentCall = pendingCalls.remove(0);
                switch (currentCall.getType()) {
                    case GET_MOVIES_NEW_RELEASES:
                    case GET_MOVIES_RECENT_ADDED:
                    case GET_MOVIES_FAVORITES:
                    case GET_MOVIES_VIEWED:
                    case GET_MOVIES_BY_GENRE:
                    case GET_MOVIES_BY_ACTOR:
                    case GET_MOVIES_BY_YEAR:
                    case GET_KIDS_MOVIES:
                    case GET_KIDS_MOVIES_FAVORITES:
                    case GET_KIDS_MOVIES_NEW_RELEASES:
                    case GET_KIDS_MOVIES_RECENT_ADDED:
                    case GET_KIDS_MOVIES_VIEWED:

                        boolean isKids = currentCall.getType() == GET_KIDS_MOVIES || currentCall.getType() == GET_KIDS_MOVIES_FAVORITES || currentCall.getType() == GET_KIDS_MOVIES_NEW_RELEASES
                                         || currentCall.getType() == GET_KIDS_MOVIES_RECENT_ADDED  || currentCall.getType() == GET_KIDS_MOVIES_VIEWED;
                        dataManager.getMovies(currentCall.getType(), currentCall.getStringParameters(), currentCall.getIntegerParameters(), isKids)
                                .onErrorReturn(error->
                                        {
                                            return new ArrayList<>();
                                        }
                                )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(movieItemCovers -> {
                                    makingWebCall = false;
                                    if(movieItemCovers.size()>0)
                                    {
                                        mRowsAdapter.add(createMovieCardRow(currentCall, movieItemCovers));
                                    }
                                    callPendingCall();

                                });
                        break;
                    case GET_MOVIES_GENRES:
                        dataManager.getMoviesGenres().
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(genres -> {
                                    makingWebCall = false;
                                    for (Genre genre: genres) {
                                        pendingCalls.add(new PendingCall(GET_MOVIES_BY_GENRE, filter, genre.getId(), genre.getName()));
                                    }
                                    callPendingCall();
                                });

                        break;
                    case GET_MOVIES_ACTORS:
                        dataManager.getMoviesActors().
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(actors -> {
                                    makingWebCall = false;
                                    for (Actor actor: actors) {
                                        pendingCalls.add(new PendingCall(GET_MOVIES_BY_ACTOR, filter, actor.getId(), actor.getName()));
                                    }
                                    callPendingCall();
                                });

                        break;
                    case GET_MOVIES_YEARS:
                        dataManager.getMoviesYears().
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(years -> {
                                    makingWebCall = false;
                                    for (int year: years) {
                                        pendingCalls.add(new PendingCall(GET_MOVIES_BY_YEAR, filter, year, String.valueOf(year) ));
                                    }
                                    callPendingCall();
                                });

                        break;
                    case GET_SERIES_NEW_RELEASES:
                    case GET_SERIES_RECENT_ADDED:
                    case GET_SERIES_FAVORITES:
                    case GET_SERIES_VIEWED:
                    case GET_SERIES_BY_GENRE:
                    case GET_SERIES_BY_YEAR:
                    case GET_SERIES_BY_LETTER:
                    case GET_KIDS_SERIES_NEW_RELEASES:
                    case GET_KIDS_SERIES_FAVORITES:
                    case GET_KIDS_SERIES_RECENT_ADDED:
                    case GET_KIDS_SERIES_VIEWED:
                    case GET_KIDS_SERIES:
                        boolean isKidsSeries = currentCall.getType() == GET_KIDS_SERIES || currentCall.getType() == GET_KIDS_SERIES_FAVORITES || currentCall.getType() == GET_KIDS_SERIES_NEW_RELEASES
                                || currentCall.getType() == GET_KIDS_SERIES_RECENT_ADDED  || currentCall.getType() == GET_KIDS_SERIES_VIEWED;
                        dataManager.getSeries(currentCall.getType(), currentCall.getStringParameters(), currentCall.getIntegerParameters(), isKidsSeries)
                                .onErrorReturn(error->
                                        {
                                            return new ArrayList<>();
                                        }
                                )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(seriesItemCovers -> {
                                    makingWebCall = false;
                                    if(seriesItemCovers.size()>0)
                                    {
                                        mRowsAdapter.add(createMovieCardRow(currentCall, seriesItemCovers));
                                    }
                                    callPendingCall();

                                });
                        break;
                    case GET_SERIES_GENRES:
                        dataManager.getSeriesGenres().
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(genres -> {
                                    makingWebCall = false;
                                    for (Genre genre: genres) {
                                        pendingCalls.add(new PendingCall(GET_SERIES_BY_GENRE, filter, genre.getId(), genre.getName()));
                                    }
                                    callPendingCall();
                                });

                        break;
                    case GET_SERIES_YEARS:
                        dataManager.getSeriesYears().
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(years -> {
                                    makingWebCall = false;
                                    for (int year: years) {
                                        pendingCalls.add(new PendingCall(GET_SERIES_BY_YEAR, filter, year, String.valueOf(year) ));
                                    }
                                    callPendingCall();
                                });

                        break;
                    case GET_SERIES_LETTERS:
                        dataManager.getSeriesLetters().
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(letters -> {
                                    makingWebCall = false;
                                    for (String letter: letters) {
                                        pendingCalls.add(new PendingCall(GET_SERIES_BY_LETTER, letter, 0, letter ));
                                    }
                                    callPendingCall();
                                });

                        break;
                    case GET_KIDS_LIVE_CHANNELS:
                        dataManager.getKidsChannels().
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(channels -> {
                                    makingWebCall = false;
                                    this.channels = channels;

                                    if(channels.size()>0)
                                    {
                                        mRowsAdapter.add(createChannelsRow(currentCall, channels));
                                    }
                                    callPendingCall();

                                   // callPendingCall();
                                });
                        break;
                    case GET_ADULTS_MOVIES_NEW_RELEASES:
                    case GET_ADULTS_MOVIES_RECENT_ADDED:
                    case GET_ADULTS_MOVIES_FAVORITES:
                    case GET_ADULTS_MOVIES_VIEWED:
                    case GET_ADULTS_MOVIES_BY_GENRE:
                    case GET_ADULTS_MOVIES_BY_ACTOR:
                    case GET_ADULTS_MOVIES_BY_YEAR:
                        dataManager.getAdultMovies(currentCall.getType(), currentCall.getStringParameters(), currentCall.getIntegerParameters(), this.isGay )
                                .onErrorReturn(error->
                                        {
                                            return new ArrayList<>();
                                        }
                                )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(movieItemCovers -> {
                                    makingWebCall = false;
                                    if(movieItemCovers.size()>0)
                                    {
                                        mRowsAdapter.add(createMovieCardRow(currentCall, movieItemCovers));
                                    }
                                    callPendingCall();

                                });
                        break;
                    case GET_ADULTS_MOVIES_GENRES:
                        dataManager.getAdultMoviesGenres(this.isGay).
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(genres -> {
                                    makingWebCall = false;
                                    for (Genre genre: genres) {
                                        pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_BY_GENRE, filter, genre.getId(), genre.getName()));
                                    }
                                    callPendingCall();
                                });

                        break;
                    case GET_ADULTS_MOVIES_ACTORS:
                        dataManager.getAdultMoviesActors(this.isGay).
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(actors -> {
                                    makingWebCall = false;
                                    for (Actor actor: actors) {
                                        pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_BY_ACTOR, filter, actor.getId(), actor.getName()));
                                    }
                                    callPendingCall();
                                });

                        break;
                    case GET_ADULTS_MOVIES_YEARS:
                        dataManager.getAdultMoviesYears(this.isGay).
                                onErrorReturn(error->
                                {
                                    return new ArrayList<>();
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(years -> {
                                    makingWebCall = false;
                                    for (int year: years) {
                                        pendingCalls.add(new PendingCall(GET_ADULTS_MOVIES_BY_YEAR, filter, year, String.valueOf(year) ));
                                    }
                                    callPendingCall();
                                });

                        break;

                }
            }
        }
        catch (Exception ignore)
        {

        }
    }



    private Row createMovieCardRow(PendingCall pendingCall, List<ItemCover> items) {

        PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        CardRow cardRow = new CardRow();
        cardRow.setTitle(pendingCall.getCategoryName());
        List<Card> cards = new ArrayList<>();
        int index = 1;
        for (ItemCover information: items) {
            Card card = new Card();
            card.setType(Card.Type.MOVIE_BASE);
            card.setTitle((index++)+": "+ information.getTitle());
            card.setImageUrl(information.getThumbnail());
            card.setId(information.getId());
            card.setSubType(pendingCall.getType());
            listRowAdapter.add(card);
            cards.add(card);
        }
        cardRow.setCards(cards);
        HeaderItem headerItem = new HeaderItem(pendingCall.getCategoryName());
        headerItem.setDescription(String.valueOf(cards.size())+" "+getString(R.string.items));

        return new CardListRow(headerItem, listRowAdapter, cardRow);
    }

    private Row createChannelsRow(PendingCall pendingCall, List<EPGLine> channels) {

        PresenterSelector presenterSelector = new CardPresenterSelector(getActivity());
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        CardRow cardRow = new CardRow();
        cardRow.setTitle(pendingCall.getCategoryName());
        List<Card> cards = new ArrayList<>();
        int index = 1;
        for (EPGLine channel: channels) {
            Card card = new Card();
            card.setType(Card.Type.MOVIE_BASE);
            card.setTitle((index++)+": "+ channel.getName());
            card.setImageUrl(channel.getThumbnail());
            card.setId(channel.getId());
            card.setSubType(pendingCall.getType());
            card.setExtraText(channel.getUrl());
            listRowAdapter.add(card);
            cards.add(card);
        }
        cardRow.setCards(cards);
        HeaderItem headerItem = new HeaderItem(pendingCall.getCategoryName());
        headerItem.setDescription(String.valueOf(cards.size())+" "+getString(R.string.items));
        return new CardListRow(headerItem, listRowAdapter, cardRow);
    }

    public static class  CustomHeaderFragment extends  HeadersFragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater.cloneInContext(new ContextThemeWrapper(inflater.getContext(), R.style.AppTheme_Leanback_Browse_Header)), container, savedInstanceState);
        }
    }


}
