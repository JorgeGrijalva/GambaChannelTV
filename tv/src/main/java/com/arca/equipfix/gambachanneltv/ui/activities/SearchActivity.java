package com.arca.equipfix.gambachanneltv.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.ui.fragments.SearchActivityFragment;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.SEARCH_STRING_EXTRA;


public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchActivityFragment fragment = new SearchActivityFragment();
        fragment.setOnSearch(search -> {
            Intent intent1 =new Intent();
            intent1.putExtra(SEARCH_STRING_EXTRA,search);
            setResult(RESULT_OK, intent1);
            finish();
        });
        getFragmentManager().beginTransaction().add(R.id.directContentView, fragment).commit();
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
