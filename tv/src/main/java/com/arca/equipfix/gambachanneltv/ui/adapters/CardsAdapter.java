package com.arca.equipfix.gambachanneltv.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabri on 7/16/2018.
 */

public class CardsAdapter extends ArrayAdapter<Card> {

    private static class ViewHolder {
        private TextView itemView;
    }

    public CardsAdapter(Context context, int textViewResourceId, ArrayList<Card> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.category_view, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.titleTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Card item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(item.getTitle());

        }

        return convertView;
    }


}
