package com.arca.equipfix.gambachanneltv.ui.local_components;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.arca.equipfix.gambachanneltv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gabri on 7/4/2018.
 */

public class InputDialog extends Dialog implements View.OnClickListener  {
    private Context _context;
    private View.OnClickListener _leftButtonListener;

    private String mInput;


    @BindView(R.id.inputDialogTitleTextView)TextView mTitleTextView;
    @BindView(R.id.inputDialogContentEditText)EditText mContentEditText;
    @BindView(R.id.inputDialogLeftButton)TextView mLeftButton;


    public InputDialog(Context context) {
        super(context);
    }

    public InputDialog(Context context, boolean cancelable, String title, String currentContent, String leftButtonText, boolean show, int type)
    {
        super(context);
        _context = context;
        setContentView(R.layout.input_dialog);
        setCancelable(cancelable);
        ButterKnife.bind(this);
        mLeftButton.setOnClickListener(this);
        setTitle(title);
        setLeftButtonText(leftButtonText);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        if(show)
        {
            show();
        }
        mContentEditText.requestFocus();
        mContentEditText.setText(currentContent);
        mContentEditText.setInputType(type);
        if(currentContent.length()>0)
        {
            mContentEditText.setSelection(0, currentContent.length());
        }

        mContentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_PREVIOUS) {
                    onClick(findViewById(R.id.inputDialogLeftButton));
                }
                return false;

            }
        });

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }



    public void setTitle(String title)
    {
        mTitleTextView.setText(title);
        mTitleTextView.setVisibility(View.VISIBLE);
    }

    public void setOnLeftButtonClick(View.OnClickListener clickListener)
    {
        _leftButtonListener = clickListener;
    }

    public void setLeftButtonText(String text)
    {
        if(!text.equals("")) {
            mLeftButton.setText(text);
            mLeftButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.inputDialogLeftButton:
                mInput = mContentEditText.getText().toString();
                if(_leftButtonListener != null)
                {
                    _leftButtonListener.onClick(v);
                    final InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                break;
        }
        dismiss();
    }

    public String getInput()
    {
        return  mInput;
    }
}
