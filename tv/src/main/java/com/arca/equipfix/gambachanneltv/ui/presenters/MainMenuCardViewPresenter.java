package com.arca.equipfix.gambachanneltv.ui.presenters;

import android.content.Context;
import android.view.ContextThemeWrapper;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;
import com.bumptech.glide.Glide;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;

/**
 * Created by gabri on 7/14/2018.
 */

public class MainMenuCardViewPresenter extends AbstractCardPresenter<ImageCardView> {

    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;

    private  ImageCardView imageCardView;

    public ImageCardView getImageCardView() { return imageCardView;}

    public MainMenuCardViewPresenter(Context context, int cardThemeResId) {
        super(new ContextThemeWrapper(context, cardThemeResId));
    }

    public MainMenuCardViewPresenter(Context context) {
        this(context, R.style.MenuCardTheme);
    }

    @Override
    protected ImageCardView onCreateView() {

        sDefaultBackgroundColor = ContextCompat.getColor(getContext(), R.color.transparent);
        sSelectedBackgroundColor = ContextCompat.getColor(getContext(), R.color.transparent);
        this.imageCardView = new ImageCardView(getContext())
        {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        imageCardView.setFocusable(true);
        imageCardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(imageCardView, false);


        return imageCardView;
    }

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
        if(view != null && view.getTag() != null && (Card)view.getTag() != null)
        {
            Card card = (Card) view.getTag();
            card.setSelected(selected);
            if(selected)
            {
                Glide.with(view.getContext())
                        .load(card.getPrimaryImageResource())
                        .into(view.getMainImageView());
            }
            else
            {
                Glide.with(view.getContext())
                        .load(card.getSecondaryImageResource())
                        .into(view.getMainImageView());
            }
        }
    }

    @Override
    public void onBindViewHolder(Card card, final ImageCardView cardView) {
        cardView.setTag(card);
        cardView.setTitleText(card.getTitle());
        cardView.setContentText(card.getDescription());
        if(cardView.getContentText().equals(EMPTY_STRING) )
        {
            cardView.setBadgeImage(null);
        }
        Glide.with(getContext())
                .load(card.getSecondaryImageResource())
                .into(cardView.getMainImageView());
    }

}