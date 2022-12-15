package com.arca.equipfix.gambachanneltv.ui.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.InvisibleRowPresenter;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRowView;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.ShadowOverlayContainer;
import androidx.leanback.widget.VerticalGridView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;
import com.arca.equipfix.gambachanneltv.data.network.model.Actor;
import com.arca.equipfix.gambachanneltv.data.network.model.Episode;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemCover;
import com.arca.equipfix.gambachanneltv.data.network.model.Season;
import com.arca.equipfix.gambachanneltv.data.network.model.SerieDetails;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.ui.adapters.SeasonAdapter;
import com.arca.equipfix.gambachanneltv.ui.adapters.ShadowRowPresenterSelector;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardListRow;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardRow;
import com.arca.equipfix.gambachanneltv.ui.local_components.CustomDialog;
import com.arca.equipfix.gambachanneltv.ui.local_components.InputDialog;
import com.arca.equipfix.gambachanneltv.ui.local_components.TextCardView;
import com.arca.equipfix.gambachanneltv.ui.presenters.CardPresenterSelector;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_COVER_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_ID_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA_EPISODE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA_SEASON;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA_SERIE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.PLAY_CODE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.PLAY_CONTINUOUS_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.TRANSITION_NAME;

public class SerieDetailsActivity extends BaseActivity  implements
        View.OnClickListener {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SerieDetailsActivity.class);
    }

    @BindView(R.id.thumbnailImage) ImageView thumbnailImage;
    @BindView(R.id.titleTextView) TextView titleTextView;
    @BindView(R.id.actionsListBox) ListView actionsListBox;
    @BindView(R.id.playButton) MaterialFancyButton playButton;
    @BindView(R.id.continueButton) MaterialFancyButton continueButton;
    @BindView(R.id.favoritesButton) MaterialFancyButton favoritesButton;
    @BindView(R.id.cardsRecyclerView)
    VerticalGridView cardsRecyclerView;
    @BindView(R.id.actorsTextView) TextView actorsTextView;
    @BindView(R.id.episodeCountTextView) TextView episodeCountTextView;
    @BindView(R.id.categoriesTextView) TextView categoriesTextView;
    @BindView(R.id.seasonsTextView)TextView seasonsTextView;
    @BindView(R.id.synopsisTextView) TextView synopsisTextView;
    @BindView(R.id.ratingTextView) TextView ratingTextView;
    @BindView(R.id.serieDetailsLayout)    LinearLayout serieDetailsLayout;
    @BindView(R.id.scrollViewStatus) TextView scrollViewStatus;

    private ArrayObjectAdapter mRowsAdapter;

    final ItemBridgeAdapter mBridgeAdapter = new ItemBridgeAdapter();
    private PresenterSelector mAdapterPresenter;
    private SerieDetails serieDetails;

    Row actorCards;
    Row relatedSeriesCards;
    Row episodesCards;
    List<Season> seasons;

    Card currentSerie;
    int itemType;
    int lastPosition;
    SeasonAdapter seasonsAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_details);

        setUnBinder(ButterKnife.bind(this));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;

        currentSerie = extras.getParcelable(ITEM_COVER_EXTRA);
        itemType = extras.getInt(ITEM_TYPE_EXTRA);
        mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());
        postponeEnterTransition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA);
            thumbnailImage.setTransitionName(imageTransitionName);

        }

        assert currentSerie != null;
        Picasso.get()
                .load(currentSerie.getImageUrl())
                .into(thumbnailImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        startPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception ex) {
                        startPostponedEnterTransition();
                    }
                });


        dataManager.getSerieDetails(currentSerie.getId())
                .onErrorReturn(error->
                        new SerieDetails()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(serieDetails -> {
                    this.serieDetails = serieDetails;
                    if(serieDetails.getId()>0)
                    {

                        serieDetailsLayout.setVisibility(View.VISIBLE);

                    }
                    episodeCountTextView.setText(String.format(Locale.US,getString(R.string.episode_count), serieDetails.getEpisodeCount()));
                    categoriesTextView.setText(serieDetails.getCategories());
                    seasonsTextView.setText(String.format(Locale.US,getString(R.string.season_count), serieDetails.getSeasonCount()));
                    synopsisTextView.setText(serieDetails.getSynopsis());
                    ratingTextView.setText(serieDetails.getRating());

                    if(serieDetails.getLastEpisode()==0)
                    {
                        continueButton.setVisibility(View.GONE);
                        playButton.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        continueButton.setVisibility(View.VISIBLE);
                        playButton.setVisibility(View.GONE);
                    }

                    if(serieDetails.getIsFavorite())
                    {
                        favoritesButton.setText(getString(R.string.remove_favorite));
                    }
                });

        dataManager.getSerieActors(currentSerie.getId())
                .onErrorReturn(error->
                        new ArrayList<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Actor>>() {
                    @Override
                    public void accept(List<Actor> actors) throws Exception {


                        actorCards = createActorCardRow(actors);
                        mRowsAdapter.add(actorCards);
                        createAndSetWrapperPresenter();

                        mBridgeAdapter.setAdapter(mRowsAdapter);
                        mBridgeAdapter.setPresenter(mAdapterPresenter);

                        if (cardsRecyclerView != null) {
                            cardsRecyclerView.setAdapter(mBridgeAdapter);
                        }

                        StringBuilder starsString = new StringBuilder();
                        for (Actor actor: actors) {
                            starsString.append(starsString.toString().equals("") ? "" : ", ").append(actor.getName());
                        }
                        actorsTextView.setText(starsString.toString());
                    }
                });

        dataManager.getSerieSeasons(currentSerie.getId())
                .onErrorReturn(error->
                        new ArrayList<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(seasons -> {
                    SerieDetailsActivity.this.seasons = seasons;
                    boolean started = false;
                    int lastEpisode = 0;
                    for (Season season: seasons) {
                        if(season.getLastEpisode()>0)
                        {
                            started = true;
                            lastEpisode = season.getLastEpisode();
                        }
                    }


                    seasons.add(0, new Season(-2, getString(R.string.details), started ? lastEpisode: 0 ));
                    seasons.add(1, new Season(-1, getString(R.string.related), 0));

                    seasonsAdapter = new SeasonAdapter( new ArrayList<>(seasons), SerieDetailsActivity.this);
                    actionsListBox.setAdapter(seasonsAdapter);
                    actionsListBox.requestFocus();

                    actionsListBox.setOnFocusChangeListener((view, focused) -> {
                        if(focused)
                        {
                            actionsListBox.setSelector(R.color.selectedItem);
                        }
                        else
                        {
                            actionsListBox.setSelector(R.color.transparent);
                        }
                    });


                    actionsListBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(seasonsAdapter != null) {
                                seasonsAdapter.setCurrentSelected(i);
                                decideAction(i);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                });

        titleTextView.setText(currentSerie.getTitle());
        actionsListBox.requestFocus();

        playButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        favoritesButton.setOnClickListener(this);


    }




    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        int action = event.getAction();
        int keyCode =  event.getKeyCode();
        boolean returnValue = false;
        if(action == ACTION_DOWN)
        {
            switch (keyCode)
            {
                case KEYCODE_DPAD_CENTER:
                case KEYCODE_ENTER:
                    if(cardsRecyclerView.hasFocus()) {
                        if (mRowsAdapter != null) {
                            View focusedChild= ((ShadowOverlayContainer) ((HorizontalGridView) ((ListRowView) ((LinearLayout) cardsRecyclerView.getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild();
                            if(focusedChild instanceof ImageCardView) {

                                ImageView imageView = ((ImageCardView)focusedChild).getMainImageView();
                                Card card = (Card) ((ShadowOverlayContainer) ((HorizontalGridView) ((ListRowView) ((LinearLayout) cardsRecyclerView.getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild().getTag();
                                if (card != null) {
                                    launchDetailsActivity(card, imageView);
                                }
                            }
                            else
                            {
                                Card card =((TextCardView)focusedChild).getCard();
                                if(card != null)
                                {
                                    CustomDialog playContinuousDialog = new CustomDialog(SerieDetailsActivity.this, true, getString(R.string.episode_selected), getString(R.string.enable_continuous_play), getString(R.string.yes), getString(R.string.no), true );
                                    playContinuousDialog.setOnLeftButtonClick(view -> passRestrictedContent(ITEM_TYPE_EXTRA_EPISODE, card.getId(), true));
                                    playContinuousDialog.setOnRightButtonClick(view -> passRestrictedContent(ITEM_TYPE_EXTRA_EPISODE, card.getId(), false));


                                }
                            }
                        }
                        returnValue = true;
                    }
                    else
                    {
                        if(actionsListBox.hasFocus())
                        {
                            if(seasons!= null && actionsListBox.getSelectedItemPosition() < seasons.size()) {
                                Season season = seasonsAdapter.getItem(actionsListBox.getSelectedItemPosition());
                                if(season.getId() == -2) {
                                    passRestrictedContent(ITEM_TYPE_EXTRA_SERIE, currentSerie.getId(), true);
                                }
                                else
                                {
                                    if(season.getId()>0)
                                    {
                                        passRestrictedContent(ITEM_TYPE_EXTRA_SEASON, season.getId(), true);
                                    }
                                }

                            }

                        }
                    }
                    break;
            }
            if(returnValue) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }



    private Row createMoviesCardRow(List<ItemCover> items) {
        // Build main row using the ImageCardViewPresenter.
        PresenterSelector presenterSelector = new CardPresenterSelector(this);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        CardRow cardRow = new CardRow();
        List<Card> cards = new ArrayList<>();
        for (ItemCover item: items) {
            Card card = new Card();
            card.setType(Card.Type.MOVIE_BASE);
            card.setTitle(item.getTitle());
            card.setImageUrl(item.getThumbnail());
            card.setId(item.getId());
            listRowAdapter.add(card);
            cards.add(card);
        }
        cardRow.setCards(cards);
        HeaderItem headerItem = new HeaderItem(EMPTY_STRING);
        return new CardListRow(headerItem, listRowAdapter, cardRow);
    }

    private Row createEpisodesCardRow(List<Episode> items) {
        PresenterSelector presenterSelector = new CardPresenterSelector(this);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        CardRow cardRow = new CardRow();
        List<Card> cards = new ArrayList<>();
        for (Episode item: items) {
            Card card = new Card();
            card.setType(Card.Type.EPISODE);
            card.setTitle(item.getTitle());

            long minutes = item.getLength()/1000/60;
            String durationString = String.format(Locale.US, getString(R.string.episode_duration_format), minutes );
            card.setExtraText( durationString);
            card.setExtraText2( String.format(Locale.US, getString(R.string.episode_director ), item.getDirector()));
            card.setExtraText3( String.format(Locale.US, getString(R.string.episode_qualities ), item.getQualitiesString()));
            if(item.getLastPosition()>0)
            {
                card.setViewed(true);
            }
            card.setId(item.getId());
            listRowAdapter.add(card);
            cards.add(card);
        }
        cardRow.setCards(cards);
        HeaderItem headerItem = new HeaderItem(EMPTY_STRING);
        return new CardListRow(headerItem, listRowAdapter, cardRow);
    }


    private Row createActorCardRow(List<Actor> items) {
        // Build main row using the ImageCardViewPresenter.
        PresenterSelector presenterSelector = new CardPresenterSelector(this);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        CardRow cardRow = new CardRow();
        cardRow.setTitle(getString(R.string.stars));
        List<Card> cards = new ArrayList<>();
        for (Actor item: items) {
            Card card = new Card();
            card.setType(Card.Type.MOVIE_BASE);
            card.setTitle(item.getName());
            card.setImageUrl(item.getPhoto());
            card.setId(item.getId());
            listRowAdapter.add(card);
            cards.add(card);
        }
        cardRow.setCards(cards);
        HeaderItem headerItem = new HeaderItem(EMPTY_STRING);
        return new CardListRow(headerItem, listRowAdapter, cardRow);
    }

    private void createAndSetWrapperPresenter() {
        final PresenterSelector adapterPresenter = mRowsAdapter.getPresenterSelector();
        if (adapterPresenter == null) {
            throw new IllegalArgumentException("Adapter.getPresenterSelector() is null");
        }
        if (adapterPresenter == mAdapterPresenter) {
            return;
        }
        mAdapterPresenter = adapterPresenter;

        Presenter[] presenters = adapterPresenter.getPresenters();
        @SuppressLint("RestrictedApi") final Presenter invisibleRowPresenter = new InvisibleRowPresenter();
        final Presenter[] allPresenters = new Presenter[presenters.length + 1];
        System.arraycopy(allPresenters, 0, presenters, 0, presenters.length);
        allPresenters[allPresenters.length - 1] = invisibleRowPresenter;
        mRowsAdapter.setPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                Row row = (Row) item;
                if(row !=null) {
                    if (row.isRenderedAsRowView()) {
                        return adapterPresenter.getPresenter(item);
                    } else {
                        return invisibleRowPresenter;
                    }
                }
                else
                {
                    if(row== null)
                    {

                    }
                    return  invisibleRowPresenter;
                }
            }

            @Override
            public Presenter[] getPresenters() {
                return allPresenters;
            }
        });
    }


    private  void showActors()
    {

        mRowsAdapter.clear();
        mRowsAdapter.add(actorCards);
        createAndSetWrapperPresenter();

        scrollViewStatus.setVisibility(View.GONE);
    }

    private void loadRelatedSeries()
    {
        dataManager.getRelatedSeries(currentSerie.getId())
                .onErrorReturn(error->
                        new ArrayList<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<ItemCover> relatedSeries) -> {
                    relatedSeriesCards = createMoviesCardRow(relatedSeries);
                    showRelatedSeries();
                });
    }

    private void showRelatedSeries()
    {
        mRowsAdapter.clear();

        cardsRecyclerView.setVisibility(View.GONE);
        if(relatedSeriesCards == null)
        {
            loadRelatedSeries();
            return;
        }

        mRowsAdapter.add(relatedSeriesCards);
        createAndSetWrapperPresenter();
        cardsRecyclerView.setVisibility(View.VISIBLE);
        scrollViewStatus.setVisibility(View.GONE);
    }

    private void loadSeasonEpisodes(Season season)
    {
        if(season.getEpisodes() == null || season.getEpisodes().size()==0) {
            dataManager.getSeasonEpisodes(season.getId())
                    .onErrorReturn(error ->
                    {
                        return new ArrayList<>();
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(episodes -> {
                        season.setEpisodes(episodes);
                        episodesCards = createEpisodesCardRow(episodes);
                        showSeasonEpisodes(season);
                    });
        }
        else
        {
            episodesCards = createEpisodesCardRow(season.getEpisodes());
            showSeasonEpisodes(season);
        }
    }

    private void showSeasonEpisodes(Season season)
    {
        mRowsAdapter.clear();

        cardsRecyclerView.setVisibility(View.GONE);
        if(episodesCards == null)
        {
            loadSeasonEpisodes(season);
            return;
        }

        mRowsAdapter.add(episodesCards);
        createAndSetWrapperPresenter();
        cardsRecyclerView.setVisibility(View.VISIBLE);
        scrollViewStatus.setVisibility(View.GONE);
    }

    private void decideAction(int position)
    {
        lastPosition = position;
        if(position>0)
        {
            scrollViewStatus.setVisibility(View.VISIBLE);
            scrollViewStatus.setText(R.string.loading_data);

            serieDetailsLayout.setVisibility(View.GONE);
        }
        else
        {
            serieDetailsLayout.setVisibility(View.VISIBLE);
        }
        switch (position)
        {
            case 0: // Details
                showActors();
                break;
            case 1: // Related
                showRelatedSeries();
                break;
            default: // Seasons
                Season season = seasons.get(position);
                loadSeasonEpisodes(season);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PLAY_CODE) {

            }
        }
        catch (Exception ignored)
        {

        }

    }

    private void playEpisodes(int itemType, int itemId, boolean continuousPlay)
    {
        Intent intent = EpisodesPlayerActivity.getStartIntent(this);
        intent.putExtra(ITEM_TYPE_EXTRA, itemType);
        intent.putExtra(ITEM_ID_EXTRA, itemId);
        intent.putExtra(PLAY_CONTINUOUS_EXTRA, continuousPlay);
        startActivityForResult(intent, PLAY_CODE);

    }

    private  void passRestrictedContent(int itemType, int itemId, boolean continuosPlay)
    {
        if(!serieDetails.getRestrictedContent())
        {
            playEpisodes(itemType, itemId, continuosPlay);
            return;
        }

        final InputDialog codeDialog = new InputDialog(this, true, getString(R.string.restricted_content), "", getString(R.string.send_code), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        codeDialog.setOnLeftButtonClick(view1 -> dataManager.validateAccessCode(codeDialog.getInput())
                .onErrorReturn(error->
                {
                    return false;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valid -> {
                    if(valid)
                    {
                        playEpisodes(itemType, itemId, continuosPlay);
                    }
                    else
                    {
                        new CustomDialog(SerieDetailsActivity.this, true, getString(R.string.invalid_access_code_title), getString(R.string.invalid_access_code), getString(R.string.ok), EMPTY_STRING, true );
                    }
                }));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.playButton:
            case R.id.continueButton:
                    passRestrictedContent(ITEM_TYPE_EXTRA_SERIE, currentSerie.getId(), true);
                break;
            case R.id.favoritesButton:
                if(serieDetails.getIsFavorite()) {
                    dataManager.removeFavoriteSerie(currentSerie.getId());
                    serieDetails.setIsFavorite(false);
                    favoritesButton.setText(getString(R.string.add_favorite));
                }
                else
                {
                    dataManager.addFavoriteSerie(currentSerie.getId());
                    serieDetails.setIsFavorite(true);
                    favoritesButton.setText(getString(R.string.remove_favorite));
                }
                break;
        }

    }

    private void launchDetailsActivity(Card selectedMovie, ImageView thumbnailImageView)
    {
        try {
            Intent intent = SerieDetailsActivity.getStartIntent(this);
            intent.putExtra(ITEM_COVER_EXTRA,  selectedMovie);
            intent.putExtra(THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA, ViewCompat.getTransitionName(thumbnailImageView));

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    SerieDetailsActivity.this, Pair.create(thumbnailImageView,    TRANSITION_NAME )
            );

            startActivity(intent, options.toBundle());


        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }





    @Override
    public void onRegistration() {

    }

    @Override
    public void onSessionStarted(SessionInformation sessionInformation) {

    }

    @Override
    public void onSessionError() {

    }


}
