package com.arca.equipfix.gambachanneltv.ui.local_components;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

/**
 * Created by gabri on 6/8/2018.
 */

public class GambaListRow extends ListRow {
    private static final String TAG = GambaListRow.class.getSimpleName();
    private int mNumRows = 2;


    public GambaListRow(HeaderItem header, ObjectAdapter adapter) {
        super(header, adapter);
    }

    public GambaListRow(long id, HeaderItem header, ObjectAdapter adapter) {
        super(id, header, adapter);
    }

    public GambaListRow(ObjectAdapter adapter) {
        super(adapter);
    }

    public void setNumRows(int numRows) {
        mNumRows = numRows;
    }

    public int getNumRows() {
        return mNumRows;
    }


}
