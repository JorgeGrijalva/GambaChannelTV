package com.arca.equipfix.gambachanneltv.ui.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arca.equipfix.common.Models.TopItem;
import com.arca.equipfix.gambachanneltv.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabri on 6/7/2018.
 */

public class TopCardAdapter extends RecyclerView.Adapter<TopCardAdapter.TopViewHolder> {

    private List<TopItem> mData;
    private Context mContext;

    private  int selected = -1;
    private int lastSelected = -1;



    public TopCardAdapter(Context context)
    {
        mData = new ArrayList<>();
        mContext = context;
    }

    public void addCardItem(TopItem item)
    {
        mData.add(item);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if(mData != null) {
            return mData.size();
        }

        return 0;
    }

    public void setSelected(int selected)
    {
        this.lastSelected = this.selected;
        this.selected = selected;
        notifyDataSetChanged();
    }


    @Override
    public TopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.top_item_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(TopViewHolder holder, final int position) {
        final TopItem item = mData.get(position);
        holder.titleTextView.setText(item.getTitle());
        Glide.with(mContext).load(item.getImage()).into(holder.coverImageView);



        float scale = 1f;
        int newColor = Color.GRAY;
        if(position ==selected )
        {
            newColor = Color.WHITE;
            scale = 1.1f;
        }

        if(position==selected || position ==lastSelected) {
            ObjectAnimator colorAnim = ObjectAnimator.ofInt(holder.titleTextView, "textColor",
                    holder.titleTextView.getCurrentTextColor(), newColor).setDuration(50);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.start();
            holder.coverImageView.animate().scaleX(scale).setDuration(40).start();
            holder.coverImageView.animate().scaleY(scale).setDuration(40).start();
            holder.titleTextView.animate().scaleX(scale).setDuration(40).start();
            holder.titleTextView.animate().scaleY(scale).setDuration(40).start();

        }

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }




    public static class TopViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView coverImageView;

        public TopViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            coverImageView = itemView.findViewById(R.id.coverImageView);
        }
    }
}


