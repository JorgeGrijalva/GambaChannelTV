package com.arca.equipfix.gambachanneltv.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.ui.local_components.GambaControlView;
import com.dm.emotionrating.library.EmotionView;
import com.dm.emotionrating.library.GradientBackgroundView;
import com.dm.emotionrating.library.RatingView;

import butterknife.BindView;
import butterknife.ButterKnife;


import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.DURATION_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.RATING_EXTRA;
import static com.dm.emotionrating.library.Constant.MAX_RATING;
import static com.dm.emotionrating.library.Constant.ZERO_RATING;

public class RateActivity extends BaseActivity {

    @BindView(R.id.emotionView)
    EmotionView emotionView;
    @BindView(R.id.ratingView)
    RatingView ratingView;
    @BindView(R.id.submitButton)
    Button submitButton;
    @BindView(R.id.gradientBackgroundView)
    GradientBackgroundView gradientBackgroundView;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, RateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        setUnBinder(ButterKnife.bind(this));
        /*ratingView.setRatingChangeListener( (previousRating, newRating) ->
        {
            emotionView.setRating(previousRating, newRating);
            gradientBackgroundView.changeBackground(previousRating, newRating);
            submitButton.setEnabled(newRating > ZERO_RATING);
            return  Unit.INSTANCE;
         return  newRating;
        });*/

        ratingView.setRating(3);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction()== ACTION_DOWN)
        {
            if(event.getKeyCode() ==  KEYCODE_DPAD_LEFT)
            {
                if(ratingView.getCurrentRating() > ZERO_RATING)
                {
                    ratingView.setRating(ratingView.getCurrentRating()-1);
                }

            }

            if(event.getKeyCode() ==  KEYCODE_DPAD_RIGHT)
            {
                if(ratingView.getCurrentRating() < MAX_RATING)
                {
                    ratingView.setRating(ratingView.getCurrentRating()+1);
                }
            }

            if(event.getKeyCode() ==  KEYCODE_DPAD_DOWN)
            {
                submitButton.requestFocus();
            }

            if(event.getKeyCode() ==  KEYCODE_DPAD_CENTER || event.getKeyCode()== KEYCODE_ENTER)
            {

                Intent resultIntent = new Intent();
                resultIntent.putExtra(RATING_EXTRA, ratingView.getCurrentRating());
                setResult(RESULT_OK, resultIntent);
                finish();

            }

        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
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
