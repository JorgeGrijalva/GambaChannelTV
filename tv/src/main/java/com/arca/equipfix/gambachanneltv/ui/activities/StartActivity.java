package com.arca.equipfix.gambachanneltv.ui.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.arca.equipfix.gambachanneltv.data.ItemType;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemCover;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.data.network.model.enums.RegistrationStatus;
import com.arca.equipfix.gambachanneltv.data.prefs.model.LoginType;
import com.arca.equipfix.gambachanneltv.ui.adapters.ShadowRowPresenterSelector;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardListRow;
import com.arca.equipfix.gambachanneltv.ui.local_components.CardRow;
import com.arca.equipfix.gambachanneltv.ui.presenters.CardPresenterSelector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.GET_MOVIES_RECENT_ADDED;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_COVER_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.REGISTRATION_INFORMATION;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.REGISTRATION_STATUS_CHECK_TIME;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.TRANSITION_NAME;

public class StartActivity extends BaseActivity implements View.OnClickListener{

    private ArrayObjectAdapter mRowsAdapter;

    final ItemBridgeAdapter mBridgeAdapter = new ItemBridgeAdapter();
    private PresenterSelector mAdapterPresenter;

    @BindView(R.id.recentContentScrollView)
    VerticalGridView recentContentScrollView;
    @BindView(R.id.registrationCodeTextView)
    TextView registrationCodeTextView;
    @BindView(R.id.activateTextView) TextView activateTextView;
    @BindView(R.id.registrationClient) TextView registrationClient;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, StartActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setUnBinder(ButterKnife.bind(this));
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            registrationResponse = extras.getParcelable(REGISTRATION_INFORMATION);
            if (registrationResponse != null) {
                initUI();
            }

            registrationHandler = new Handler();
            callRegistrationStatus(true);


            registrationHandler.postDelayed(registrationStatus, REGISTRATION_STATUS_CHECK_TIME); // 1 Minutes
        }

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

    private void initUI()
    {


        registrationCodeTextView.setText(registrationResponse.getRegistrationCode());
        if(registrationResponse.getStatus() == RegistrationStatus.EXPIRED)
        {
            activateTextView.setText(R.string.membership_expired);
            registrationClient.setText(registrationResponse.getClientName());
        }
        dataManager.getMovies(GET_MOVIES_RECENT_ADDED, EMPTY_STRING, 0, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error->
                new ArrayList<>())
                .subscribe((List<ItemCover> lastAddeds) -> {
                    mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());
                    mRowsAdapter.add(createCardRows(lastAddeds));
                    createAndSetWrapperPresenter();

                    mBridgeAdapter.setAdapter(mRowsAdapter);
                    mBridgeAdapter.setPresenter(mAdapterPresenter);

                    if (recentContentScrollView != null) {
                        recentContentScrollView.setAdapter(mBridgeAdapter);
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
                    if(mRowsAdapter != null)
                    {
                        ImageView imageView = ((ImageCardView) ((ShadowOverlayContainer)((HorizontalGridView)((ListRowView)((LinearLayout)recentContentScrollView.getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getMainImageView();
                       Card card=  (Card) ((ShadowOverlayContainer)((HorizontalGridView)((ListRowView)((LinearLayout)recentContentScrollView.getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild().getTag();
                       if(card != null)
                       {
                           launchDetailsActivity(card, imageView);
                       }
                    }
                    returnValue = true;
                    break;
            }
            if(returnValue) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
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
        HeaderItem headerItem = new HeaderItem(getString(R.string.recent_added_content));
        headerItem.setDescription(getString(R.string.recent_added_content));
        return new CardListRow(headerItem, listRowAdapter, cardRow);
    }


    private void launchDetailsActivity(Card selectedMovie, ImageView thumbnailImageView)
    {
        try {
            Intent intent = MovieDetailsActivity.getStartIntent(this);
            intent.putExtra(ITEM_COVER_EXTRA,  selectedMovie);
            intent.putExtra(ITEM_TYPE_EXTRA, ItemType.MOVIE.getValue());
            intent.putExtra(THUMBNAIL_IMAGE_TRANSITION_NAME_EXTRA, ViewCompat.getTransitionName(thumbnailImageView));

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    StartActivity.this,
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
    public void onClick(View view) {

    }



    @Override
    public void onRegistration() {
        if(registrationResponse != null && registrationResponse.getStatus() != RegistrationStatus.ERROR)
        {
            Profile profile = dataManager.getSelectedProfile();
            if(profile != null) {
                callStartSession(profile);
            }
        }
    }

    @Override
    public void onSessionStarted(SessionInformation sessionInformation) {

        if(registrationResponse.getStatus() == RegistrationStatus.ACTIVE)
        {
            dataManager.setLoginType(LoginType.CLIENT);
            Intent intent = MainMenuActivity.getStartIntent(this);
            assert intent != null;
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        registrationHandler.removeCallbacks(registrationStatus);
        registrationHandler.postDelayed(registrationStatus , REGISTRATION_STATUS_CHECK_TIME);

    }

    @Override
    public void onSessionError() {
        //callRegistrationStatus();
        registrationHandler.postDelayed(registrationStatus , REGISTRATION_STATUS_CHECK_TIME);
    }
}
