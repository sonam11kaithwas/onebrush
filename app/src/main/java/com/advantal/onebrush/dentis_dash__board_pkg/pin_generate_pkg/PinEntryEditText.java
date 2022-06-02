package com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.advantal.onebrush.R;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;

/**
 * Created by Sonam on 11-02-2022 15:20.
 */
public class PinEntryEditText extends EditText {

    public static final String XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
    protected long lastCursorChangeState = -1;
    protected boolean cursorVisible = true;
    int[][] mStates = new int[][]{
            new int[]{android.R.attr.state_selected}, // selected
            new int[]{android.R.attr.state_focused}, // focused
            new int[]{-android.R.attr.state_focused}, // unfocused
    };
    int[] mColors = new int[]{
            Color.RED,
            Color.BLACK,
            Color.GREEN
    };
    ColorStateList mColorStates = new ColorStateList(mStates, mColors);
    //    private float mSpace = ((int) getResources().getDimension(R.dimen._minus1sdp));
    //24 dp by default, space between the lines
    private float mSpace = -1; //24 dp by default, space between the lines
    private float mCharSize;
    private float mNumChars = 4;
    private float mLineSpacing = 8; //8dp by default, height of the text from our lines
    //    private float mLineSpacing = ((int) getResources().getDimension(R.dimen._5sdp)); //8dp by default, height of the text from our lines
//    private int mMaxLength = ((int) getResources().getDimension(R.dimen._4sdp));
    private int mMaxLength = 4;
    private OnClickListener mClickListener;
    private float mLineStroke = 0.8f; //1dp by default
    private float mLineStrokeSelected = 0.8f; //2dp by default
    private Paint mLinesPaint;

    public PinEntryEditText(Context context) {
        super(context);

    }


    public PinEntryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public PinEntryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }


    public PinEntryEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);


        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;
        mLineStroke = multi * mLineStroke;
        mLineStrokeSelected = multi * mLineStrokeSelected;
        mLinesPaint = new Paint(getPaint());

        setWidth(((int) getResources().getDimension(R.dimen._10sdp)));
//        setWidth(10);
        mLinesPaint.setStrokeWidth(mLineStroke);
        if (!isInEditMode()) {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorControlActivated,
                    outValue, true);
            final int colorActivated = outValue.data;
            mColors[0] = colorActivated;

            context.getTheme().resolveAttribute(R.attr.colorPrimaryDark,
                    outValue, true);
            final int colorDark = outValue.data;
            mColors[1] = colorDark;

            context.getTheme().resolveAttribute(R.attr.colorControlHighlight,
                    outValue, true);
            final int colorHighlight = outValue.data;
            mColors[2] = colorHighlight;
        }
        setBackgroundResource(0);

        setCursorVisible(true);

        mSpace = 20; //convert to pixels for our density
//        mSpace = ((int) getResources().getDimension(R.dimen._10sdp)); //convert to pixels for our density
//        mSpace = multi * mSpace; //convert to pixels for our density
        mLineSpacing = multi * mLineSpacing; //convert to pixels for our density
        mMaxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 4);
        mNumChars = mMaxLength;

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        // When tapped, move cursor to end of text.
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());

                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }
        });

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int availableWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        if (mSpace < 0) {
            mCharSize = (availableWidth / (mNumChars * 3 - 1));
        } else {
            mCharSize = (availableWidth - (mSpace * (mNumChars - 1))) / mNumChars;
        }

        int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();


        Editable text = getText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        getPaint().setColor(Color.parseColor("#02071A"));

        for (int i = 0; i < mNumChars; i++) {
            updateColorForLines(i == textLength);
            canvas.drawLine(startX, bottom, startX + mCharSize, bottom, mLinesPaint);

            if (getText().length() > i) {
                float middle = startX + mCharSize / 2;
                canvas.drawText(text, i, i + 1, middle - textWidths[0] / 2, bottom - mLineSpacing, getPaint());
            }


            if (mSpace < 0) {
                startX += mCharSize * 2;
            } else {
                startX += mCharSize + mSpace;
            }
        }
    }


    private int getColorForState(int... states) {
        return mColorStates.getColorForState(states, Color.GRAY);
    }

    /**
     * @param next Is the current char the next character to be input?
     */
    private void updateColorForLines(boolean next) {
        if (isFocused()) {
            mLinesPaint.setStrokeWidth(mLineStrokeSelected);
            mLinesPaint.setColor(ContextCompat.getColor(OneBrushApp.getInstance(), R.color.royal));
            if (next) {
                mLinesPaint.setColor(ContextCompat.getColor(OneBrushApp.getInstance(), R.color.royal));
            }
        } else {
            mLinesPaint.setStrokeWidth(mLineStroke);
            mLinesPaint.setColor(ContextCompat.getColor(OneBrushApp.getInstance(), R.color.royal));
        }
    }


}
//https://github.com/alphamu/PinEntryEditText/issues/21