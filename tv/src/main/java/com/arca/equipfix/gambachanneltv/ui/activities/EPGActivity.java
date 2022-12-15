package com.arca.equipfix.gambachanneltv.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.arca.equipfix.gambachanneltv.GambaChannelApp;
import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;
import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.network.model.EPGLine;
import com.arca.equipfix.gambachanneltv.ui.adapters.CardsAdapter;
import com.arca.equipfix.gambachanneltv.ui.local_components.GambaExoPlayer;
import com.dm.emotionrating.library.RatingView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_MENU;
import static com.arca.equipfix.gambachanneltv.ui.activities.PlayerActivity.PREFER_EXTENSION_DECODERS;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CATEGORIES_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_INDEX_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_LIST_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_URL_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.FIVE_MINUTES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.HALF_HOUR;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.MIN_DURATION_BEFORE_MOVE_EPG;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.PLAY_CODE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SUBMENU_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.TWO_MINUTES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ZERO_HOURS;

public class EPGActivity extends AuthenticateBaseActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, EPGActivity.class);
    }

    @Inject
    DefaultBandwidthMeter bandwidthMeter;

    @Inject
    TrackSelection.Factory fixedFactory;



    List<EPGLine> channels;
    private  int firstLine = 0;

    int selectedRow = 0;

    private long mStartMoveTime;
    private int mLastDirection;
    private Card currentCategory;

    float SCALE = 0;
    int margin2DP = 0;
    int margin15DP = 0;
    Calendar startDate;
    Calendar endDate;
    int[] rowIds = new int[5];
    int[] linearLayoutsIds = new int[5];
    int[] logosIds = new int[5];
    int[] namesIds = new int[5];
    int[] hoursIds= new int[6];
    int previewRow  = -1;
    boolean drawingEPG= false;

    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private Handler mainHandler;
    private MediaSourceEventListener eventLogger;
    private  ArrayList<Card> categories;



    @BindView(R.id.channelTitleTextView) TextView channelTitleTextView;
    @BindView(R.id.channelLogo)  ImageView channelLogoImageView;
    @BindView(R.id.programTitleTextView) TextView programTitleTextView;
    @BindView(R.id.programStartTimeTextView) TextView programStartTimeTextView;
    @BindView(R.id.programEndTimeTextView) TextView programEndTimeTextView;
    @BindView(R.id.subtitleTextView) TextView subtitleTextView;
    @BindView(R.id.ratingTextView)  TextView ratingTextView;
    @BindView(R.id.ratingView) RatingView ratingView;

    @BindView(R.id.descriptionTextView) TextView descriptionTextView;
    @BindView(R.id.categoriesTextView) TextView categoriesTextView;
    @BindView(R.id.yearTextView) TextView yearTextView;
    @BindView(R.id.actorsTextView) TextView actorsTextView;

    @BindView(R.id.guideTableLayout) TableLayout guideTableLayout;
    @BindView(R.id.categoryName) TextView categoryName;
    @BindView(R.id.currentAction) TextView currentAction;
    @BindView(R.id.playerView)    GambaExoPlayer playerView;
    @BindView(R.id.currentHour) TextView currentHour;
    @BindView(R.id.downloadingGuide) TextView downloadingGuide;


    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            showCategoriesMenu();
        }
    });

    private Handler updateProgramsHandler = new Handler();
    Runnable callUpdateChannels = new Runnable() {
        @Override
        public void run() {
            updatePrograms();
            updateProgramsHandler.removeCallbacks(callUpdateChannels);
            updateProgramsHandler.postDelayed(callUpdateChannels, TWO_MINUTES);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epg);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        mainHandler = new Handler();
        mediaDataSourceFactory = ((GambaChannelApp) getApplication()).buildDataSourceFactory(bandwidthMeter);
        initValues();
        showHours();
        updateProgramsHandler.postDelayed(callUpdateChannels, TWO_MINUTES);

       /* broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                        if (currentHour != null) {
                            currentHour.setText(sdtWathTime.format(new Date()));
                        }
                    }
                }
                catch (Exception ignore)
                {

                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));*/

    }

    @Override
    protected void onPause() {
        releasePlayer();
        super.onPause();
    }

    void initValues()
    {
        SCALE  = this.getResources().getDisplayMetrics().density;
        margin2DP = Math.round(2f * SCALE);
        margin15DP = Math.round(15f * SCALE);



        for (int i = 1; i<5; i++)
        {
            rowIds[i-1] = getResources().getIdentifier("channelRow"+i, "id", this.getPackageName());
            linearLayoutsIds[i-1] = getResources().getIdentifier("linearLayoutRow"+i, "id", this.getPackageName());
            logosIds[i-1] = getResources().getIdentifier("channelLogo"+i, "id", this.getPackageName());
            namesIds[i-1] = getResources().getIdentifier("channelName"+i, "id", this.getPackageName());
        }

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        int currentMinute = startDate.get(Calendar.MINUTE);
        if(currentMinute<30)
        {
            startDate.add(Calendar.MINUTE, currentMinute*-1);
            endDate.add(Calendar.MINUTE, currentMinute*-1);
        }
        else
        {
            startDate.add(Calendar.MINUTE, 30 - currentMinute);
            endDate.add(Calendar.MINUTE, 30 - currentMinute);
        }
        endDate.add(Calendar.HOUR, 3);

        for(int i=1; i<7; i++)
        {
            hoursIds[i-1] = getResources().getIdentifier("hour"+i, "id", this.getPackageName());
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            if(extras.get(CATEGORIES_EXTRA) != null)
            {
                this.categories = extras.getParcelableArrayList(CATEGORIES_EXTRA);
            }
            else
            {
                this.categories = new ArrayList<>();
            }
            if(extras.get(SUBMENU_EXTRA) != null) {
                this.currentCategory = extras.getParcelable(SUBMENU_EXTRA);

            }
        }


        updateCategory();

    }

    private void updateCategory()
    {
        downloadingGuide.setVisibility(View.VISIBLE);
        this.categoryName.setText(this.currentCategory.getTitle());
        dataManager.getChannelList(currentCategory.getId()).
                onErrorReturn(error-> new ArrayList<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(epgLines -> {
                    channels = epgLines;
                    firstLine = 0;
                    showChannels();
                    downloadingGuide.setVisibility(View.GONE);
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(data != null && data.hasExtra(CHANNEL_INDEX_EXTRA)) {
                selectedRow = data.getIntExtra(CHANNEL_INDEX_EXTRA, selectedRow);
                firstLine = selectedRow;
                previewRow = firstLine;
                showChannels();
            }
            releasePlayer();
            initializePlayer();

        }
        catch (Exception ignore)
        {

        }

    }

    private  void showHours()
    {
        try {
            SimpleDateFormat simpleDateFormat;
            SimpleDateFormat justHour = new SimpleDateFormat(getString(R.string.hh_mm), Locale.US);
            Date currentDate = startDate.getTime();

            for (int i = 0; i < 6; i++) {
                TextView hourTextView = findViewById(hoursIds[i]);

                if (justHour.format(currentDate).equals(ZERO_HOURS)) {
                    simpleDateFormat = new SimpleDateFormat(getString(R.string.ee_hh_mm), Locale.US);
                } else {
                    simpleDateFormat = new SimpleDateFormat(getString(R.string.hh_mm), Locale.US);
                }

                hourTextView.setText(simpleDateFormat.format(currentDate.getTime()));
                currentDate = new Date(currentDate.getTime() + HALF_HOUR);
            }
            drawingEPG = false;
        }
        catch (Exception ignore)
        {

        }
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        if(updateProgramsHandler != null)
        {
            updateProgramsHandler.removeCallbacks(callUpdateChannels);
        }
        super.onDestroy();
    }

    private void showCategoriesMenu()
    {
        CardsAdapter adapter = new CardsAdapter(EPGActivity.this, 0, categories);
        new LovelyChoiceDialog(this)

                .setTopColorRes(R.color.gambaRedTransparent)
                .setTitle(R.string.select_category)
                .setIcon(R.drawable.live_on_icon)
                .setMessage(R.string.categories)
                .setItems(adapter, (position, item) -> {
                    if(item != null) {
                        if (currentCategory.getId() != item.getId()) {
                            currentCategory = item;
                            clearCurrentChannels();

                            updateCategory();
                        }
                    }
                })
                .show();

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
                case  KEYCODE_MENU:
                    showCategoriesMenu();
                    break;
                case KEYCODE_DPAD_CENTER :
                case KEYCODE_ENTER :
                    TableRow currentPlayRow = findViewById(rowIds[0]);
                    ChannelProgram currentProgram ;
                    TextView selectedProgram ;
                    if (currentPlayRow.getChildCount() > 1) {
                        selectedProgram = (TextView) currentPlayRow.getChildAt(1);
                        currentProgram = (ChannelProgram) selectedProgram.getTag();
                        Calendar currentDate = Calendar.getInstance();
                        if(currentProgram.isFirstProgram() || (currentProgram.getStartDate().getTime() - currentDate.getTime().getTime() <= TWO_MINUTES))
                        {
                            EPGLine channel = this.channels.get(selectedRow);
                            if(selectedRow == previewRow)
                            {
                                releasePlayer();
                                //player.setPlayWhenReady(false);
                                Intent intent = LivePlayerActivity.getStartIntent(this);
                                intent.putParcelableArrayListExtra(CHANNEL_LIST_EXTRA, new ArrayList<>(channels));
                                intent.putExtra(CHANNEL_INDEX_EXTRA, selectedRow);
                                intent.putExtra(CHANNEL_URL_EXTRA, channel.getUrl()  );
                                startActivityForResult(intent, PLAY_CODE);

                            }
                            else
                            {
                                releasePlayer();

                                previewRow = selectedRow;
                                initializePlayer();
                                currentAction.setText(getString(R.string.press_ok_to_play));
                                showChannels();
                            }
                        }
                        else
                        {
                            EPGLine channel = this.channels.get(selectedRow);
                            TableRow row = guideTableLayout.findViewById(rowIds[0]);
                            if(row.getChildCount()>1) {
                                TextView textView = (TextView) row.getChildAt(1);
                                if(textView != null) {
                                    ChannelProgram program = (ChannelProgram)textView.getTag();
   /*                                 if(program != null && program.getId() > 0) {
                                        if(!program.getReminder()) {
                                            dataManager.addProgramReminder(channel.getId(), program.getTitle(), false);
                                            program.setReminder(true);
                                            Toast.makeText(EPGActivity.this, R.string.reminder_added, Toast.LENGTH_LONG).show();*/

/*                                                    .onErrorReturn(error -> 0)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(reminderId -> {
                                                        if (reminderId > 0) {
                                                            program.setReminder(reminderId);
                                                            Toast.makeText(EPGActivity.this, R.string.reminder_added, Toast.LENGTH_LONG).show();
                                                        }
                                                    });*/
                                        /*}
                                        else
                                        {
                                            if (dataManager.deleteProgramReminder( channel.getId(), program.getTitle() ))
                                            {
                                                program.setReminder(false);
                                                Toast.makeText(EPGActivity.this, R.string.reminder_deleted, Toast.LENGTH_LONG).show();

                                            }
/*                                                    .onErrorReturn(error -> false)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(deleted -> {
                                                        if (deleted) {
                                                            program.setReminder(0);
                                                            Toast.makeText(EPGActivity.this, R.string.reminder_deleted, Toast.LENGTH_LONG).show();
                                                        }
                                                    });*/
                                        //}
                                    //}
                                }
                            }
                        }
                    }

                    returnValue = true;
                    break;
                case KEYCODE_DPAD_DOWN:
                    if(drawingEPG) return true;

                    long pressDuration = System.currentTimeMillis() - mStartMoveTime;
                    if (pressDuration > MIN_DURATION_BEFORE_MOVE_EPG || mLastDirection != KEYCODE_DPAD_DOWN) {
                        mStartMoveTime = System.currentTimeMillis();
                        mLastDirection = KEYCODE_DPAD_DOWN;
                        this.firstLine++;
                        if(this.firstLine >= channels.size() )
                        {
                            this.firstLine = 0;
                        }

                        showChannels();

                    }
                    returnValue = true;
                    break;
                case KEYCODE_DPAD_UP:
                    if(drawingEPG) return  true;

                    long pressDuration2 = System.currentTimeMillis() - mStartMoveTime;
                    if (pressDuration2 > MIN_DURATION_BEFORE_MOVE_EPG || mLastDirection != KEYCODE_DPAD_UP) {
                        mStartMoveTime = System.currentTimeMillis();
                        mLastDirection = KEYCODE_DPAD_UP;
                        this.firstLine--;
                        if(this.firstLine < 0 )
                        {
                            this.firstLine = channels.size()-1;
                        }
                        showChannels();
                    }
                    returnValue = true;
                    break;
                case KEYCODE_DPAD_RIGHT:
                case KEYCODE_DPAD_LEFT:
                    if(drawingEPG) return  true;
                    TableRow currentRow = findViewById(rowIds[0]);
                    int minutes = 0;
                    if(currentRow.getChildCount()>1) {
                        int currentIndex = 1;
                        while(minutes==0 && currentIndex<currentRow.getChildCount()) {
                            {
                                TextView textView = (TextView) currentRow.getChildAt(currentIndex);
                                minutes = Math.round(((TableRow.LayoutParams) textView.getLayoutParams()).weight);
                                currentIndex++;
                            }
                        }
                    }

                    while (minutes % 15 != 0   )
                    {
                        minutes++;
                    }

                    if(keyCode == KEYCODE_DPAD_LEFT)
                    {
                        minutes *= -1;
                    }
                    startDate.add(Calendar.MINUTE, minutes);
                    endDate.add(Calendar.MINUTE, minutes);
                    showChannels();
                    returnValue = true;
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
        if( firstLine !=  previewRow && previewRow != -1)
        {
            firstLine =  previewRow ;
            showChannels();
        }
        else {
            super.onBackPressed();
        }
    }

    private void clearCurrentChannels()
    {
        try {
            for (int i = 0; i < 4; i++) {
                TableRow row = guideTableLayout.findViewById(rowIds[i]);
                if (row == null) {
                    break;
                }
                while (row.getChildCount() > 1) {
                    row.removeViewAt(1);
                }
                ImageView channelLogo = guideTableLayout.findViewById(logosIds[i]);
                channelLogo.setImageResource(android.R.color.transparent);
                TextView channelName = guideTableLayout.findViewById(namesIds[i]);
                channelName.setText(EMPTY_STRING);
            }
        }
        catch (Exception ignore)
        {

        }

    }

    private void updatePrograms()
    {
        try {
            if (this.channels == null) {
                return;
            }


            Calendar currentDate = Calendar.getInstance();
            for (int i = 0; i < this.channels.size(); i++) {
                EPGLine channel = channels.get(i);
                        if ((currentDate.getTime().getTime() - channel.getLastUpdate()) >= TWO_MINUTES) {
                            int finalI = i;
                            downloadingGuide.setVisibility(View.VISIBLE);
                            dataManager.getChannelPrograms(channel.getId())
                                    .onErrorReturn(error -> new ArrayList<>()
                                    )
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(channelPrograms -> {
                                        channel.setLastUpdate(currentDate.getTime().getTime() );
                                        if (channelPrograms != null && channelPrograms.size() > 0) {
                                            EPGLine updateChannel = channels.get(finalI);

                                            if(channelPrograms == null || channelPrograms.size() == 0)
                                            {
                                                ChannelProgram emptyProgram = new ChannelProgram();
                                                emptyProgram.setTitle(this.getResources().getString(R.string.no_info_description));
                                                Calendar emptyStart = Calendar.getInstance();
                                                emptyStart.add(Calendar.HOUR, -1);

                                                Calendar endDate = Calendar.getInstance();
                                                endDate.add(Calendar.DATE, 5);


                                                emptyProgram.setStartDate(emptyStart.getTime());
                                                emptyProgram.setEndDate(endDate.getTime());
                                                emptyProgram.setId(0L);
                                                channelPrograms.add(emptyProgram);
                                            }

                                            updateChannel.setPrograms(channelPrograms);

                                            downloadingGuide.setVisibility(View.GONE);

                                        }
                                    });
                        }
            }
        }
        catch (Exception ignore)
        {

        }
    }

    private void showChannels()
    {
        try {
            drawingEPG = true;
            selectedRow = firstLine;
            if (this.channels == null || this.channels.size() == 0) {
                return;
            }

            if (firstLine > this.channels.size()) {
                firstLine = 0;
            } else {
                if (firstLine < 0) {
                    firstLine = this.channels.size() - 1;
                }
            }


            for (int i = -1; i < Math.min(this.channels.size(), 5); i++) {
                EPGLine currentLine;
                int lineNumber = i + firstLine;
                if ((firstLine + i) >= this.channels.size()) {
                    lineNumber = (firstLine + i) - this.channels.size();
                } else {
                    if ((firstLine + i) < 0) {
                        lineNumber = this.channels.size() + i;
                    }
                }
                currentLine = this.channels.get(lineNumber);

                if (i >= 0 && i < 4) {
                    TableRow row = guideTableLayout.findViewById(rowIds[i]);
                    LinearLayout linearLayout = guideTableLayout.findViewById(linearLayoutsIds[i]);

                    if (lineNumber == previewRow) {
                        linearLayout.setBackground(getDrawable(R.drawable.red_border_line));
                        row.setBackground(getDrawable(R.drawable.red_border_line));
                    } else {
                        linearLayout.setBackground(getDrawable(R.drawable.white_border_line));
                        row.setBackground(getDrawable(R.drawable.white_border_line));
                    }
                    while (row.getChildCount() > 1) {
                        row.removeViewAt(1);
                    }

                    ImageView channelLogo = guideTableLayout.findViewById(logosIds[i]);
                    Picasso.get().load(currentLine.getThumbnail()).into(channelLogo);
                    TextView channelName = guideTableLayout.findViewById(namesIds[i]);
                    channelName.setText(currentLine.getName());

                }
                if (currentLine.getPrograms() == null || currentLine.getPrograms().size() == 0) {

                    ChannelProgram emptyProgram = new ChannelProgram();

                    Calendar emptyStart = Calendar.getInstance();
                    emptyStart.add(Calendar.HOUR, -1);

                    Calendar endDate = Calendar.getInstance();
                    endDate.add(Calendar.DATE, 5);

                    emptyProgram.setTitle(this.getResources().getString(R.string.no_info));
                    emptyProgram.setStartDate(emptyStart.getTime());
                    emptyProgram.setEndDate(endDate.getTime());
                    emptyProgram.setId(0L);
                    ArrayList<ChannelProgram> channelPrograms = new ArrayList<>();
                    channelPrograms.add(emptyProgram);

                    channels.get(lineNumber).setPrograms(channelPrograms);
                    if (i < 4 && i >= 0) {
                        showProgramsInRow(lineNumber, i, lineNumber == previewRow);
                    }


/*                    int finalLineNumber = lineNumber;
                    int finalI = i;
                    int finalLineNumber1 = lineNumber;

                        downloadingGuide.setVisibility(View.VISIBLE);
                        dataManager.getChannelPrograms(currentLine.getId())
                                .onErrorReturn(error -> new ArrayList<>()
                                )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(channelPrograms -> {
                                    if(channelPrograms == null || channelPrograms.size() == 0)
                                    {
                                        ChannelProgram emptyProgram = new ChannelProgram();

                                        Calendar emptyStart = Calendar.getInstance();
                                        emptyStart.add(Calendar.HOUR, -1);

                                        Calendar endDate = Calendar.getInstance();
                                        endDate.add(Calendar.DATE, 5);

                                        emptyProgram.setTitle(this.getResources().getString(R.string.no_info));
                                        emptyProgram.setStartDate(emptyStart.getTime());
                                        emptyProgram.setEndDate(endDate.getTime());
                                        emptyProgram.setId(0L);
                                        channelPrograms.add(emptyProgram);
                                    }
                                    channels.get(finalLineNumber).setPrograms(channelPrograms);
                                    if (finalI < 4 && finalI >= 0) {
                                        showProgramsInRow(finalLineNumber, finalI, finalLineNumber1 == previewRow);
                                    }
                                    downloadingGuide.setVisibility(View.GONE);
                                });*/
                } else {
                    if (i >= 0 && i < this.channels.size()) {
                        showProgramsInRow(lineNumber, i, lineNumber == previewRow);
                    }

                }
            }
            showHours();
        }
        catch (Exception ignore)
        {

        }
    }

    private  void addProgramTextView(ChannelProgram program, long minutes, int rowNumber, boolean firstColumn, boolean isFirstProgram, boolean isPreviewRow)
    {
        try {
            if (minutes == 29 || minutes == 59 || minutes == 89 || minutes == 119 || minutes == 149 || minutes == 179) {
                minutes += 1;
            }

            if (guideTableLayout == null) {
                return;
            }

            TableRow row = guideTableLayout.findViewById(rowIds[rowNumber]);
            TableRow.LayoutParams programLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, minutes);
            TextView programTextView = new TextView(this);
            programTextView.setText(program.getTitle());
            programTextView.setTag(program);

            programTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            programTextView.setGravity(Gravity.CENTER_VERTICAL);

            programTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, this.getResources().getDimension(R.dimen.text_16_sp));
            programTextView.setLayoutParams(programLayout);
            program.setFirstProgram(isFirstProgram);
            if (rowNumber == 0 && firstColumn) {
                programTextView.setBackground(this.getResources().getDrawable(R.drawable.black_border_line));
                programTextView.setTextColor(Color.parseColor("black"));
                if (program.isFirstProgram() && previewRow == rowNumber) {
                    currentAction.setText(getString(R.string.press_ok_to_play));
                } else {
                    if (program.isFirstProgram()) {
                        currentAction.setText(getString(R.string.press_ok_to_preview));
                    } else {
                        if (program.getReminder()) {
                            currentAction.setText(getString(R.string.press_ok_to_remove_reminder));
                        } else {
                            if (program.getId() > 0) {
                                currentAction.setText(getString(R.string.press_ok_to_reminder));
                            } else {
                                currentAction.setText(getString(R.string.no_actions_available));
                            }
                        }
                    }
                }

            } else {

                if (program.getReminder() ) {
                    programTextView.setBackground(this.getResources().getDrawable(R.drawable.reminder_background));
                } else {

                    if (isPreviewRow) {
                        programTextView.setBackground(this.getResources().getDrawable(R.drawable.red_border_line));
                    } else {
                        programTextView.setBackground(this.getResources().getDrawable(R.drawable.white_border_line));
                    }
                }
                programTextView.setTextColor(Color.parseColor("white"));
            }
            if (row != null) {
                row.addView(programTextView);
            }
        }
        catch (Exception ignore)
        {

        }
    }

    private  void showProgramDetails(int channelIndex)
    {
        try {
            EPGLine channel = channels.get(channelIndex);

            channelTitleTextView.setText(channel.getName());
            Picasso.get().load(channel.getThumbnail()).into(channelLogoImageView);
            TableRow currentRow = findViewById(rowIds[0]);
            ChannelProgram currentProgram = null;
            TextView selectedProgram ;
            if (currentRow.getChildCount() > 1) {
                selectedProgram = (TextView) currentRow.getChildAt(1);
                currentProgram = (ChannelProgram) selectedProgram.getTag();
            }

            if(currentProgram != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.hh_mm), Locale.US);
                if (currentProgram.getId() > 0) {
                    programTitleTextView.setText(currentProgram.getTitle());
                    programStartTimeTextView.setText(simpleDateFormat.format(currentProgram.getStartDate()));
                    programEndTimeTextView.setText(simpleDateFormat.format(currentProgram.getEndDate()));

                    subtitleTextView.setText(currentProgram.getSubTitle());
                    descriptionTextView.setText(currentProgram.getDescription());
                    if (currentProgram.getYear() > 0) {
                        yearTextView.setText(String.valueOf(currentProgram.getYear()));
                    } else {
                        yearTextView.setText(EMPTY_STRING);
                    }
                    categoriesTextView.setText(currentProgram.getCategories());
                    ratingTextView.setText(currentProgram.getRating());
                    actorsTextView.setText(currentProgram.getActors());
                    try {
                        ratingView.setRating(Math.round(Float.parseFloat(currentProgram.getStartRating())));
                    } catch (Exception ex) {
                        ratingView.setRating(0);
                    }
                } else {
                    programTitleTextView.setText(currentProgram.getTitle());
                    programStartTimeTextView.setText(simpleDateFormat.format(currentProgram.getStartDate()));
                    programEndTimeTextView.setText(simpleDateFormat.format(currentProgram.getEndDate()));
                    subtitleTextView.setText(EMPTY_STRING);
                    descriptionTextView.setText(this.getResources().getString(R.string.no_info_description));
                    yearTextView.setText(EMPTY_STRING);
                    categoriesTextView.setText(EMPTY_STRING);
                    ratingTextView.setText(EMPTY_STRING);
                    actorsTextView.setText(EMPTY_STRING);
                    ratingView.setRating(0);
                }
            }
        }
        catch (Exception ignored)
        {

        }

    }

    private  void showProgramsInRow(int channelIndex, int rowNumber, boolean isPreviewRow)
    {

        try {
            while (startDate.get(Calendar.MINUTE) != 15 && startDate.get(Calendar.MINUTE) != 30 && startDate.get(Calendar.MINUTE) != 45 && startDate.get(Calendar.MINUTE) != 0) {
                startDate.add(Calendar.MINUTE, -1);
                endDate.add(Calendar.MINUTE, -1);
            }

            Calendar currentDate = Calendar.getInstance();
            int currentMinute = currentDate.get(Calendar.MINUTE);
            if (currentMinute < 30) {
                currentDate.add(Calendar.MINUTE, currentMinute * -1);
            } else {
                currentDate.add(Calendar.MINUTE, 30 - currentMinute);
            }

            while (currentDate.getTime().getTime() - startDate.getTime().getTime() > (TWO_MINUTES)) {
                startDate.add(Calendar.MINUTE, 2);
                endDate.add(Calendar.MINUTE, 2);
            }

            boolean isFirstProgram = true;

            int index = 0;
            EPGLine channel = this.channels.get(channelIndex);
            Date lastDate = startDate.getTime();
            boolean firstColumn = true;
            while (index < channel.getPrograms().size()) {
                ChannelProgram program = channel.getPrograms().get(index);
                if ((endDate.getTime().getTime() - program.getStartDate().getTime()) >= FIVE_MINUTES && (program.getEndDate().getTime() - startDate.getTime().getTime()) > TWO_MINUTES) {
                    long minutes;
                    if (program.getStartDate().getTime() - lastDate.getTime() > TWO_MINUTES) {
                        long diffInMs = program.getStartDate().getTime() - lastDate.getTime();
                        minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
                        ChannelProgram emptyProgram = new ChannelProgram(this.getResources().getString(R.string.no_info), lastDate, program.getStartDate());

                        addProgramTextView(emptyProgram, minutes, rowNumber, firstColumn, index == 0, isPreviewRow);
                        isFirstProgram = false;
                        firstColumn = false;
                    }


                    if (program.getStartDate().getTime() <= startDate.getTime().getTime()) {
                        if (program.getEndDate().getTime() <= endDate.getTime().getTime()) {
                            long diffInMs = program.getEndDate().getTime() - startDate.getTime().getTime();
                            minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
                        } else {
                            long diffInMs = endDate.getTime().getTime() - startDate.getTime().getTime();
                            minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
                        }
                    } else {
                        if (program.getEndDate().getTime() <= endDate.getTime().getTime()) {
                            long diffInMs = program.getEndDate().getTime() - program.getStartDate().getTime();
                            minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
                        } else {
                            long diffInMs = endDate.getTime().getTime() - program.getStartDate().getTime();
                            minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
                        }
                    }
                    lastDate = program.getEndDate();

                    addProgramTextView(program, minutes, rowNumber, firstColumn, (isFirstProgram && index == 0 && (program.getStartDate().getTime() - currentDate.getTime().getTime() <= TWO_MINUTES)), isPreviewRow);
                    firstColumn = false;

                } else {
                    if (program.getStartDate().getTime() - endDate.getTime().getTime() > TWO_MINUTES) {
                        break;
                    }
                }
                index++;
            }

            if (endDate.getTime().getTime() - lastDate.getTime() > TWO_MINUTES) {
                long minutes;

                long diffInMs = endDate.getTime().getTime() - lastDate.getTime();
                minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
                ChannelProgram emptyProgram = new ChannelProgram(this.getResources().getString(R.string.no_info), lastDate, endDate.getTime());
                if(channel.getPrograms().size() == 0)
                {
                    ArrayList<ChannelProgram> emptyList = new ArrayList<>();
                    emptyList.add(emptyProgram);
                    channel.setPrograms(emptyList);
                }

                addProgramTextView(emptyProgram, minutes, rowNumber, firstColumn, index == 0, isPreviewRow);
            }

            if (rowNumber == 0) {
                showProgramDetails(channelIndex);
            }
            guideTableLayout.setVisibility(View.VISIBLE);
        }
        catch (Exception ignore)
        {

        }
    }


    private void initializePlayer( ) {


        Intent intent = getIntent();
        boolean needNewPlayer = player == null;
        if (needNewPlayer) {
            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            eventLogger = new MediaSourceEventListener() {
                @Override
                public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

                }

                @Override
                public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

                }

                @Override
                public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

                }

                @Override
                public void onLoadCompleted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

                }

                @Override
                public void onLoadCanceled(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

                }

                @Override
                public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {

                }

                @Override
                public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

                }

                @Override
                public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

                }

                @Override
                public void onDownstreamFormatChanged(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

                }
            };

        }

        boolean preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS, false);
        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                ((GambaChannelApp) getApplication()).useExtensionRenderers()
                        ? (preferExtensionDecoders ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this,
                null, extensionRendererMode);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        playerView.setPlayer(player);
        player.addListener(new PlayerEventListener());

        EPGLine channel = this.channels.get(previewRow);

        dataManager.getLiveUrl(channel.getId())
                .onErrorReturn(error ->
                        EMPTY_STRING)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(url -> {
                    MediaSource mediaSource =  buildMediaSource(Uri.parse(url), "");
                    if(player != null) {
                        player.prepare(mediaSource, true, true);
                    }

                });


        player.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        @C.ContentType int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        switch (type) {

            case C.TYPE_HLS:
                return new HlsMediaSource.Factory( mediaDataSourceFactory)
                        .setAllowChunklessPreparation(true)
                        .createMediaSource(uri, mainHandler, eventLogger);
            //return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                if(uri.toString().startsWith("rtmp"))
                {
                    RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
                    return new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
                            .createMediaSource(uri);

                }
                else {
                    return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                            mainHandler, null);
                }
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            trackSelector = null;
            eventLogger = null;
        }
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((GambaChannelApp) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? bandwidthMeter : null);
    }


    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((GambaChannelApp) getApplication())
                .buildHttpDataSourceFactory(useBandwidthMeter ? bandwidthMeter : null);
    }



    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
            }
            else {
            }

            if(playbackState == Player.STATE_READY )
            {


            }
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
        }





        @Override
        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                Exception cause = e.getRendererException();
                if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                            (MediaCodecRenderer.DecoderInitializationException) cause;
                    if (decoderInitializationException.decoderName == null) {
                        if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                            errorString = getString(R.string.error_querying_decoders);
                        } else if (decoderInitializationException.secureDecoderRequired) {
                            errorString = getString(R.string.error_no_secure_decoder,
                                    decoderInitializationException.mimeType);
                        } else {
                            errorString = getString(R.string.error_no_decoder,
                                    decoderInitializationException.mimeType);
                        }
                    } else {
                        errorString = getString(R.string.error_instantiating_decoder,
                                decoderInitializationException.decoderName);
                    }
                }
            }
            else {
                releasePlayer();
                initializePlayer();
            }
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        }

    }



}
