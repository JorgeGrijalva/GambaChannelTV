package com.arca.equipfix.gambachanneltv.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.arca.equipfix.gambachanneltv.data.network.model.Genre;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemCover;
import com.arca.equipfix.gambachanneltv.ui.adapters.ShadowRowPresenterSelector;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardListRow;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardRow;
import com.arca.equipfix.gambachanneltv.ui.local_components.CustomDialog;
import com.arca.equipfix.gambachanneltv.ui.local_components.InputDialog;
import com.arca.equipfix.gambachanneltv.ui.presenters.CardPresenterSelector;
import com.arca.equipfix.gambachanneltv.ui.presenters.NoShadowRowPresenterSelector;

import java.text.SimpleDateFormat;
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
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ADULTS_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ADULTS_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ADULTS_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ADULTS_ID;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CATEGORIES_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CURRENT_PROFILE_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_COVER_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.KIDS_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.KIDS_ID;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.LIVE_ID;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.MOVIES_BY_ACTOR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.MOVIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.MOVIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.MOVIE_ID;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.PROFILES_ID;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.RADIO_BY_COUNTRY;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.RADIO_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.RADIO_ID;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SERIES_BY_GENRE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SERIES_BY_LETTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SERIES_BY_YEAR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SERIE_ID;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SETTINGS_ID;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SUBMENU_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.TRANSITION_NAME;

public class MainMenuActivity extends AuthenticateBaseActivity implements View.OnClickListener {

    private ArrayObjectAdapter recentMoviesAdapter;
    private ArrayObjectAdapter menuAdapter;
    private ArrayObjectAdapter submenuAdapter;

    final ItemBridgeAdapter recentMoviesBridgeAdapter = new ItemBridgeAdapter();
    final ItemBridgeAdapter menuBridgeAdapter = new ItemBridgeAdapter();
    final ItemBridgeAdapter submenuBridgeAdapter = new ItemBridgeAdapter();

    private PresenterSelector recentMoviesAdapterPresenter;
    private PresenterSelector menuAdapterPresenter;
    private PresenterSelector submenuAdapterPresenter;

    private Card selectedMenu;




    @BindView(R.id.recentContentScrollView) VerticalGridView recentContentScrollView;

    @BindView(R.id.expirationDate) TextView expirationDateTextView;
    @BindView(R.id.serialNumber) TextView serialNumberTextView;
    @BindView(R.id.subMenuItems) VerticalGridView subMenuItems;
    @BindView(R.id.menuItems) VerticalGridView menuItems;
    @BindView(R.id.linkingCodeTextView) TextView linkingCodeTextView;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }

    private void createAndSetWrapperPresenter(ArrayObjectAdapter rowsAdapter, PresenterSelector currentSelector) {
        final PresenterSelector adapterPresenter = rowsAdapter.getPresenterSelector();
        if (adapterPresenter == null) {
            throw new IllegalArgumentException("Adapter.getPresenterSelector() is null");
        }
        if (adapterPresenter == currentSelector) {
            return;
        }
        currentSelector = adapterPresenter;

        Presenter[] presenters = adapterPresenter.getPresenters();
        @SuppressLint("RestrictedApi") final Presenter invisibleRowPresenter = new InvisibleRowPresenter();
        final Presenter[] allPresenters = new Presenter[presenters.length + 1];
        System.arraycopy(allPresenters, 0, presenters, 0, presenters.length);
        allPresenters[allPresenters.length - 1] = invisibleRowPresenter;
        rowsAdapter.setPresenterSelector(new PresenterSelector() {
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

    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setUnBinder(ButterKnife.bind(this));
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null && extras.get(CURRENT_PROFILE_EXTRA) != null)
        {
            currentProfile = extras.getParcelable(CURRENT_PROFILE_EXTRA);
        }

        initUI();
        menuItems.requestFocus();

        haveStoragePermission();


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
                case KEYCODE_DPAD_CENTER :
                case KEYCODE_ENTER :
                    returnValue = true;
                    if(menuItems.hasFocus())
                    {
                        if(menuAdapter != null) {
                            CardListRow row = (CardListRow) menuAdapter.get(0);

                            for (Card menu: row.getCardRow().getCards()) {
                                if(menu.isSelected())
                                {
                                    this.selectedMenu = menu;
                                    switch (menu.getId())
                                    {
                                        case SETTINGS_ID:
                                            Intent intentSettings = SettingsActivity.getStartIntent(this);
                                            startActivity(intentSettings);
                                            break;
                                        case PROFILES_ID:
                                            Intent profilesIntent = ProfilesActivity.getStartIntent(this);
                                            startActivity(profilesIntent);
                                            break;
                                        case KIDS_ID:
                                            Intent intentKids = ContentCategoryActivity.getStartIntent(this);
                                            intentKids.putExtra(SUBMENU_EXTRA,  KIDS_BY_GENRE);
                                            startActivity(intentKids );
                                            break;
                                        default:
                                            showSubmenu(menu);
                                    }

                                    break;
                                }
                            }

                        }
                    }
                    else
                    {
                        if(recentContentScrollView.hasFocus()) {
                            if (recentMoviesAdapter != null) {
                                ImageView imageView = ((ImageCardView) ((ShadowOverlayContainer) ((HorizontalGridView) ((ListRowView) ((LinearLayout) recentContentScrollView.getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getMainImageView();
                                Card card = (Card) ((ShadowOverlayContainer) ((HorizontalGridView) ((ListRowView) ((LinearLayout) recentContentScrollView.getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild().getTag();
                                if (card != null) {
                                    launchDetailsActivity(card, imageView);
                                }
                            }
                        }
                        else
                        {
                            if(subMenuItems.hasFocus())
                            {
                                CardListRow row = (CardListRow) submenuAdapter.get(0);

                                for (Card menu: row.getCardRow().getCards()) {
                                    if(menu.isSelected())
                                    {
                                        decideActivity(menu);
                                        break;
                                    }
                                }

                            }

                        }
                    }

                    break;
                case KEYCODE_DPAD_DOWN:
                    returnValue = true;
                    if(subMenuItems.hasFocus()) {
                        subMenuItems.setVisibility(View.GONE);
                        menuItems.requestFocus();
                    }
                    else
                    {
                        if(recentContentScrollView.hasFocus())
                        {
                            if(subMenuItems.getVisibility() == View.VISIBLE)
                            {
                                subMenuItems.requestFocus();
                            }
                            else
                            {
                                menuItems.requestFocus();
                            }
                        }
                    }
                    break;
                case KEYCODE_DPAD_UP:
                    returnValue = true;
                    if(subMenuItems.hasFocus() || (menuItems.hasFocus() && subMenuItems.getVisibility() != View.VISIBLE)) {
                        recentContentScrollView.requestFocus();
                    }
                    else
                    {
                        if(subMenuItems.getVisibility() == View.VISIBLE)
                        {
                            subMenuItems.requestFocus();
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

    @Override
    public void onBackPressed() {
        if(subMenuItems.hasFocus()) {
            subMenuItems.setVisibility(View.GONE);
            menuItems.requestFocus();
        }
        else {
            CustomDialog confirmDialog = new CustomDialog(this, true, getString(R.string.confirm), getString(R.string.want_to_exit), getString(R.string.no), getString(R.string.yes), true);
            confirmDialog.setOnLeftButtonClick(view -> confirmDialog.hide());
            confirmDialog.setOnRightButtonClick(view -> {
                ActivityCompat.finishAffinity(MainMenuActivity.this);
                finish();
            });
        }

    }

    private void showSubmenu(Card menuSelected)
    {
        submenuAdapter = new ArrayObjectAdapter(new NoShadowRowPresenterSelector());
        if(menuSelected.getId() == LIVE_ID)
        {
            dataManager.getLiveCategories()
                    .onErrorReturn(error->
                            {
                                return new ArrayList<>();
                            }
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(genres -> {
                        PresenterSelector presenterSelector = new CardPresenterSelector(this);
                        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
                        CardRow cardRow = new CardRow();
                        List<Card> cards = new ArrayList<>();

                        for (Genre genre: genres ) {
                            cards.add(new Card(genre.getId(),genre.getName(), Card.Type.SUBMENU));
                        }

                        cards.add(new Card(ADULTS_ID, getString(R.string.adults_1), Card.Type.SUBMENU));

                        listRowAdapter.addAll(0, cards);
                        cardRow.setCards(cards);
                        HeaderItem headerItem = new HeaderItem(EMPTY_STRING);
                        CardListRow cardListRow = new CardListRow(headerItem, listRowAdapter, cardRow);


                        submenuAdapter.add(cardListRow);
                        createAndSetWrapperPresenter(submenuAdapter, submenuAdapterPresenter);
                        submenuBridgeAdapter.setAdapter(submenuAdapter);
                        submenuBridgeAdapter.setPresenter(submenuAdapterPresenter);
                        subMenuItems.setAdapter(submenuBridgeAdapter);
                        subMenuItems.setVisibility(View.VISIBLE);
                        subMenuItems.requestFocus();

                    });

        }
        else
        {
            submenuAdapter.add(createSubmenuCardRows(menuSelected));
            createAndSetWrapperPresenter(submenuAdapter, submenuAdapterPresenter);
            submenuBridgeAdapter.setAdapter(submenuAdapter);
            submenuBridgeAdapter.setPresenter(submenuAdapterPresenter);
            subMenuItems.setAdapter(submenuBridgeAdapter);
            subMenuItems.setVisibility(View.VISIBLE);
            subMenuItems.requestFocus();

        }

    }

    private  void decideActivity(Card submenu) {
        Intent intent = null;
        if(selectedMenu.getId() == LIVE_ID)
        {
            if(  submenu.getId() == ADULTS_ID)
            {
                final InputDialog codeDialog = new InputDialog(this, true, getString(R.string.restricted_content), "", getString(R.string.send_code), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                codeDialog.setOnLeftButtonClick(view -> dataManager.validateAccessCode(codeDialog.getInput())
                        .onErrorReturn(error->
                        {
                            return false;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(valid -> {
                            if(valid)
                            {
                                Intent intent1 = EPGActivity.getStartIntent(MainMenuActivity.this);
                                intent1.putExtra(SUBMENU_EXTRA, submenu);
                                startActivity(intent1);
                            }
                            else
                            {
                                new CustomDialog(MainMenuActivity.this, true, getString(R.string.invalid_access_code_title), getString(R.string.invalid_access_code), getString(R.string.ok), EMPTY_STRING, true );
                            }
                        }));
            }
            else
            {
                intent = EPGActivity.getStartIntent(this);
                intent.putExtra(SUBMENU_EXTRA, submenu);
                ArrayList<Card> categories = new ArrayList<>(((CardListRow) submenuAdapter.get(0)).getCardRow().getCards());
                categories.remove(categories.size()-1);
                intent.putExtra(CATEGORIES_EXTRA,categories);
                startActivity(intent);

            }
            return;
        }
        else {
            switch (submenu.getId()) {
                case MOVIES_BY_GENRE:
                case MOVIES_BY_ACTOR:
                case MOVIES_BY_YEAR:
                case SERIES_BY_GENRE:
                case SERIES_BY_YEAR:
                case SERIES_BY_LETTER:
                    intent = ContentCategoryActivity.getStartIntent(this);
                    break;
                case ADULTS_BY_GENRE:
                case ADULTS_BY_YEAR:
                case ADULTS_BY_ACTOR:
                    final InputDialog codeDialog = new InputDialog(this, true, getString(R.string.restricted_content), "", getString(R.string.send_code), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
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
                                                Intent intentAdults = ContentCategoryActivity.getStartIntent(MainMenuActivity.this);
                                                intentAdults.putExtra(SUBMENU_EXTRA, submenu.getId());
                                                startActivity(intentAdults);
                                            }
                                            else
                                            {
                                                new CustomDialog(MainMenuActivity.this, true, getString(R.string.invalid_access_code_title), getString(R.string.invalid_access_code), getString(R.string.ok), EMPTY_STRING, true );
                                            }
                                        }
                                    });
                        }
                    });
                    return;
            }
        }

        if (intent != null) {
            intent.putExtra(SUBMENU_EXTRA, submenu.getId());
            startActivity(intent);
        }

    }

    private void launchDetailsActivity(Card selectedMovie, ImageView thumbnailImageView)
    {
        try {
            Intent intent = MovieDetailsActivity.getStartIntent(this);
            intent.putExtra(ITEM_COVER_EXTRA,  selectedMovie);
            intent.putExtra(THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA, ViewCompat.getTransitionName(thumbnailImageView));

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( MainMenuActivity.this,
                    Pair.create(thumbnailImageView,    TRANSITION_NAME )
            );

            startActivity(intent, options.toBundle());


        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }



    private  void initUI()
    {
        menuAdapter = new ArrayObjectAdapter(new NoShadowRowPresenterSelector());
        menuAdapter.add(createMenuCardRows());
        createAndSetWrapperPresenter(menuAdapter, menuAdapterPresenter);
        menuBridgeAdapter.setAdapter(menuAdapter);
        menuBridgeAdapter.setPresenter(menuAdapterPresenter);
        menuItems.setAdapter(menuBridgeAdapter);
        menuItems.setVisibility(View.VISIBLE);

        dataManager.getMovies(GET_MOVIES_RECENT_ADDED, EMPTY_STRING, 0, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error->
                        new ArrayList<>())
                .subscribe((List<ItemCover> lastAddeds) -> {
                    recentMoviesAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());
                    recentMoviesAdapter.add(createCardRows(lastAddeds));
                    createAndSetWrapperPresenter(recentMoviesAdapter, recentMoviesAdapterPresenter);

                    recentMoviesBridgeAdapter.setAdapter(recentMoviesAdapter);
                    recentMoviesBridgeAdapter.setPresenter(recentMoviesAdapterPresenter);

                    if (recentContentScrollView != null) {
                        recentContentScrollView.setVisibility(View.VISIBLE);
                        recentContentScrollView.setAdapter(recentMoviesBridgeAdapter);
                    }
                });
    }

    private Row createCardRows(List<ItemCover> items) {
        // Build main row using the ImageCardViewPresenter.
        PresenterSelector presenterSelector = new CardPresenterSelector(this);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        CardRow cardRow = new CardRow();
        cardRow.setTitle(getString(R.string.recent_added_content));
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

    private Row createMenuCardRows()
    {
        // Build main row using the ImageCardViewPresenter.
        PresenterSelector presenterSelector = new CardPresenterSelector(this);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        CardRow cardRow = new CardRow();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(LIVE_ID, getString(R.string.live_stream) , R.drawable.live_on1, R.drawable.live_off1, Card.Type.MAIN_MENU));
        cards.add(new Card(MOVIE_ID, getString(R.string.movies),  R.drawable.movies_on1, R.drawable.movies_off1, Card.Type.MAIN_MENU));
        cards.add(new Card(SERIE_ID,  getString(R.string.series), R.drawable.series_on1, R.drawable.series_off1, Card.Type.MAIN_MENU));
        cards.add(new Card(KIDS_ID, getString(R.string.kids) , R.drawable.kids_on1, R.drawable.kids_off1, Card.Type.MAIN_MENU));
    //    cards.add(new Card(CONTINUOUS_ID, getString(R.string.continuous) , R.drawable.continuous_on, R.drawable.continuous_off, Card.Type.MAIN_MENU));
     //   cards.add(new Card(SPORTS_ID, getString(R.string.sports) , R.drawable.sports_on, R.drawable.sports_off, Card.Type.MAIN_MENU));

        if(currentProfile==null || currentProfile.getEnableAdults()) {
            cards.add(new Card(ADULTS_ID, getString(R.string.adults_1), R.drawable.adults_on1, R.drawable.adults_off, Card.Type.MAIN_MENU));
        }
        cards.add(new Card(RADIO_ID , getString(R.string.radio) , R.drawable.radio_on, R.drawable.radio_off, Card.Type.MAIN_MENU));

        cards.add(new Card(SETTINGS_ID , getString(R.string.configuration) , R.drawable.configuration_on1, R.drawable.configuration_off1, Card.Type.MAIN_MENU));
        cards.add(new Card(PROFILES_ID , getString(R.string.profiles) , R.drawable.profile_on, R.drawable.profile_off1, Card.Type.MAIN_MENU));
        listRowAdapter.addAll(0, cards);
        cardRow.setCards(cards);
        HeaderItem headerItem = new HeaderItem(EMPTY_STRING);
        return new CardListRow(headerItem, listRowAdapter, cardRow);
    }


    private Row createSubmenuCardRows(Card card)
    {
        // Build main row using the ImageCardViewPresenter.
        PresenterSelector presenterSelector = new CardPresenterSelector(this);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
        CardRow cardRow = new CardRow();
        List<Card> cards = new ArrayList<>();
        switch (card.getId()) {
            case MOVIE_ID:
                cards.add(new Card(MOVIES_BY_GENRE, getString(R.string.group_categories), Card.Type.SUBMENU));
                cards.add(new Card(MOVIES_BY_ACTOR, getString(R.string.group_actors), Card.Type.SUBMENU));
                cards.add(new Card(MOVIES_BY_YEAR, getString(R.string.group_year), Card.Type.SUBMENU));
                break;
            case SERIE_ID:
                cards.add(new Card(SERIES_BY_GENRE, getString(R.string.group_categories), Card.Type.SUBMENU));
                cards.add(new Card(SERIES_BY_YEAR, getString(R.string.group_year), Card.Type.SUBMENU));
                cards.add(new Card(SERIES_BY_LETTER, getString(R.string.group_abc), Card.Type.SUBMENU));

                break;
            case ADULTS_ID:
                cards.add(new Card(ADULTS_BY_GENRE, getString(R.string.group_categories), Card.Type.SUBMENU));
                cards.add(new Card(ADULTS_BY_YEAR, getString(R.string.group_year), Card.Type.SUBMENU));
                cards.add(new Card(ADULTS_BY_ACTOR, getString(R.string.group_actors), Card.Type.SUBMENU));
                break;
            case RADIO_ID:
                cards.add(new Card(RADIO_BY_GENRE, getString(R.string.group_categories), Card.Type.SUBMENU));
                cards.add(new Card(RADIO_BY_COUNTRY, getString(R.string.group_countries), Card.Type.SUBMENU));
                break;
        }
        listRowAdapter.addAll(0, cards);
        cardRow.setCards(cards);
        HeaderItem headerItem = new HeaderItem(EMPTY_STRING);
        return new CardListRow(headerItem, listRowAdapter, cardRow);
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onRegistration() {
        String formattedDate = new SimpleDateFormat(getString(R.string.expiration_date_format)).format(registrationResponse.getExpirationDate());
        if(expirationDateTextView != null) {
            expirationDateTextView.setText(formattedDate);
        }
        if(serialNumberTextView != null) {
            serialNumberTextView.setText(dataManager.getDeviceSerialNumber());
        }


        super.onRegistration();

    }
}
