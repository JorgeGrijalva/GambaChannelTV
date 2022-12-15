package com.arca.equipfix.gambachanneltv.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.arca.equipfix.gambachanneltv.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gabri on 7/2/2018.
 */

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

    private RecyclerView parentRecycler;
    private List<String> mData;
    private  int selected;


    public PreviewAdapter(List<String> data)
    {
        mData = data;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.preview_item, parent, false);
        return new PreviewAdapter.ViewHolder(v);


    }


    public  int getSelected()
    {
        return  this.selected;
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
    public void onBindViewHolder(final PreviewAdapter.ViewHolder holder, final int position) {

        String item = mData.get(position);
        Glide.with(holder.itemView.getContext()).load(item).into(holder.previewImageView);

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView previewImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            previewImageView = itemView.findViewById(R.id.previewImageView);
        }
    }


}

