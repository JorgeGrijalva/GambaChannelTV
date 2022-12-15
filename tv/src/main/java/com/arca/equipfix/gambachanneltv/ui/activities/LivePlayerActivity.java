package com.arca.equipfix.gambachanneltv.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arca.equipfix.gambachanneltv.GambaChannelApp;
import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;
import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.models.PendingCall;
import com.arca.equipfix.gambachanneltv.data.network.model.EPGLine;
import com.arca.equipfix.gambachanneltv.ui.adapters.CardsAdapter;
import com.arca.equipfix.gambachanneltv.ui.local_components.CustomDialog;
import com.arca.equipfix.gambachanneltv.ui.local_components.GambaExoPlayer;
import com.arca.equipfix.gambachanneltv.ui.local_components.TrackSelectionHelper;
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
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_MENU;
import static android.view.View.GONE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_INDEX_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_LIST_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CHANNEL_URL_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.TWO_MINUTES;

public class LivePlayerActivity extends AuthenticateBaseActivity  implements  PlaybackControlView.VisibilityListener {



    public static Intent getStartIntent(Context context) {
        return new Intent(context, LivePlayerActivity.class);
    }

    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";


    @Inject
    DefaultBandwidthMeter bandwidthMeter;

    @Inject
    TrackSelection.Factory fixedFactory;


    @BindView(R.id.player_view)    GambaExoPlayer simpleExoPlayerView;
    @BindView(R.id.channelsRecyclerView)
    RecyclerView channelsRecyclerView;
    @BindView(R.id.programInfoLayout) LinearLayout programInfoLayout;
    @BindView(R.id.channelInfoLayout) LinearLayout channelInfoLayout;
    @BindView(R.id.programTitleTextView) TextView programTitleTextView;
    @BindView(R.id.programStartTimeTextView) TextView programStartTimeTextView;
    @BindView(R.id.programEndTimeTextView) TextView programEndTimeTextView;
    @BindView(R.id.programDescriptionTextView) TextView programDescriptionTextView;
    @BindView(R.id.channelLogo) ImageView channelLogo;
    @BindView(R.id.channelNameTextView) TextView channelNameTextView;
    Long lastShow = 0L;

    private Handler mainHandler;
    private MediaSourceEventListener eventLogger;
    private Handler hideControlsHandler = new Handler();
    private Runnable hideControls = new Runnable() {
        @Override
        public void run() {
            long timeSinceShow = System.currentTimeMillis() - lastShow;
            if(timeSinceShow > 1000 * 5) {
                if (channelInfoLayout != null && programInfoLayout != null) {
                    channelInfoLayout.setVisibility(GONE);
                    programInfoLayout.setVisibility(GONE);
                }
            }
        }
    };

    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;
    private boolean inErrorState;
    private TrackGroupArray lastSeenTrackGroupArray;

    private int resumeWindow;
    private long resumePosition;

    ArrayList<EPGLine> channels;
    int currentRow;

    Intent resultIntent;
    int retryCount = 0;
    CustomDialog retryDialog;
    boolean showingControls = false;
    String url;
    boolean urlUsed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_player);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        currentRow = getIntent().getIntExtra(CHANNEL_INDEX_EXTRA, 0);
        channels = getIntent().getParcelableArrayListExtra(CHANNEL_LIST_EXTRA);
        url = getIntent().getStringExtra(CHANNEL_URL_EXTRA);



        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();

        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.setControllerAutoShow(false);
        simpleExoPlayerView.requestFocus();
        channelsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }




    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        clearResumePosition();
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {

        if(hideControlsHandler != null) {
            hideControlsHandler.removeCallbacks(hideControls);
            hideControlsHandler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializePlayer();
        } else {
            showToast(R.string.storage_permission_denied);
            finish();
        }
    }

// Activity input

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // See whether the player view wants to handle media or DPAD keys events.
        boolean result = false;
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (event.getKeyCode())
            {
                case KEYCODE_DPAD_UP:
                    currentRow--;
                    retryCount = 0;
                    if(currentRow<0)
                    {
                        currentRow = channels.size()-1;
                    }
                    releasePlayer();
                    initializePlayer();
                    return true;
                case KEYCODE_DPAD_DOWN:
                    currentRow++;
                    retryCount = 0;
                    if(currentRow>=channels.size())
                    {
                        currentRow = 0;
                    }
                    releasePlayer();
                    initializePlayer();
                    return true;
                case KEYCODE_DPAD_CENTER:
                case KEYCODE_ENTER:
                    showInformationControls();
                    return  true;
                case  KEYCODE_MENU:
                    showChannelsMenu();
                    break;
                default:
                    return simpleExoPlayerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
            }
        }
        return  super.dispatchKeyEvent(event);

    }


    private void showChannelsMenu()
    {
        ArrayList<Card> channelList = new ArrayList<>();

        for (EPGLine channel: channels) {
            channelList.add(new Card(channel.getId(), channel.getName(), 0, 0, Card.Type.DEFAULT));
        }

        CardsAdapter adapter = new CardsAdapter(LivePlayerActivity.this, 0, channelList);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.gambaRed)
                .setTitle(R.string.select_category)
                .setIcon(R.drawable.live_on_icon)
                .setMessage(R.string.categories)
                .setItems(adapter, (position, item) -> {
                    if(item != null) {
                        EPGLine currentChannel = channels.get(currentRow);
                        if (currentChannel.getId() != item.getId()) {
                            currentRow = position;
                            retryCount = 0;
                            releasePlayer();
                            initializePlayer();
                        }
                    }
                })
                .show();

    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(CHANNEL_INDEX_EXTRA, currentRow);
        setResult(RESULT_OK, resultIntent);
        finish();
    //    super.onBackPressed();
    }

    private void showInformationControls()
    {
    //    if(!showingControls) {
            hideControlsHandler.removeCallbacks(hideControls);
            hideControlsHandler.postDelayed(hideControls, 6000);
            lastShow = System.currentTimeMillis() ;
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat justHour = new SimpleDateFormat(getString(R.string.hh_mm), Locale.US);
            channelInfoLayout.setVisibility(View.VISIBLE);
            EPGLine currentChannel = channels.get(currentRow);
            dataManager.getCurrentProgram(currentChannel.getId())
                    .onErrorReturn(error ->
                    {
                        return new ChannelProgram(this.getResources().getString(R.string.no_info), currentDate.getTime(), currentDate.getTime());
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(channelProgram -> {
                        if (channelProgram != null) {
                            if (channelProgram.getStartDate().getTime() - currentDate.getTime().getTime() > TWO_MINUTES) {
                                channelProgram = new ChannelProgram(LivePlayerActivity.this.getResources().getString(R.string.no_info), currentDate.getTime(), channelProgram.getStartDate());
                            }
                            if (programDescriptionTextView != null) {

                                programDescriptionTextView.setText(channelProgram.getDescription());
                                programTitleTextView.setText(channelProgram.getTitle());
                                programStartTimeTextView.setText(justHour.format(channelProgram.getStartDate()));
                                programEndTimeTextView.setText(justHour.format(channelProgram.getEndDate()));
                                programInfoLayout.setVisibility(View.VISIBLE);
                            }

                        }
                    });
    /*    }
        else
        {
            programInfoLayout.setVisibility(View.GONE);
            channelInfoLayout.setVisibility(View.GONE);

        }
        showingControls = !showingControls;*/



    }



// Internal methods

    private void initializePlayer() {
        try {
//            loadingVideoProgress.setVisibility(View.VISIBLE);

            Intent intent = getIntent();

            boolean needNewPlayer = player == null;
            if (needNewPlayer) {
                TrackSelection.Factory adaptiveTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
                trackSelectionHelper = new TrackSelectionHelper(trackSelector, adaptiveTrackSelectionFactory);
                lastSeenTrackGroupArray = null;
                eventLogger = new MediaSourceEventListener() {
                    @Override
                    public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

                    }

                    @Override
                    public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

                    }

                    @Override
                    public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
                        hideControlsHandler.removeCallbacks(hideControls);
                        hideControlsHandler.postDelayed(hideControls, 6000);
                        retryCount = 0;
                        if(retryDialog != null)
                        {
                            retryDialog.hide();
                            retryDialog = null;
                        }
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

            player.addListener(new PlayerEventListener());
            if (simpleExoPlayerView != null) {
                simpleExoPlayerView.setPlayer(player);
            } else {
                try {
                    simpleExoPlayerView = findViewById(R.id.player_view);
                    simpleExoPlayerView.setPlayer(player);
                } catch (Exception ex) {
                    new CustomDialog(LivePlayerActivity.this, true, getString(R.string.didnt_see_that_coming), getString(R.string.unexpected_state), getString(R.string.ok), EMPTY_STRING, true);
                    finish();
                }
            }

            EPGLine currentChannel = channels.get(currentRow);
            channelNameTextView.setText(currentChannel.getName());
            Picasso.get().load(currentChannel.getThumbnail()).into(channelLogo);
            showInformationControls();
            if(!urlUsed && url != null && !url.equals(EMPTY_STRING))
            {
                urlUsed = true;
                MediaSource mediaSource = buildMediaSource(Uri.parse(url), "");
                if (player != null && mediaSource != null) {
                    player.prepare(mediaSource, true, true);
                } else {
                    releasePlayer();
                    initializePlayer();
                }
            }
            else {

                dataManager.getLiveUrl(currentChannel.getId())
                        .onErrorReturn(error ->
                                EMPTY_STRING)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(url -> {
                            MediaSource mediaSource = buildMediaSource(Uri.parse(url), "");
                            if (player != null && mediaSource != null) {
                                player.prepare(mediaSource, true, true);
                            } else {
                                releasePlayer();
                                initializePlayer();
                            }

                        });
            }

            //MediaSource mediaSource =  buildMediaSource(Uri.parse("http://66.70.183.41:8080/hls/prueba.m3u8?md5=C3V7oKvvRIX3iBqGqkzhhA&expires=1532134711"), "");    //Uri.parse(currentChannel.getUrl()), "");

            player.setPlayWhenReady(true);
        }
        catch (Exception ignore)
        {

        }


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
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
            trackSelectionHelper = null;
            eventLogger = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = Math.max(0, player.getContentPosition());
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((GambaChannelApp) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? bandwidthMeter : null);
    }


    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((GambaChannelApp) getApplication())
                .buildHttpDataSourceFactory(useBandwidthMeter ? bandwidthMeter : null);
    }


    private void showControls() {
        channelsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @Override
    public void onVisibilityChange(int visibility) {

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
                hideControlsHandler.removeCallbacks(hideControls);
                hideControlsHandler.postDelayed(hideControls, 6000);
                retryCount = 0;
                if(retryDialog != null)
                {
                    retryDialog.hide();
                    retryDialog = null;
                }
            }
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
            if (inErrorState) {
                // This will only occur if the user has performed a seek whilst in the error state. Update
                // the resume position so that if the user then retries, playback will resume from the
                // position to which they seeked.
                updateResumePosition();
            }
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
            if (errorString != null) {
                showToast(errorString);
            }
            inErrorState = true;
            if (isBehindLiveWindow(e)) {
                clearResumePosition();
                initializePlayer();
            } else {
                if(retryCount >= 7) {
                    new CustomDialog(LivePlayerActivity.this, true, getString(R.string.content_not_available), getString(R.string.channel_not_available), getString(R.string.ok), EMPTY_STRING, true);
                    if(retryDialog != null)
                    {
                        retryDialog.hide();
                        retryDialog = null;
                        retryCount  = 0;
                    }
                }
                else
                {
                    retryCount++;
                    if(retryDialog != null)
                    {
                        retryDialog.hide();
                    }
                    retryDialog = new CustomDialog(LivePlayerActivity.this, true, getString(R.string.server_connection_error), String.format(Locale.US,getString(R.string.try_number), retryCount) , EMPTY_STRING, EMPTY_STRING, true);

                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    releasePlayer();
                    initializePlayer();
                }
            }
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            if (trackGroups != lastSeenTrackGroupArray) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_audio);
                    }
                }
                lastSeenTrackGroupArray = trackGroups;
            }
        }

    }

}
