package com.arca.equipfix.gambachanneltv.ui.local_components;

import android.content.Context;

import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

/**
 * Created by gabri on 6/20/2018.
 */

public class GambaExoPlayer extends GambaPlayerView {


    public GambaExoPlayer(Context context) {
        super(context);
    }

    public GambaExoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GambaExoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Switches the view targeted by a given {@link SimpleExoPlayer}.
     *
     * @param player The player whose target view is being switched.
     * @param oldPlayerView The old view to detach from the player.
     * @param newPlayerView The new view to attach to the player.
     */
    public static void switchTargetView(
            @NonNull SimpleExoPlayer player,
            @Nullable SimpleExoPlayerView oldPlayerView,
            @Nullable SimpleExoPlayerView newPlayerView) {
        PlayerView.switchTargetView(player, oldPlayerView, newPlayerView);
    }
}
