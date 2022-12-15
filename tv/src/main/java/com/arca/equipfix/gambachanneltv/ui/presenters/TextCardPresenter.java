package com.arca.equipfix.gambachanneltv.ui.presenters;

import android.content.Context;

import com.arca.equipfix.gambachanneltv.data.Card;
import com.arca.equipfix.gambachanneltv.ui.local_components.TextCardView;

/**
 * Created by gabri on 7/10/2018.
 */

public class TextCardPresenter extends AbstractCardPresenter<TextCardView> {

    public TextCardPresenter(Context context) {
        super(context);
    }

    @Override
    protected TextCardView onCreateView() {
        return new TextCardView(getContext());
    }

    @Override
    public void onBindViewHolder(Card card, TextCardView cardView) {
        cardView.updateUi(card);
    }

}
