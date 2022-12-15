package com.arca.equipfix.gambachanneltv.ui.local_components;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.arca.equipfix.gambachanneltv.R;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;

/**
 * Created by gabri on 7/4/2018.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {

    private Context _context;
    private View.OnClickListener _leftButtonListener;
    private View.OnClickListener _rightButtonListener;


    @BindView(R.id.questionTextView)
    TextView _questionTextView;
    @BindView(R.id.contentTextView)TextView _contentTextView;
    @BindView(R.id.leftButton)
    MaterialFancyButton _leftButton;
    @BindView(R.id.rightButton)
    MaterialFancyButton _rightButton;
    @BindView(R.id.verticalLine) View _verticalLine;

    public CustomDialog(Context context, boolean cancelable)
    {
        super(context);
        _context = context;
        setContentView(R.layout.confirm_dialog);
        setCancelable(cancelable);
        ButterKnife.bind(this);
        _leftButton.setOnClickListener(this);
        _rightButton.setOnClickListener(this);
    }

    public CustomDialog(Context context, boolean cancelable, String question, String content, String leftButtonText, String rightButtonText, boolean show)
    {
        super(context);
        _context = context;
        setContentView(R.layout.confirm_dialog);
        setCancelable(cancelable);
        ButterKnife.bind(this);
        _leftButton.setOnClickListener(this);
        _rightButton.setOnClickListener(this);
        setQuestion(question);
        setContent(content);
        if(!leftButtonText.equals(EMPTY_STRING))
        {
            _leftButton.requestFocus();
        }
        setLeftButtonText(leftButtonText);
        setRightButtonText(rightButtonText);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        if(show)
        {
            try {
                show();
            }
            catch (Exception ignore)
            {
                //Posibbly Activity Died
            }
        }
    }

    public void setQuestion(String question)
    {
        _questionTextView.setText(question);
        _questionTextView.setVisibility(View.VISIBLE);
    }

    public void setContent(String content)
    {
        _contentTextView.setText(content);
        _contentTextView.setVisibility(View.VISIBLE);
    }

    public void setOnLeftButtonClick(View.OnClickListener clickListener)
    {
        _leftButtonListener = clickListener;
    }

    public void setOnRightButtonClick(View.OnClickListener clickListener)
    {
        _rightButtonListener = clickListener;
    }

    public void setLeftButtonText(String text)
    {
        if(!text.equals("")) {
            _leftButton.setText(text);
            _leftButton.setVisibility(View.VISIBLE);
            if(_rightButton.getVisibility()== View.VISIBLE)
            {
                _verticalLine.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRightButtonText(String text)
    {
        if(!text.equals("")) {
            _rightButton.setText(text);
            _rightButton.setVisibility(View.VISIBLE);
            if(_leftButton.getVisibility()== View.VISIBLE)
            {
                _verticalLine.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftButton:
                if(_leftButtonListener != null)
                {
                    _leftButtonListener.onClick(v);
                }
                break;
            case R.id.rightButton:
                if(_rightButtonListener != null)
                {
                    _rightButtonListener.onClick(v);
                }
                break;
        }
        dismiss();
    }
}
