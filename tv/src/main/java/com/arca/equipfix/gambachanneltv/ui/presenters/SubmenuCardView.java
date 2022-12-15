package com.arca.equipfix.gambachanneltv.ui.presenters;

import android.content.Context;

import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.leanback.widget.BaseCardView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;

/**
 * Created by gabri on 7/14/2018.
 */

public class SubmenuCardView extends BaseCardView {

    private Card card;

    public SubmenuCardView(Context context) {
        super(context, null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_card, this);
        setFocusable(true);
    }

    public void updateUi(Card card) {
        TextView primaryText =  findViewById(R.id.titleTextView);
        this.card = card;
        primaryText.setText(card.getTitle());
    }

    public void setTextColor(boolean selected)
    {
        this.card.setSelected(selected);
        TextView primaryText =  findViewById(R.id.titleTextView);
        primaryText.setTextColor(selected ? getContext().getResources().getColor(R.color.gambaRed) : getContext().getResources().getColor(R.color.black) );

    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
