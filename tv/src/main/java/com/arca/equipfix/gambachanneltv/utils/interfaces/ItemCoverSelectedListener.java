package com.arca.equipfix.gambachanneltv.utils.interfaces;

import android.widget.ImageView;
import android.widget.TextView;

import com.arca.equipfix.gambachanneltv.data.network.model.ItemCover;

/**
 * Created by gabri on 6/19/2018.
 */

public interface ItemCoverSelectedListener {
    void onItemSelected(ItemCover item, ImageView thumbnail, TextView title);
}
