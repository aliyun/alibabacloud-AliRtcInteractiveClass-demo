package com.aliyun.rtc.interactiveclassplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.aliyun.rtc.interactiveclassplayer.utils.SizeUtil;


public class ChannelEditVIew extends android.support.v7.widget.AppCompatEditText implements TextWatcher {
    private static final String TAG = ChannelEditVIew.class.getSimpleName();
    private int maxLenght;
    private boolean focused;
    private int mWidthSize;
    private int mHeightSize;
    private int grapWidth = SizeUtil.dip2px(getContext(), 16);//间隔
    private Paint mPaint;
    private float[] points;
    private int marginLfet;
    private int marginButtom = SizeUtil.dip2px(getContext(), 8);
    private String content;
    private float mLineLenght;
    private boolean mErrorEnable;
    private int mErrorColorBottomLine;

    public ChannelEditVIew(Context context) {
        this(context, null);
    }

    public ChannelEditVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        int attributeCount = attrs.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = attrs.getAttributeName(i);
            if ("maxLength".equals(attributeName)) {
                String value = attrs.getAttributeValue(i);
                maxLenght = Integer.parseInt(value);
            }
        }
        initPaint();
        addTextChangedListener(this);
        setCursorVisible(false);
    }

    public void setErrorEnable(boolean errorEnable) {
        this.mErrorEnable = errorEnable;
    }

    public void setErrorColorBottomLine(int errorColorBottomLine) {
        mErrorColorBottomLine = errorColorBottomLine;
    }

    private void initLines() {
        points = new float[4 * maxLenght];
        marginLfet = SizeUtil.dip2px(getContext(), 6);
        mLineLenght = (mWidthSize * 1.0f - grapWidth * (maxLenght - 1) - marginLfet) / maxLenght;
        for (int i = 0; i < maxLenght; i++) {
            points[i * 4] = i * mLineLenght + i * grapWidth + marginLfet;
            points[i * 4 + 1] = mHeightSize * 1f;
            points[i * 4 + 2] = points[i * 4] + mLineLenght;
            points[i * 4 + 3] = mHeightSize * 1f;
        }
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(SizeUtil.sp2px(getContext(), 14));
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        mWidthSize = getSizeBySpecMode(widthMode, mWidthSize);
        mHeightSize = getSizeBySpecMode(heightMode, mHeightSize);
        initLines();
        setBackground(null);
    }

    private int getSizeBySpecMode(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST) {
            size = ((ViewGroup) getParent()).getMeasuredWidth();
        } else if (mode == MeasureSpec.UNSPECIFIED) {
            size = ((ViewGroup) getParent()).getMeasuredWidth();
        }
        return size;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        this.focused = focused;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (focused || !TextUtils.isEmpty(content)) {
            mPaint.setStrokeWidth(SizeUtil.dip2px(getContext(), 3));
            mPaint.setColor(mErrorEnable ? mErrorColorBottomLine : Color.BLACK);
            canvas.drawLines(points, mPaint);
        } else {
            mPaint.setStrokeWidth(SizeUtil.dip2px(getContext(), 2));
            canvas.drawLine(marginLfet, mHeightSize, mWidthSize, mHeightSize, mPaint);
        }

        for (int i = 0; i < content.length(); i++) {
            mPaint.setStrokeWidth(1);
            canvas.drawText(String.valueOf(content.charAt(i)), points[i * 4] + mLineLenght / 2, mHeightSize - marginButtom, mPaint);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.content = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
