package com.arca.equipfix.gambachanneltv.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.network.model.Season;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

/**
 * Created by gabri on 7/11/2018.
 */

public class SeasonAdapter extends BaseAdapter {


    private ArrayList<Season> data;
    private Context context;

    private MaterialFancyButton playContinueButton;

    View.OnClickListener playContinueClick;

    private int currentSelected = -1;


    public SeasonAdapter(ArrayList<Season>  data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Season getItem(int position) {
        return data.get(position);
    }

    public ArrayList<Season> getItems()
    {
        return data;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void setPlayContinueClick(View.OnClickListener onClickListener)
    {
        playContinueClick = onClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setCurrentSelected(int currentSelected)
    {
        this.currentSelected = currentSelected;
        notifyDataSetChanged();
    }

    public int getCurrentSelected()
    {
        return this.currentSelected;
    }


    public void insertItem(Season seasonInformation)
    {
        data.add(seasonInformation);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<Season> seasons)
    {
        data.addAll(seasons);
        notifyDataSetChanged();
    }


    public void executeSelectedButton()
    {
        if(playContinueClick != null)
        {
            playContinueClick.onClick(null);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View result;
        final Season seasonInformation = getItem(position);
        if(currentSelected == position && seasonInformation.getId()!=-1)
        {
            result =  LayoutInflater.from(context).inflate(R.layout.season_item_focused, parent, false);
            playContinueButton = result.findViewById(R.id.playButton);
            if(seasonInformation.getLastEpisode()>0)
            {
                playContinueButton.setText(context.getString(R.string.continue_playing));
            }
            playContinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(playContinueClick != null)
                    {
                        playContinueClick.onClick(view);
                    }
                }
            });

        }
        else
        {
            result = LayoutInflater.from(context).inflate(R.layout.season_item, parent, false);
        }

        TextView title = result.findViewById(R.id.titleTextView);
        title.setText(seasonInformation.getName());

        return result;
    }
}

