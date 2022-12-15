package com.arca.equipfix.gambachanneltv.ui.fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.util.Log;

import androidx.leanback.app.SearchFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends SearchFragment implements SearchFragment.SearchResultProvider {

private ArrayObjectAdapter mRowsAdapter;

public interface OnSearch
{
    void onSearch(String search);
}
    private static  String TAG = "SearchFragment";

    private OnSearch onSearch;

    public void setOnSearch(OnSearch onSearch)
    {
        this.onSearch = onSearch;
    }

    public SearchActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSpeechRecognitionCallback(() -> {
                    try {
                        //startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, "Cannot find activity for speech recognizer", e);
                    }
                });

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSearchResultProvider(this);
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return new ObjectAdapter() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public Object get(int position) {
                return null;
            }
        };
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query != EMPTY_STRING) {
            if(onSearch != null)
            {
                onSearch.onSearch(query);
            }
        }
        return false;
    }

}
