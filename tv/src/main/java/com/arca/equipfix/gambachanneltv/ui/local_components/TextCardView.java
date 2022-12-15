package com.arca.equipfix.gambachanneltv.ui.local_components;

import android.content.Context;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.BaseCardView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;

/**
 * Created by gabri on 7/10/2018.
 */

public class TextCardView extends BaseCardView {

    private  Card card;

    public TextCardView(Context context) {
        super(context, null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_icon_card, this);
        setFocusable(true);
    }

    public void updateUi(Card card) {
        TextView lengthTextView= findViewById(R.id.lengthTextView);
        TextView directorTextView = findViewById(R.id.directorTextView);

        TextView primaryText =  findViewById(R.id.titleTextView);
        final ImageView imageView = findViewById(R.id.imageViewed);
        if(card.isViewed())
        {
            imageView.setVisibility(VISIBLE);
        }
        else
        {
            imageView.setVisibility(GONE);
        }
        this.card = card;

        lengthTextView.setText(card.getExtraText());
        directorTextView.setText(card.getExtraText2());

        primaryText.setText(card.getTitle());
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
