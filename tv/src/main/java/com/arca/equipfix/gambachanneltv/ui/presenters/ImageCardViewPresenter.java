package com.arca.equipfix.gambachanneltv.ui.presenters;

import android.content.Context;
import android.view.ContextThemeWrapper;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;


public class ImageCardViewPresenter extends AbstractCardPresenter<ImageCardView> {

    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;

    private static int sSelectedTextColor;
    private static int sDefaultTextColor;

    private  ImageCardView imageCardView;

    public ImageCardView getImageCardView() { return imageCardView;}

    public ImageCardViewPresenter(Context context, int cardThemeResId) {
        super(new ContextThemeWrapper(context, cardThemeResId));
    }

    public ImageCardViewPresenter(Context context) {
        this(context, R.style.DefaultCardTheme);
    }


    @Override
    protected ImageCardView onCreateView() {

        sDefaultBackgroundColor = ContextCompat.getColor(getContext(), R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(getContext(), R.color.gambaRed);
        sDefaultTextColor =  ContextCompat.getColor(getContext(),R.color.lb_basic_card_title_text_color);
        sSelectedTextColor =  ContextCompat.getColor(getContext(),R.color.black);

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
        int textColor = selected ? sSelectedTextColor : sDefaultTextColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);


        // Set title text color
   /*     TextView title =  view.findViewById(R.id.title_text);
        if(title != null) {
            ((TextView) view.findViewById(R.id.title_text)).setTextColor(ContextCompat.getColor(view.getContext(), textColor));
        }*/
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
        if (card.getLocalImageResourceName() != null) {
            int resourceId = getContext().getResources()
                    .getIdentifier(card.getLocalImageResourceName(),
                            "drawable", getContext().getPackageName());
            Glide.with(getContext())
                    .load(resourceId)
                    .into(cardView.getMainImageView());
        }
        else
        {
            Glide.with(getContext()).load(card.getImageUrl())
                    .into(cardView.getMainImageView());
        }
    }

}
