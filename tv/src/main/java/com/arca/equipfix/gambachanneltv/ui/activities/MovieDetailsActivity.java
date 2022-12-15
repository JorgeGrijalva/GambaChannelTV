package com.arca.equipfix.gambachanneltv.ui.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.arca.equipfix.gambachanneltv.data.ItemDetails;
import com.arca.equipfix.gambachanneltv.data.ItemType;
import com.arca.equipfix.gambachanneltv.data.network.model.Actor;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemCover;
import com.arca.equipfix.gambachanneltv.data.network.model.MovieDetails;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.data.prefs.model.LoginType;
import com.arca.equipfix.gambachanneltv.ui.adapters.ShadowRowPresenterSelector;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardListRow;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardRow;
import com.arca.equipfix.gambachanneltv.ui.local_components.CustomDialog;
import com.arca.equipfix.gambachanneltv.ui.local_components.InputDialog;
import com.arca.equipfix.gambachanneltv.ui.presenters.CardPresenterSelector;
import com.arca.equipfix.gambachanneltv.utils.device_information.AppDeviceInformationHelper;
import com.dm.emotionrating.library.RatingView;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static com.arca.equipfix.gambachanneltv.data.ItemType.DEMO;
import static com.arca.equipfix.gambachanneltv.data.ItemType.MOVIE;
import static com.arca.equipfix.gambachanneltv.data.ItemType.TRAILER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.DURATION_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_ADULTS_MOVIES_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_COVER_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_DETAILS_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.LAST_POSITION_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.PLAY_CODE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.TRANSITION_NAME;


public class MovieDetailsActivity extends BaseActivity  implements
         View.OnClickListener {




    @BindView(R.id.thumbnailImage) ImageView thumbnailImage;
    @BindView(R.id.titleTextView) TextView titleTextView;
    @BindView(R.id.actionsListBox) ListView actionsListBox;
    @BindView(R.id.playButton) MaterialFancyButton playButton;
    @BindView(R.id.trailerButton) MaterialFancyButton trailerButton;
    @BindView(R.id.restartButton) MaterialFancyButton restartButton;
    @BindView(R.id.continueButton) MaterialFancyButton continueButton;
    @BindView(R.id.favoritesButton) MaterialFancyButton favoritesButton;
    @BindView(R.id.cardsRecyclerView)
    VerticalGridView cardsRecyclerView;
    @BindView(R.id.actorsTextView) TextView actorsTextView;
    @BindView(R.id.lengthTextView) TextView lengthTextView;
    @BindView(R.id.categoriesTextView) TextView categoriesTextView;
    @BindView(R.id.yearTextView)TextView yearTextView;
    @BindView(R.id.qualitiesTextView) TextView qualitiesTextView;
    @BindView(R.id.synopsisTextView) TextView synopsisTextView;
    @BindView(R.id.directorTextView) TextView directorTextView;
    @BindView(R.id.ratingTextView) TextView ratingTextView;
    @BindView(R.id.userRatingsTextView) TextView userRatingsTextView;
    @BindView(R.id.ratingView)
    RatingView ratingView;


    @BindView(R.id.movieDetailsLinearLayout)    LinearLayout movieDetailsLayout;
    @BindView(R.id.scrollViewStatus) TextView scrollViewStatus;

    private ArrayObjectAdapter mRowsAdapter;
    int actionSelected = 0;

    final ItemBridgeAdapter mBridgeAdapter = new ItemBridgeAdapter();
    private PresenterSelector mAdapterPresenter;
    private MovieDetails movieDetails;




    Row actorCards;
    Row relatedMoviesCards;

    Card currentMovie;
    int itemType;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, MovieDetailsActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        setUnBinder(ButterKnife.bind(this));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        currentMovie = extras.getParcelable(ITEM_COVER_EXTRA);
        itemType = extras.getInt(ITEM_TYPE_EXTRA);
        mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());
        postponeEnterTransition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA);
            thumbnailImage.setTransitionName(imageTransitionName);

        }

        assert currentMovie != null;
        Picasso.get()
                .load(currentMovie.getImageUrl())
        //        .networkPolicy(NetworkPolicy.OFFLINE)
         //       .noFade()
                .into(thumbnailImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                        startPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception ex) {
                        Picasso.get()
                                .load(currentMovie.getImageUrl())
                                .noFade()
                                .into(thumbnailImage, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                startPostponedEnterTransition();
                                            }

                                            @Override
                                            public void onError(Exception ex) {
                                                startPostponedEnterTransition();
                                            }
                                        });
                    }
                });


        if(this.itemType == MOVIE.getValue()) {
            dataManager.getMovieDetails(currentMovie.getId())
                    .onErrorReturn(error ->
                            new MovieDetails()
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movieDetails -> {
                        showMovieDetails(movieDetails);
                    });
            dataManager.getMovieActors(currentMovie.getId())
                    .onErrorReturn(error->
                            new ArrayList<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(actors -> showActors(actors));
        }
        else
        {
            dataManager.getAdultMovieDetails(currentMovie.getId())
                    .onErrorReturn(error ->
                            new MovieDetails()
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movieDetails -> {
                        showMovieDetails(movieDetails);
                    });
            dataManager.getAdultMovieActors(currentMovie.getId())
                    .onErrorReturn(error->
                            new ArrayList<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(actors -> showActors(actors));
        }




        titleTextView.setText(currentMovie.getTitle());
        actionsListBox.requestFocus();

        playButton.setOnClickListener(this);
        trailerButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        restartButton.setOnClickListener(this);
        favoritesButton.setOnClickListener(this);

    }

    private void showMovieDetails(MovieDetails movieDetails)
    {
        this.movieDetails = movieDetails;
        if(movieDetails.getId()>0)
        {

            movieDetailsLayout.setVisibility(View.VISIBLE);

        }
        lengthTextView.setText(String.format("%d %s", movieDetails.getLength(), getString(R.string.minutes_abr)));
        categoriesTextView.setText(movieDetails.getCategories());
        yearTextView.setText(String.valueOf(movieDetails.getYear()));
        qualitiesTextView.setText(String.format(getString(R.string.available_in), movieDetails.getQualitiesString()));
        synopsisTextView.setText(movieDetails.getSynopsis());
        directorTextView.setText(movieDetails.getDirector());
        if(this.itemType == MOVIE.getValue() ) {
            ratingTextView.setText(movieDetails.getRating());
        }
        else
        {
            ratingTextView.setVisibility(View.GONE);
            trailerButton.setVisibility(View.GONE);
        }
        if(movieDetails.getUsersRating() == 0) {
            userRatingsTextView.setText(R.string.no_rated);
        }
        else
        {
            userRatingsTextView.setText(String.format(Locale.US, "%.2f", movieDetails.getUsersRating()));
        }
        ratingView.setRating( Math.round(movieDetails.getUsersRating()));


        if(movieDetails.getLastPosition()==0)
        {
            restartButton.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        }
        else
        {
            restartButton.setVisibility(View.VISIBLE);
            continueButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
        }

        if(movieDetails.getIsFavorite())
        {
            favoritesButton.setText(getString(R.string.remove_favorite));
        }
        showDuration();
    }

    private void showActors(List<Actor> actors)
    {
        actorCards = createActorCardRow(actors);
        mRowsAdapter.add(actorCards);
        createAndSetWrapperPresenter();

        mBridgeAdapter.setAdapter(mRowsAdapter);
        mBridgeAdapter.setPresenter(mAdapterPresenter);

        if (cardsRecyclerView != null) {
            cardsRecyclerView.setAdapter(mBridgeAdapter);
        }

        List<String> values = new ArrayList<>();
        values.add(getString(R.string.details));
        values.add(getString(R.string.related));
        StringBuilder starsString = new StringBuilder();
        for (Actor actor: actors) {
            values.add(actor.getName());

            starsString.append(starsString.toString().equals("") ? "" : ", ").append(actor.getName());
        }

        actorsTextView.setText(starsString.toString());


        ArrayAdapter<String > adapter = new ArrayAdapter<String>(MovieDetailsActivity.this, android.R.layout.simple_expandable_list_item_1, values){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView)super.getView(position, convertView, parent);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                return textView;
            }
        };

        actionsListBox.setAdapter(adapter);
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
                decideAction(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                            ImageView imageView = ((ImageCardView) ((ShadowOverlayContainer) ((HorizontalGridView) ((ListRowView) ((LinearLayout) cardsRecyclerView.getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getMainImageView();
                            Card card = (Card) ((ShadowOverlayContainer) ((HorizontalGridView) ((ListRowView) ((LinearLayout) cardsRecyclerView.getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild().getTag();
                            if (card != null) {
                                launchDetailsActivity(card, imageView);
                            }
                        }
                        returnValue = true;
                    }
                    break;
                case KEYCODE_DPAD_UP:
                    if(!actionsListBox.hasFocus() || (actionsListBox.hasFocus() && actionSelected == 0))
                    {
                        if(playButton.getVisibility() == View.VISIBLE)
                        {
                            playButton.requestFocus();
                        }
                        else
                        {
                            if(continueButton.getVisibility() == View.VISIBLE)
                            {
                                continueButton.requestFocus();
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
                if (row.isRenderedAsRowView()) {
                    return adapterPresenter.getPresenter(item);
                } else {
                    return invisibleRowPresenter;
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

    private void loadRelatedMovies()
    {
        if( (this.itemType == MOVIE.getValue())  ) {
            dataManager.getRelatedMovies(currentMovie.getId())
                    .onErrorReturn(error ->
                            new ArrayList<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((List<ItemCover> relatedMovies) -> {
                        relatedMoviesCards = createMoviesCardRow(relatedMovies);
                        showRelatedMovies();
                    });
        }
        else
        {
            dataManager.getAdultRelatedMovies(currentMovie.getId())
                    .onErrorReturn(error ->
                            new ArrayList<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((List<ItemCover> relatedMovies) -> {
                        relatedMoviesCards = createMoviesCardRow(relatedMovies);
                        showRelatedMovies();
                    });
        }
    }

    private void showRelatedMovies()
    {
        mRowsAdapter.clear();

        cardsRecyclerView.setVisibility(View.GONE);
        if(relatedMoviesCards == null)
        {
            loadRelatedMovies();
            return;
        }

        mRowsAdapter.add(relatedMoviesCards);
        createAndSetWrapperPresenter();
        cardsRecyclerView.setVisibility(View.VISIBLE);
        scrollViewStatus.setVisibility(View.GONE);
    }

    private void showActorMovies(int actorId)
    {
        if(itemType == MOVIE.getValue() ) {
            dataManager.getMovies(GET_MOVIES_BY_ACTOR, EMPTY_STRING, actorId, false)
                    .onErrorReturn(error ->
                            new ArrayList<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(itemCovers -> {
                        mRowsAdapter.clear();
                        mRowsAdapter.add(createMoviesCardRow(itemCovers));
                        scrollViewStatus.setVisibility(View.GONE);
                        cardsRecyclerView.setVisibility(View.VISIBLE);
                    });
        }
        else
        {
            dataManager.getAdultMovies(GET_ADULTS_MOVIES_BY_ACTOR, EMPTY_STRING, actorId, false)
                    .onErrorReturn(error ->
                            new ArrayList<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(itemCovers -> {
                        mRowsAdapter.clear();
                        mRowsAdapter.add(createMoviesCardRow(itemCovers));
                        scrollViewStatus.setVisibility(View.GONE);
                        cardsRecyclerView.setVisibility(View.VISIBLE);
                    });
        }
    }

    private void decideAction(int position)
    {
        actionSelected = position;
        if(position>0)
        {
            scrollViewStatus.setVisibility(View.VISIBLE);
            scrollViewStatus.setText(R.string.loading_data);

            movieDetailsLayout.setVisibility(View.GONE);
        }
        else
        {
            movieDetailsLayout.setVisibility(View.VISIBLE);
        }
        switch (position)
        {
            case 0: // Details
                showActors();
                break;
            case 1: // Related
                showRelatedMovies();
                break;
            default: // Actors
                Card card = ((CardListRow)actorCards).getCardRow().getCards().get(position-2);
                showActorMovies(card.getId());
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PLAY_CODE) {
                if (data.hasExtra(LAST_POSITION_EXTRA)) {
                    movieDetails.setLastPosition(data.getLongExtra(LAST_POSITION_EXTRA, 0));
                    if (movieDetails.getLastPosition() > 0) {
                        playButton.setVisibility(View.GONE);
                        restartButton.setVisibility(View.VISIBLE);
                        continueButton.setVisibility(View.VISIBLE);
                    } else {
                        playButton.setVisibility(View.VISIBLE);
                        restartButton.setVisibility(View.GONE);
                        continueButton.setVisibility(View.GONE);
                    }

                }

                if (data.hasExtra(DURATION_EXTRA)) {
                    if (movieDetails.getLength() == 0) {
                        movieDetails.setLength(data.getLongExtra(DURATION_EXTRA, 0));
                    }
                    showDuration();
                }
            }
        }
        catch (Exception ex)
        {

        }

    }

    private void showDuration()
    {
        long minutes = movieDetails.getLength()/1000/60;
        long hours = minutes/60;
        minutes = minutes  % 60;
        String durationString = String.format(Locale.US,getString(R.string.duration_format), hours, minutes );
        lengthTextView.setText(durationString);
    }

    private void playCurrentMovie(boolean shouldContinue)
    {
        Intent intentPlay = PlayerActivity.getStartIntent(this);
        boolean isDemo = dataManager.getLoginType() != LoginType.CLIENT;
        ItemDetails movie = new ItemDetails(currentMovie.getId(), isDemo ? DEMO : (ItemType.values()[itemType]) , shouldContinue ? movieDetails.getLastPosition() :  0, currentMovie.getTitle(), EMPTY_STRING, isDemo ? -1 : movieDetails.getLength());
        intentPlay.putExtra(ITEM_DETAILS_EXTRA, movie);
        intentPlay.putExtra(ITEM_TYPE_EXTRA, this.itemType);
        startActivityForResult(intentPlay, PLAY_CODE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.playButton:
            case R.id.restartButton:
            case R.id.continueButton:
                if(movieDetails.getRestrictedContent())
                {
                    final InputDialog codeDialog = new InputDialog(this, true, getString(R.string.restricted_content), "", getString(R.string.send_code), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    codeDialog.setOnLeftButtonClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dataManager.validateAccessCode(codeDialog.getInput())
                                    .onErrorReturn(error->
                                    {
                                        return false;
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Boolean>() {
                                        @Override
                                        public void accept(Boolean valid) throws Exception {
                                            if(valid)
                                            {
                                                playCurrentMovie(view.getId() == R.id.continueButton);
                                            }
                                            else
                                            {
                                                new CustomDialog(MovieDetailsActivity.this, true, getString(R.string.invalid_access_code_title), getString(R.string.invalid_access_code), getString(R.string.ok), EMPTY_STRING, true );
                                            }
                                        }
                                    });
                        }
                    });
                }
                else {
                    playCurrentMovie(view.getId() == R.id.continueButton);
                }
                break;
            case R.id.trailerButton:
                Intent intentTrailer = PlayerActivity.getStartIntent(this);
                ItemDetails trailer = new ItemDetails(currentMovie.getId(), TRAILER, 0, currentMovie.getTitle(), EMPTY_STRING, -1 );
                intentTrailer.putExtra(ITEM_DETAILS_EXTRA, trailer);
                startActivity(intentTrailer);
                break;
            case R.id.favoritesButton:
                if(movieDetails.getIsFavorite()) {
                    if(this.itemType == MOVIE.getValue() ) {
                        dataManager.removeFavoriteMovie(currentMovie.getId());
                    }
                    else
                    {
                        dataManager.removeAdultFavoriteMovie(currentMovie.getId());
                    }
                    movieDetails.setIsFavorite(false);
                    favoritesButton.setText(getString(R.string.add_favorite));
                }
                else
                {
                    if(this.itemType == MOVIE.getValue()) {
                        dataManager.addFavoriteMovie(currentMovie.getId());
                    }
                    else
                    {
                        dataManager.addAdultFavoriteMovie(currentMovie.getId());
                    }
                    movieDetails.setIsFavorite(true);
                    favoritesButton.setText(getString(R.string.remove_favorite));
                }
                break;
        }

    }

    private void launchDetailsActivity(Card selectedMovie, ImageView thumbnailImageView)
    {
        try {
            Intent intent = MovieDetailsActivity.getStartIntent(this);
            intent.putExtra(ITEM_COVER_EXTRA,  selectedMovie);
            intent.putExtra(THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA, ViewCompat.getTransitionName(thumbnailImageView));
            intent.putExtra(ITEM_TYPE_EXTRA, this.itemType);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    MovieDetailsActivity.this,
                    Pair.create(thumbnailImageView,    TRANSITION_NAME )
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
