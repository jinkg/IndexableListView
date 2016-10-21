package com.yalin.indexablelistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：YaLin
 * 日期：2016/7/15.
 */
public class IndexBar extends View {
    public interface OnLetterChangeListener {
        void onLetterChange(Character letter, int y);

        void onCancel();
    }

    private static final int DEFAULT_BACKGROUND = Color.parseColor("#000000");

    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffff");

    private static final int DEFAULT_TEXT_SIZE = 30;

    private Character[] mLetterChars = {'#', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'S', 'Y', 'Z'};

    private Paint mPaint;

    private List<Character> mLetters;

    private int x, y;

    private int mLetterSpace = 0;

    private boolean mIsLetterUpper = true;

    private int mBackgroundColor;

    private int mTextColor;

    private int mTextSize;

    private int mLetterSize;

    private OnLetterChangeListener mListener;

    Rect mTextBounds = new Rect();

    private int mCharWidth;

    private int mCharHeight;

    public IndexBar(Context context) {
        super(context);
        init(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexBar);
        mBackgroundColor = typedArray.getColor(R.styleable.IndexBar_backgroundColor, DEFAULT_BACKGROUND);
        mTextColor = typedArray.getColor(R.styleable.IndexBar_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.IndexBar_textSize, DEFAULT_TEXT_SIZE);
        typedArray.recycle();

        mLetters = Arrays.asList(mLetterChars);
        mLetterSize = mLetters.size();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

        setBackgroundColor(mBackgroundColor);

    }

    public void setLetterChangeListener(OnLetterChangeListener listener) {
        mListener = listener;
    }

    public void setLetters(List<Character> letters) {
        if (letters == null || letters.size() == 0) {
            return;
        }

        mLetters = new ArrayList<>();
        mLetters.add(mLetterChars[0]);
        mLetters.addAll(letters);
        mLetterSize = mLetters.size();
        postInvalidate();
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        super.setBackgroundColor(mBackgroundColor);
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mPaint.setColor(mTextColor);
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int finalWidth = 0, finalHeight = 0;

        float charWidthAndHeight = mPaint.measureText(mLetters.get(0).toString()) + mLetterSpace;

        if (widthMode == MeasureSpec.EXACTLY) {
            finalWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            finalWidth = (int) (charWidthAndHeight + getPaddingLeft() + getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            finalHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            finalHeight = (int) (charWidthAndHeight + mLetters.size() + getPaddingBottom());
        }

        finalHeight *= (float) 4 / 5;
        setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        y = getHeight() / mLetterSize;
        for (int i = 0; i < mLetterSize; i++) {
            String letter = mLetters.get(i).toString();
            mPaint.getTextBounds(letter, 0, 1, mTextBounds);
            mCharWidth = mTextBounds.width();
            mCharHeight = mTextBounds.height();
            x = (getWidth() / 2 - mCharWidth / 2);
            String tempString = mIsLetterUpper ? letter.toUpperCase() : letter.toLowerCase();
            canvas.drawText(tempString, x, y * i + y / 2 + mCharHeight / 2, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        int height = getHeight();
        int currentLetterIndex = (int) (y * mLetterSize / height);
        y = (float) height / mLetterSize * currentLetterIndex;
        y += getTop();
        y += (float) height / mLetterSize / 2;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                onTouchIndex(currentLetterIndex, (int) y);
                return true;
            case MotionEvent.ACTION_UP:
                onTouchCancel();
                return true;
            default:
                return true;
        }
    }

    private int lastIndex = -1;

    private void onTouchIndex(int index, int y) {
        if (lastIndex == index) {
            return;
        }
        if (index >= mLetterSize || index < 0) {
            return;
        }
        lastIndex = index;
        if (mListener != null) {
            mListener.onLetterChange(mLetters.get(lastIndex), y);
        }
    }

    private void onTouchCancel() {
        lastIndex = -1;
        if (mListener != null) {
            mListener.onCancel();
        }
    }
}
