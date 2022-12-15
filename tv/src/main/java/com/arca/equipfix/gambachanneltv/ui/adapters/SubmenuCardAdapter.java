package com.arca.equipfix.gambachanneltv.ui.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arca.equipfix.common.Models.SubmenuItem;
import com.arca.equipfix.gambachanneltv.R;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by gabri on 6/13/2018.
 */

public class SubmenuCardAdapter extends RecyclerView.Adapter<SubmenuCardAdapter.ViewHolder> {

    private RecyclerView parentRecycler;
    private List<SubmenuItem> mData;
    private  int selected;
    private  int previousSelected=-1;
    private  boolean canMove = true;
    private  boolean firstLoad = true;

    public SubmenuCardAdapter(List<SubmenuItem> data)
    {
        mData = data;

    }


    private final PublishSubject<SubmenuItem> onClickSubject = PublishSubject.create();

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @Override
    public SubmenuCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.submenu_item_fragment, parent, false);
        return new SubmenuCardAdapter.ViewHolder(v);
    }

    public SubmenuItem getItemAt(int index)
    {
        if(mData == null) return null;

        if(index>=0 && index<mData.size() )
        {
            return mData.get(index);
        }

        return  null;
    }

    public void addCardItem(SubmenuItem item)
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

    public  SubmenuItem getItemSelected()
    {
        Log.d("getItem", "Selected: "+this.selected);

        if(mData == null || selected<0 || selected>mData.size())
        {
            return null;
        }
        return mData.get(selected);
    }

    public  SubmenuItem getItemPreviousSelected()
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

    public Observable<SubmenuItem> getPositionClicks(){
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
    public void onBindViewHolder(final SubmenuCardAdapter.ViewHolder holder, final int position) {



        final SubmenuItem item = mData.get(position);
        holder.titleTextView.setText(item.getTitle());
        if(firstLoad)
        {
            firstLoad = false;
            holder.showText();
        }
        else {

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
        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            itemView.findViewById(R.id.container).setOnClickListener(this);
        }

        public void showText() {
            SubmenuItem item = getItemSelected();
            if(item != null) {
                titleTextView.requestLayout();
                titleTextView.animate().scaleX(1.0f)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                canMove = true;
                            }
                        })
                        .scaleY(1.0f).setDuration(200)
                        .start();
            }
        }

        public void hideText() {
            canMove = false;
            SubmenuItem item = getItemPreviousSelected();
            if(item != null) {
                titleTextView.setVisibility(View.INVISIBLE);
                titleTextView.animate().scaleX(1f).scaleY(1f)
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

