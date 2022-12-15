package com.arca.equipfix.gambachanneltv.ui.adapters;


import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.network.model.ItemCover;
import com.arca.equipfix.gambachanneltv.utils.interfaces.ItemCoverSelectedListener;
import com.bumptech.glide.Glide;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class ItemCoverAdapter  extends RecyclerView.Adapter<ItemCoverAdapter.ViewHolder> {

    private RecyclerView parentRecycler;
    private List<ItemCover> mData;
    private  int selected;

    private  boolean canMove = true;
    private  boolean firstAssign = false;
    private  boolean showAssign = false;


    private ItemCoverSelectedListener itemCoverSelectedListener ;

    public ItemCoverAdapter(List<ItemCover> data)
    {
        mData = data;

    }


    private final PublishSubject<ItemCover> onClickSubject = PublishSubject.create();

    public void setItemCoverSelectedListener(ItemCoverSelectedListener listener)
    {
        this.itemCoverSelectedListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;

        recyclerView.setOnKeyListener((v, keyCode, event) -> {


            if(mData == null || mData.size()==0)
            {
                return false;
            }
            if (event.getAction() == ACTION_DOWN) {

                if(keyCode == KEYCODE_ENTER || keyCode == KEYCODE_DPAD_CENTER)
                {
                    if(itemCoverSelectedListener != null)
                    {
                        ViewHolder holder = (ViewHolder) parentRecycler.findViewHolderForAdapterPosition(selected);
                        holder.loadingProgressBar.setVisibility(View.VISIBLE);
                        itemCoverSelectedListener.onItemSelected(getItemSelected(), holder.categoryImageView, holder.titleTextView);
                    }


                    return true;
                }

            }

            return false;
        });

    }

    @Override
    public ItemCoverAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.last_added_start_item, parent, false);
        return new ItemCoverAdapter.ViewHolder(v);


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
        if(selected == -1)
        {
            ViewHolder viewHolder = (ViewHolder) parentRecycler.findViewHolderForAdapterPosition(0);
            if (viewHolder != null) {
                viewHolder.hideText();
            }
            canMove= true;
        }

        if(selected>=0 && selected<mData.size()) {



            if(parentRecycler != null) {
                if(this.selected >=0) {
                    ViewHolder viewHolder = (ViewHolder) parentRecycler.findViewHolderForAdapterPosition(this.selected);
                    if (viewHolder != null) {
                        viewHolder.hideText();
                    }
                }

                ViewHolder viewHolder = (ViewHolder) parentRecycler.findViewHolderForAdapterPosition(selected);
                if(viewHolder != null)
                {
                    viewHolder.showText();
                }
            }

            this.selected = selected;

            if(!firstAssign)
            {
                firstAssign = true;
            }
        }
    }

    public ItemCover getItemSelected()
    {
        Log.d("getItem", "Selected: "+this.selected);

        if(mData == null || selected<0 || selected>mData.size())
        {
            return null;
        }
        return mData.get(selected);
    }

    public  int getSelected()
    {
        return  this.selected;
    }

    public Observable<ItemCover> getPositionClicks(){
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
    public void onBindViewHolder(final ItemCoverAdapter.ViewHolder holder, final int position) {

        ItemCover item = mData.get(position);
        holder.titleTextView.setText(item.getTitle());
        Glide.with(holder.itemView.getContext()).load(item.getThumbnail()).into(holder.categoryImageView);
        //holder.yearTextView.setText(String.valueOf(item.getYear()));
        ViewCompat.setTransitionName(holder.categoryImageView, "image"+ item.getTitle()+ item.getId() );
        ViewCompat.setTransitionName(holder.titleTextView, "title"+ item.getTitle()+ item.getId() );

        if(firstAssign)
        {
            if(position == selected) {
                firstAssign = false;
                if (!showAssign) {
                    showAssign = true;
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    canMove = true;
                }
            }
        }

        holder.itemView.setOnKeyListener((view, i, keyEvent) -> {
            int action = keyEvent.getAction();
            int keyCode =  keyEvent.getKeyCode();

            if(action== ACTION_DOWN)
            {
                if(keyCode == KEYCODE_ENTER || keyCode == KEYCODE_DPAD_CENTER)
                {
                    if(itemCoverSelectedListener != null)
                    {
                        itemCoverSelectedListener.onItemSelected(item, holder.categoryImageView, holder.titleTextView);
                    }


                    return true;
                }
            }
            return false;

        });

        holder.itemView.setOnClickListener(v -> {
            onClickSubject.onNext(item);
            setSelected(position);
        });


    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        ImageView categoryImageView;
        LinearLayout containerLayout;
        LinearLayout bottomLayout;
        SmoothProgressBar loadingProgressBar;
        TextView yearTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            categoryImageView = itemView.findViewById(R.id.categoryImageView);
            containerLayout = itemView.findViewById(R.id.container);
            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            loadingProgressBar = itemView.findViewById(R.id.loadingProgress);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            itemView.findViewById(R.id.container).setOnClickListener(this);
        }

        public void showText() {
            ItemCover item = getItemSelected();
            if(item != null) {
               /* categoryImageView.animate().cancel();
                int parentHeight = ((View) categoryImageView.getParent()).getHeight();
                float scale = (parentHeight - titleTextView.getHeight()) / (float) categoryImageView.getHeight();
                categoryImageView.setPivotX(categoryImageView.getWidth() * 0.5f);
                categoryImageView.setPivotY(0);
                categoryImageView.animate().scaleX(scale)
                        .withEndAction(() -> {
                            titleTextView.setVisibility(View.VISIBLE);
                    //        bottomLayout.setVisibility(View.VISIBLE);
                            canMove = true;

                        })
                        .scaleY(scale).setDuration(100)
                        .start();*/
                titleTextView.setVisibility(View.VISIBLE);
            }
        }

        public void hideText() {
          //  canMove = false;
            ItemCover item =  getItemSelected();
            if(item != null) {
                titleTextView.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
                loadingProgressBar.setVisibility(View.GONE);

/*                categoryImageView.animate().cancel();
                titleTextView.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
                loadingProgressBar.setVisibility(View.GONE);
                containerLayout.setBackgroundColor( containerLayout.getContext().getResources().getColor(R.color.transparent));
                categoryImageView.animate().scaleX(1f).scaleY(1f)
                        .withEndAction(()->
                                {
                                    canMove = true;
                                }
                        )
                        .setDuration(100)
                        .start();*/
            }
        }

        @Override
        public void onClick(View v) {
            parentRecycler.smoothScrollToPosition(getAdapterPosition());
        }
    }


}

