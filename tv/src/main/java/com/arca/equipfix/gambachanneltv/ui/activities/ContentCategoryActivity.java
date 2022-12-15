package com.arca.equipfix.gambachanneltv.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.ui.fragments.ContentCategoryActivityFragment;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SUBMENU_EXTRA;

public class ContentCategoryActivity extends AuthenticateBaseActivity {

    ContentCategoryActivityFragment fragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ContentCategoryActivity.class);
    }

    Handler backPressedHandler = new Handler();
    Runnable callOnBackPressed = () -> onBackPressed();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_category);

        if(getFragmentManager().findFragmentById(R.id.directContentView) == null) {

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();

            fragment = new ContentCategoryActivityFragment();
            Bundle args = new Bundle();
            args.putInt(SUBMENU_EXTRA, extras.getInt(SUBMENU_EXTRA));
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().add(R.id.directContentView, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {

        if(fragment  != null)
        {
            fragment.cancelCalls();
            backPressedHandler.removeCallbacks(callOnBackPressed);
            backPressedHandler.postDelayed(callOnBackPressed, 150);
            if(fragment.getMakingCal())
            {
                return;
            }
        };
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        backPressedHandler.removeCallbacks(callOnBackPressed);
        super.onDestroy();
    }



}
