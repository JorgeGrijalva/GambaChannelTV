package com.arca.equipfix.gambachanneltv.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.github.angads25.toggle.widget.LabeledSwitch;


import java.util.ArrayList;

/**
 * Created by gabri on 7/20/2018.
 */

public class ProfileAdapter extends BaseAdapter {


    private ArrayList<Profile> profiles;
    private Context mContext;

    private int mCurrentIndex;


    public ProfileAdapter(ArrayList<Profile>  data, Context context) {
        profiles = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public Profile getItem(int position) {
        return profiles.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void decCurrentIndex()
    {
        if(profiles!=null)
        {
            if(mCurrentIndex>0)
            {
                mCurrentIndex--;
                notifyDataSetChanged();

            }
        }
    }


    public void setCurrentIndex(int currentIndex)
    {
        if(profiles!=null)
        {
            if(currentIndex>=0 && currentIndex<profiles.size() )
            {
                mCurrentIndex = currentIndex;
                notifyDataSetChanged();

            }
        }
    }


    public void ascCurrentItem()
    {
        if(profiles != null)
        {
            if(mCurrentIndex<profiles.size()-1)
            {
                mCurrentIndex++;
                notifyDataSetChanged();
            }
        }
    }
    public int getCurrentIndex()
    {
        return mCurrentIndex;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void insertItem(Profile profile)
    {
        profiles.add(profile);
        notifyDataSetChanged();
    }

    public ArrayList<Profile> getItems()
    {
        return profiles;
    }

    public void addItems(ArrayList<Profile> items)
    {
        if(items.size()>0) {
            profiles.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addItem(Profile item)
    {
        profiles.add(item);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout result;
        final Profile profile = getItem(position);
        result = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.profile_item_new, parent, false);

        TextView profileNameTextView = result.findViewById(R.id.profileNameTextView);
        profileNameTextView.setText(profile.getName());
        LabeledSwitch adultsEnabledSwitch = result.findViewById(R.id.adultsEnabledSwitch);
        LabeledSwitch passwordProtectedSwitch = result.findViewById(R.id.passwordProtectedSwitch);

        if(profile.getId()>0) {
            adultsEnabledSwitch.setOn(profile.getEnableAdults());
            passwordProtectedSwitch.setOn(profile.getPasswordProtected());
        }
        else
        {
            TextView adultsEnabledTitle = result.findViewById(R.id.adultsEnabledTitle);
            adultsEnabledTitle.setVisibility(View.GONE);
            TextView passwordProtectedTitle = result.findViewById(R.id.passwordProtectedTitle);
            passwordProtectedTitle.setVisibility(View.GONE);
            adultsEnabledSwitch.setVisibility(View.GONE);
            passwordProtectedSwitch.setVisibility(View.GONE);
        }
        return result;
    }
}



