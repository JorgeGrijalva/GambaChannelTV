package com.arca.equipfix.gambachanneltv.ui.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arca.equipfix.common.Models.MenuItem;
import com.arca.equipfix.gambachanneltv.R;
import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by gabri on 6/7/2018.
 */

public class MenuCardPagerAdapter extends RecyclerView.Adapter<MenuCardPagerAdapter.ViewHolder> {

    private RecyclerView parentRecycler;
    private List<MenuItem> mData;
    private  int selected;
    private  int previousSelected=-1;
    private  boolean canMove = true;
    private  boolean firstLoad = true;

    public MenuCardPagerAdapter(List<MenuItem> data)
    {
        mData = data;

    }


    private final PublishSubject<MenuItem> onClickSubject = PublishSubject.create();

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.menu_item_fragment, parent, false);
        return new ViewHolder(v);
    }

    public MenuItem getItemAt(int index)
    {
        if(mData == null) return null;

        if(index>=0 && index<mData.size() )
        {
            return mData.get(index);
        }

        return  null;
    }

    public void addCardItem(MenuItem item)
    {
        mData.add(item);
        notifyItemInserted(mData.size()-1);
    }

    public void setSelected(int selected)
    {
        if(!canMove)
        {
            return;
        }
        if(mData == null)
        {
            return;
        }
        Log.d("setSelected", "Selected: "+this.selected+"; New Selected: "+selected);
        if(selected>=0 && selected<mData.size()) {
            this.previousSelected = this.selected;
            this.selected = selected;
        }
    }

    public  MenuItem getItemSelected()
    {
        Log.d("getItem", "Selected: "+this.selected);

        if(mData == null || selected<0 || selected>mData.size())
        {
            return null;
        }
        return mData.get(selected);
    }

    public  MenuItem getItemPreviousSelected()
    {
        Log.d("getItemPrevious", "Selected: "+this.previousSelected);

        if(mData == null || previousSelected<0 || previousSelected>mData.size())
        {
            return null;
        }
        return mData.get(previousSelected);
    }

    public  int getSelected()
    {
        return  this.selected;
    }

    public Observable<MenuItem> getPositionClicks(){
        return onClickSubject;
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        final MenuItem item = mData.get(position);
        holder.titleTextView.setText(item.getTitle());
        if(firstLoad)
        {
            firstLoad = false;
            holder.showText();
        }
        else {
            Glide.with(holder.itemView.getContext()).load(item.getImageOff()).into(holder.categoryImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(item);
                setSelected(position);
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        ImageView categoryImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            categoryImageView = itemView.findViewById(R.id.categoryImageView);
            itemView.findViewById(R.id.container).setOnClickListener(this);
        }

        public void showText() {
            MenuItem item = getItemSelected();
            if(item != null) {
                Glide.with(categoryImageView.getContext()).load(item.getImageOn()).into(categoryImageView);
                int parentHeight = ((View) categoryImageView.getParent()).getHeight();
                float scale = (parentHeight - titleTextView.getHeight()) / (float) categoryImageView.getHeight();
                categoryImageView.setPivotX(categoryImageView.getWidth() * 0.5f);
                categoryImageView.setPivotY(0);
                categoryImageView.animate().scaleX(scale)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                titleTextView.setVisibility(View.VISIBLE);
                                canMove = true;
                                //          categoryOnImageView.setColorFilter(Color.BLACK);

                            }
                        })
                        .scaleY(scale).setDuration(200)
                        .start();
            }
        }

        public void hideText() {
            canMove = false;
            MenuItem item = getItemPreviousSelected();
            if(item != null) {
                Glide.with(categoryImageView.getContext()).load(item.getImageOff()).into(categoryImageView);
                titleTextView.setVisibility(View.INVISIBLE);
                categoryImageView.animate().scaleX(1f).scaleY(1f)
                        .setDuration(200)
                        .start();
            }
        }

        @Override
        public void onClick(View v) {
            parentRecycler.smoothScrollToPosition(getAdapterPosition());
        }
    }


}

