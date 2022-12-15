package com.arca.equipfix.gambachanneltv.ui.presenters;

import android.content.Context;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;

import java.util.HashMap;

/**
 * Created by gabri on 6/22/2018.
 */

public class CardPresenterSelector extends PresenterSelector {

    private final Context mContext;
    private final HashMap<Card.Type, Presenter> presenters = new HashMap<Card.Type, Presenter>();

    public CardPresenterSelector(Context context) {
        mContext = context;
    }

    @Override
    public Presenter getPresenter(Object item) {

        if (!(item instanceof Card)) throw new RuntimeException(
                String.format("The PresenterSelector only supports data items of type '%s'",
                        Card.class.getName()));
        Card card = (Card) item;
        Presenter presenter = presenters.get(card.getType());
        if (presenter == null) {
            switch (card.getType()) {
                case SINGLE_LINE:
                    presenter = new SingleLineCardPresenter(mContext);
                    break;
                case MOVIE_BASE:
                    int themeResId = R.style.MovieCardBasicTheme;
                    presenter = new ImageCardViewPresenter(mContext, themeResId);
                    break;
                case EPISODE:
                    presenter = new TextCardPresenter(mContext);
                    break;
                case MAIN_MENU:
                    presenter = new MainMenuCardViewPresenter(mContext);
                    break;
                case SUBMENU:
                    presenter = new SubmenuCardPresenter(mContext);
                    break;
                default:
                    presenter = new ImageCardViewPresenter(mContext);
                    break;
            }
        }
        presenters.put(card.getType(), presenter);
        return presenter;
    }

}
