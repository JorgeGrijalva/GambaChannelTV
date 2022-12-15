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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arca.equipfix.gambachanneltv.GambaChannelApp;
import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemUrls;
import com.arca.equipfix.gambachanneltv.data.network.model.PlayEpisode;
import com.arca.equipfix.gambachanneltv.ui.adapters.PreviewAdapter;
import com.arca.equipfix.gambachanneltv.ui.local_components.GambaExoPlayer;
import com.arca.equipfix.gambachanneltv.ui.local_components.TrackSelectionHelper;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
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
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.DURATION_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_ID_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA_EPISODE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA_SEASON;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.ITEM_TYPE_EXTRA_SERIE;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.PLAY_CONTINUOUS_EXTRA;

public class EpisodesPlayerActivity extends AuthenticateBaseActivity implements View.OnClickListener,
        PlaybackControlView.VisibilityListener {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, EpisodesPlayerActivity.class);
    }

    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";


    @Inject
    DefaultBandwidthMeter bandwidthMeter;

    @Inject
    TrackSelection.Factory fixedFactory;


    @BindView(R.id.player_view)
    GambaExoPlayer simpleExoPlayerView;
    @BindView(R.id.controls_root)
    LinearLayout debugRootView;
    @BindView(R.id.retry_button)
    MaterialFancyButton retryButton;
    @BindView(R.id.root) View rootView;
    @BindView(R.id.audio_button) MaterialFancyButton audioButton;
    @BindView(R.id.subtitles_button) MaterialFancyButton subtitlesButton;
    @BindView(R.id.previeRecyclerView)
    RecyclerView previeRecyclerView;
    @BindView(R.id.seasonNameTextView)
    TextView seasonNameTextView;
    @BindView(R.id.episodeTitleTextView)
    TextView episodeTitleTextView;
    @BindView(R.id.episodeInfoLayout)
    LinearLayout episodeInfoLayout;
    @BindView(R.id.nextEpisodeTimeTextView)
    TextView nextEpisodeTimeTextView;

    private Handler mainHandler;
    private MediaSourceEventListener eventLogger;

    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;
    private boolean inErrorState;
    private TrackGroupArray lastSeenTrackGroupArray;

    private boolean shouldAutoPlay;
    private int resumeWindow;
    private  long resumePosition;



    private int subtitlesGroupIndex = -1;
    private int audioGroupIndex = -1;
    private boolean subtitlesSet = false;
    private boolean audioSet = false;
    private String[] subtitlesLanguages;
    private String[] audioLanguages;
    String audioLanguage = EMPTY_STRING;
    String subtitlesLanguage  = EMPTY_STRING;
    Boolean subtitlesEnabled = false;
    boolean wifiRestarted = false;
    private  int itemType;
    private int episodeId = 0;
    private int seasonId = 0;
    private  int serieId = 0;

    private Handler nextEpisodeHandler = new Handler() ;
    private  Runnable showNextEpisodeTime = new Runnable() {
        @Override
        public void run() {
            if (player != null)
            {
                nextEpisodeHandler.removeCallbacks(showNextEpisodeTime);
                long timeMs = duration - player.getCurrentPosition();
                long seconds = timeMs / 1000;
                if( seconds < 10 )
                {
                    episodeInfoLayout.setVisibility(View.VISIBLE);
                    nextEpisodeTimeTextView.setText( String.format(Locale.US, getString(R.string.next_episode_seconds), seconds));

                    nextEpisodeHandler.postDelayed(showNextEpisodeTime, 1000);
                }
                nextEpisodeHandler.postDelayed(showNextEpisodeTime, 5000);
            }
        }
    };
    private  boolean positionSet = false;
    Intent resultIntent;
    private  long duration = 0;
    private ItemUrls itemUrl;
    private PlayEpisode playEpisode;
    private boolean playContinuous = true;
    private  boolean firstEpisode = true;


// Activity lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes_player);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        itemType = getIntent().getIntExtra(ITEM_TYPE_EXTRA, 0);
        switch (itemType)
        {
            case ITEM_TYPE_EXTRA_SERIE:
                serieId = getIntent().getIntExtra(ITEM_ID_EXTRA, 0);
            break;
            case ITEM_TYPE_EXTRA_SEASON:
                seasonId = getIntent().getIntExtra(ITEM_ID_EXTRA, 0);
                break;
            case ITEM_TYPE_EXTRA_EPISODE:
                episodeId = getIntent().getIntExtra(ITEM_ID_EXTRA, 0);
                playContinuous = getIntent().getBooleanExtra(PLAY_CONTINUOUS_EXTRA, true);
                break;
        }

        subtitlesEnabled = dataManager.getSubtitlesEnabled();
        if(subtitlesEnabled) {
            subtitlesLanguage = dataManager.getSubtitlesLanguage();
        }
        audioLanguage = dataManager.getAudioLanguage();


        shouldAutoPlay = true;
        clearResumePosition();
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();

        rootView.setOnClickListener(this);
        retryButton.setOnClickListener(this);
        audioButton.setOnClickListener(this);
        subtitlesButton.setOnClickListener(this);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();
        previeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        shouldAutoPlay = true;
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
        if(player !=null)
        {
            releasePlayer();
        }

        if(nextEpisodeHandler != null )
        {
            nextEpisodeHandler.removeCallbacks(showNextEpisodeTime );
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
        return simpleExoPlayerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }


    @Override
    public void onClick(View view) {
        if (view == retryButton) {
            initializePlayer();
        } else if (view.getParent() == debugRootView) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                trackSelectionHelper.showSelectionDialog(this, ((MaterialFancyButton) view).getText(),
                        trackSelector.getCurrentMappedTrackInfo(), (int) view.getTag());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(player != null && player.getDuration()>0) {
            if(playEpisode != null) {
                dataManager.setLastPositionEpisode(playEpisode.getId(), player.getCurrentPosition());
                playEpisode.setLastPosition(player.getCurrentPosition());
            }
        }

        super.onBackPressed();
    }

    @Override
    public void onVisibilityChange(int visibility) {
        debugRootView.setVisibility(visibility);
        episodeInfoLayout.setVisibility(visibility);
    }

// Internal methods

    private void initializePlayer() {
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
        player.addListener(new EpisodesPlayerActivity.PlayerEventListener());
//        player.addListener(eventLogger);
  //      player.addMetadataOutput(eventLogger);
  //      player.setAudioDebugListener(eventLogger);
    //    player.setVideoDebugListener(eventLogger);


        simpleExoPlayerView.setPlayer(player);

        simpleExoPlayerView.setOnScrubListener(new TimeBar.OnScrubListener() {

            @Override
            public void onScrubStart(TimeBar timeBar, long position) {

            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                try {
                    long currentIndex = (position / 1000 / 13)+1;
                    List<String> images = new ArrayList<>();

                    for (int i = -4; i<4; i++)
                    {
                        if( (i+currentIndex) >0)
                        {
                            String imageUrl =  String.format(String.format("%s%%03d.jpg", itemUrl.getThumbnails()), currentIndex+i);
                            images.add(imageUrl);
                        }
                        PreviewAdapter adapter = new PreviewAdapter(images);
                        previeRecyclerView.setAdapter(adapter);
                        previeRecyclerView.setVisibility(View.VISIBLE);

                    }
                } catch (Exception ex) {

                }

            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                previeRecyclerView.setVisibility(View.GONE);

            }
        });

        player.setPlayWhenReady(shouldAutoPlay);


        if(this.firstEpisode && this.episodeId>0)
        {
            this.firstEpisode = false;
            dataManager.getEpisodeInformation(episodeId)
                    .onErrorReturn(error ->
                            new PlayEpisode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(playEpisode -> {
                        clearResumePosition();

                        this.playEpisode = playEpisode;
                        if (playEpisode == null || playEpisode.getItemUrls() == null) {
                            showToast(getString(R.string.content_not_available));
                            finish();
                        }
                        episodeTitleTextView.setText(playEpisode.getTitle());
                        seasonNameTextView.setText(playEpisode.getSeasonName());
                        nextEpisodeTimeTextView.setText(EMPTY_STRING);

                        episodeId = playEpisode.getId();
                        itemUrl = playEpisode.getItemUrls();
                        MediaSource mediaSource = buildMediaSource(Uri.parse(playEpisode.getItemUrls().getUrl()), "");
                        boolean haveResumePosition = resumePosition > 0;
                        if (haveResumePosition) {
                            player.seekTo(resumeWindow, resumePosition);
                        }
                        player.prepare(mediaSource, !haveResumePosition, false);
                        inErrorState = false;
                        updateButtonVisibilities();
                    });
        }
        else {
            dataManager.getNextEpisodeInformation(serieId, seasonId, episodeId)
                    .onErrorReturn(error ->
                            new PlayEpisode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(playEpisode -> {
                        clearResumePosition();
                        nextEpisodeTimeTextView.setText(EMPTY_STRING);
                        this.firstEpisode = false;
                        this.playEpisode = playEpisode;
                        if (playEpisode == null || playEpisode.getItemUrls() == null) {
                            showToast(getString(R.string.no_more_episodes));
                            finish();
                        }

                        if (seasonId > 0) {
                            if (seasonId != playEpisode.getSeasonId()) {
                                finish();
                                return;
                            }
                        }


                        episodeTitleTextView.setText(playEpisode.getTitle());
                        seasonNameTextView.setText(playEpisode.getSeasonName());

                        episodeId = playEpisode.getId();
                        itemUrl = playEpisode.getItemUrls();
                        MediaSource mediaSource = buildMediaSource(Uri.parse(playEpisode.getItemUrls().getUrl()), "");
                        boolean haveResumePosition = resumePosition > 0;
                        if (haveResumePosition) {
                            player.seekTo(resumeWindow, resumePosition);
                        }
                        player.prepare(mediaSource, !haveResumePosition, false);
                        inErrorState = false;
                        updateButtonVisibilities();
                    });
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
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mainHandler, null);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
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


    private void updateButtonVisibilities() {
        audioButton.setVisibility(View.GONE);
        subtitlesButton.setVisibility(View.GONE);


        if (player == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }

        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {


                switch (player.getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        audioGroupIndex = i;
                        audioLanguages = new String[trackGroups.length];

                        if(trackGroups.length <=1)
                        {
                            continue;
                        }
                        audioButton.setTag(i);

                        audioButton.setVisibility(View.VISIBLE);
                        for (int j = 0; j< trackGroups.length; j++)
                        {
                            audioLanguages[j] = trackGroups.get(j).getFormat(0).language;
                        }


                        break;
                    case C.TRACK_TYPE_VIDEO:
                        continue;
                    case C.TRACK_TYPE_TEXT:
                        subtitlesGroupIndex = i;
                        subtitlesLanguages = new String[trackGroups.length];
                        if(trackGroups.length ==0)
                        {
                            continue;
                        }

                        subtitlesButton.setVisibility(View.VISIBLE);
                        subtitlesButton.setTag(i);
                        for (int j = 0; j< trackGroups.length; j++)
                        {
                            subtitlesLanguages[j] = trackGroups.get(j).getFormat(0).language;
                        }

                        break;
                    default:
                        continue;
                }
            }
        }

        setSubtitles(mappedTrackInfo);
        setAudio(mappedTrackInfo);


    }


    private void setSubtitles( MappingTrackSelector.MappedTrackInfo mappedTrackInfo)
    {
        if(subtitlesSet)
        {
            return;
        }
        else
        {
            subtitlesSet = true;
        }

        if(subtitlesLanguages== null || subtitlesLanguage.length()<1)
        {
            return;
        }

        if(subtitlesGroupIndex>-1) {
            if (subtitlesEnabled) {
                int trackIndex = -1;

                for(int i = 0; i< subtitlesLanguages.length; i++)
                {
                    if(subtitlesLanguages[i].equals(subtitlesLanguage))
                    {
                        trackIndex = i;
                    }
                }
                if(trackIndex>-1) {

                    TrackGroupArray trackGroups = trackSelector.getCurrentMappedTrackInfo().getTrackGroups(subtitlesGroupIndex);

                    DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(subtitlesGroupIndex, trackIndex);
                    trackSelector.setSelectionOverride(subtitlesGroupIndex, trackGroups, override);
                }
                else
                {
                    trackSelector.setRendererDisabled(subtitlesGroupIndex, true);
                }

            } else {
                trackSelector.setRendererDisabled(subtitlesGroupIndex, true);
            }
        }
    }

    private void setAudio( MappingTrackSelector.MappedTrackInfo mappedTrackInfo)
    {
        if(! audioSet) {
            audioSet = true;
            if (audioGroupIndex > -1 && audioLanguages.length > 1) {
                int groupIndex = 0;

                for (int i = 0; i < audioLanguages.length; i++) {
                    if (audioLanguages[i].equals(audioLanguage)) {
                        groupIndex = i;
                    }
                }

                trackSelector.setRendererDisabled(audioGroupIndex, false);
                TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(audioGroupIndex);
                DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(audioGroupIndex, groupIndex);
                trackSelector.setSelectionOverride(audioGroupIndex, trackGroups, override);
            }
        }
    }

    private void showControls() {
        debugRootView.setVisibility(View.VISIBLE);
        episodeInfoLayout.setVisibility(View.VISIBLE);
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



    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
                dataManager.setLastPositionEpisode(playEpisode.getId(), duration);

                if(EpisodesPlayerActivity.this.playContinuous)
                {
                    releasePlayer();
                    initializePlayer();
                }
                else
                {
                    finish();
                }

            }
            else {
                updateButtonVisibilities();
            }

            if(playbackState == Player.STATE_READY )
            {
                duration = player.getDuration();

                nextEpisodeHandler.postDelayed(showNextEpisodeTime,30000 );

                if(!positionSet) {
                    player.seekTo(playEpisode.getLastPosition());
                    positionSet = true;
                }
                resultIntent = new Intent();
                resultIntent.putExtra(DURATION_EXTRA, player.getDuration());
                dataManager.postEpisodePlayed(playEpisode.getId(), duration);

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
                if(!wifiRestarted) {
//                    restartWifi(PlayerActivity.this);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    wifiRestarted = true;
                    updateResumePosition();
                    initializePlayer();

                    showControls();

                }
                else
                {
                    showToast(getString(R.string.content_not_available));
                    finish();
                    //updateButtonVisibilities();
                }
            }
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            updateButtonVisibilities();
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


