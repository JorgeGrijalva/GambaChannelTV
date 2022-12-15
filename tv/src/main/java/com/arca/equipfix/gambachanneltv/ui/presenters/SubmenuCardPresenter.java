package com.arca.equipfix.gambachanneltv.ui.presenters;

import android.content.Context;
import com.arca.equipfix.gambachanneltv.data.Card;


/**
 * Created by gabri on 7/14/2018.
 */

public class SubmenuCardPresenter extends AbstractCardPresenter<SubmenuCardView> {

    private SubmenuCardView submenuCardView;

    public SubmenuCardPresenter(Context context) {
        super(context);
    }

    @Override
    protected SubmenuCardView onCreateView() {
        this.submenuCardView = new SubmenuCardView(getContext())
        {
            @Override
            public void setSelected(boolean selected) {
                updateTextColor(this, selected);
                super.setSelected(selected);
            }
        };

        return submenuCardView;
    }

    private static void updateTextColor(SubmenuCardView view, boolean selected) {
        view.setTextColor(selected);
    }



    @Override
    public void onBindViewHolder(Card card, SubmenuCardView cardView) {
        cardView.updateUi(card);
    }

}
