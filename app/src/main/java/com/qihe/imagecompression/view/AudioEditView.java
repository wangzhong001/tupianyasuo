package com.qihe.imagecompression.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qihe.imagecompression.R;
import com.xinqidian.adcommon.util.KLog;


/**
 * 描述：拖动裁剪View
 * 作者：Zhout
 * 日期：2019/12/20 9:54
 * 邮箱：247505838@qq.com
 */
public class AudioEditView extends View {
    /**
     * 自身宽 / 高
     */
    private int mWidth, mHeight;
    /**
     * 画笔 宽1dp
     */
    private Paint mPaint;
    /**
     * 左右俩个矩形
     */
    private RectF rectF1, rectF2, cursorRectF;
    /**
     * 俩头滑块控件宽度    20dp
     */
    private float thumbWidth;
    /**
     * 指示器宽度 8dp
     */
    private float cursorWidth;
    /**
     * 俩头滑块的icon
     */
    private Bitmap thumbBitmapLeft;
    private Bitmap thumbBitmapRight;
    /**
     * 指示器Icon
     */
    private Bitmap cursorBitmap;
    /**
     * 滑动监听
     */
    private OnScrollListener onScrollListener;
    /**
     * 最小间隔时长对应的px
     */
    private float minPx;
    /**
     * 最大时长对应的px
     */
    private float maxPx;
    /**
     * 最大时长 600s
     */
    private int maxMilliSecond = 600 * 1000;
    /**
     * 最小时长 5s
     */
    private int minMilliSecond = 1 * 1000;
    /**
     * 区域外左侧部分
     */
    private RectF mGrayRectLeft;
    /**
     * 区域外左侧部分
     */
    private RectF mGrayRectRight;
    /**
     * 滑块滑动后的左右两侧空白部分颜色(透明40%)
     */
    private int outRectColor = getResources().getColor(R.color.transparent_40);
    /**
     * 上线2条线的颜色
     */
    private int strokeColor = getResources().getColor(R.color.audio_edit_stroke_color);


    public AudioEditView(Context context) {
        super(context);
        init();
    }

    public AudioEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int strokeWidth = (int) getResources().getDimension(R.dimen.stroke_width);
        mPaint.setStrokeWidth(strokeWidth);
        thumbBitmapLeft = BitmapFactory.decodeResource(getResources(), R.drawable.icon_thumb_left);
        thumbBitmapRight = BitmapFactory.decodeResource(getResources(), R.drawable.icon_thumb_right);
        cursorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_cursor);
        thumbWidth = (int) getResources().getDimension(R.dimen.thumb_width);
        cursorWidth = (int) getResources().getDimension(R.dimen.cursor_width);
        initPx();
    }

    /**
     * 设置最小时间间隔 单位毫秒
     *
     * @param minMilliSec
     */
    public void setMinInterval(int minMilliSec) {
        if (minMilliSec >= minMilliSecond && minMilliSec <= maxMilliSecond) {
            minMilliSecond = minMilliSec;
        }
        initPx();
    }

    /**
     * 设置时长 单位毫秒
     *
     * @param duration
     */
    public void setDuration(int duration) {
        maxMilliSecond = duration;
        initPx();
    }

    private void initPx() {
        // 延时处理，保证mWidth > 0
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mWidth > 0) {
                    maxPx = mWidth - thumbWidth * 2;
                    minPx = maxPx * minMilliSecond / maxMilliSecond;
                    updateListener();
                }
            }
        }, 100);
    }


    public interface OnScrollListener {
        /**
         * 滑动两边滑块监听
         *
         * @param info 滑动信息
         */
        void onScrollThumb(ScrollInfo info);

        /**
         * 手动去滑动指示器
         *
         * @param info 滑动信息
         */
        void onScrollCursor(ScrollInfo info);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.onScrollListener = listener;
    }

    public float getLeftInterval() {
        return rectF1.left;
    }

    public float getRightInterval() {
        return rectF2.right;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mWidth == 0) {
            mWidth = getWidth();
            mHeight = getHeight();
            rectF1 = new RectF();
            rectF1.left = 0;
            rectF1.top = 0;
            rectF1.right = thumbWidth;
            rectF1.bottom = mHeight;
            cursorRectF = new RectF();
            cursorRectF.left = rectF1.right - cursorWidth / 2;
            cursorRectF.top = 0;
            cursorRectF.right = rectF1.right + cursorWidth / 2;
            cursorRectF.bottom = mHeight;
            rectF2 = new RectF();
            rectF2.left = mWidth - thumbWidth;
            rectF2.top = 0;
            rectF2.right = mWidth;
            rectF2.bottom = mHeight;
            mGrayRectLeft = new RectF();
            mGrayRectLeft.left = 0;
            mGrayRectLeft.top = 0;
            mGrayRectLeft.right = rectF1.left;
            mGrayRectLeft.bottom = mHeight;
            mGrayRectRight = new RectF();
            mGrayRectRight.left = rectF2.right;
            mGrayRectRight.top = 0;
            mGrayRectRight.right = mWidth;
            mGrayRectRight.bottom = mHeight;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        move(event);
        return scrollLeft || scrollRight || scrollCursor;
    }

    /**
     * 按下的点 X
     */
    private float downX;
    private boolean scrollLeft;
    private boolean scrollRight;
    private boolean scrollCursor;

    private boolean move(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                if (downX > cursorRectF.left - cursorWidth / 2 && downX < cursorRectF.right + cursorWidth / 2) {
                    scrollCursor = true;
                } else {
                    if (downX > rectF1.left - thumbWidth / 2 && downX < rectF1.right + thumbWidth / 2) {
                        scrollLeft = true;
                    }
                    if (downX > rectF2.left - thumbWidth / 2 && downX < rectF2.right + thumbWidth / 2) {
                        scrollRight = true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float scrollX = moveX - downX;

                KLog.e("scrollX-->"+moveX+"-->"+scrollX);

                if (scrollLeft) {
                    rectF1.left = rectF1.left + scrollX;
                    rectF1.right = rectF1.right + scrollX;
                    if (rectF1.left < 0) {
                        rectF1.left = 0;
                        rectF1.right = thumbWidth;
                    }
                    if (rectF1.right > rectF2.left - minPx) {
                        rectF1.right = rectF2.left - minPx;
                        rectF1.left = rectF1.right - thumbWidth;
                    }
                    scrollCursor = true;
                    mGrayRectLeft.right = rectF1.left;
                    moveCursorToStart();
                } else if (scrollRight) {
                    rectF2.left = rectF2.left + scrollX;
                    rectF2.right = rectF2.right + scrollX;
                    if (rectF2.right > mWidth) {
                        rectF2.right = mWidth;
                        rectF2.left = rectF2.right - thumbWidth;
                    }
                    if (rectF2.left < rectF1.right + minPx) {
                        rectF2.left = rectF1.right + minPx;
                        rectF2.right = rectF2.left + thumbWidth;
                    }
                    scrollCursor = true;
                    mGrayRectRight.left = rectF2.right;
                    moveCursorToStart();
                } else if (scrollCursor) {
                    cursorRectF.left = cursorRectF.left + scrollX;
                    cursorRectF.right = cursorRectF.right + scrollX;
                    if (cursorRectF.left + cursorWidth / 2 < rectF1.right) {
                        cursorRectF.left = rectF1.right - cursorWidth / 2;
                        cursorRectF.right = cursorRectF.left + cursorWidth;
                    }
                    if (cursorRectF.right - cursorWidth / 2 > rectF2.left) {
                        cursorRectF.right = rectF2.left + cursorWidth / 2;
                        cursorRectF.left = cursorRectF.right - cursorWidth;
                    }
                }
                updateListener();
                downX = moveX;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                downX = 0;
                scrollLeft = false;
                scrollRight = false;
                scrollCursor = false;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 两滑块之间矩形区域
        mPaint.setColor(Color.GREEN);
        canvas.drawBitmap(thumbBitmapLeft, null, rectF1, mPaint);
        canvas.drawBitmap(thumbBitmapRight, null, rectF2, mPaint);

        mPaint.setColor(strokeColor);
        canvas.drawLine(rectF1.left + thumbWidth, 0, rectF2.right - thumbWidth, 0, mPaint);
        canvas.drawLine(rectF1.left + thumbWidth, mHeight, rectF2.right - thumbWidth, mHeight, mPaint);

        canvas.drawBitmap(cursorBitmap, null, cursorRectF, mPaint);

        // 两滑块矩形外层区域
        mPaint.setColor(outRectColor);
        canvas.drawRect(mGrayRectLeft, mPaint);
        canvas.drawRect(mGrayRectRight, mPaint);
    }


    /**
     * 更新指示器位置
     */
    public void updateCursor(float indexTime) {
        KLog.e("indexTime-->",indexTime+"");
        if (mWidth == 0) {// 布局还未测量
            return;
        }
        float index = indexTime * maxPx / maxMilliSecond + thumbWidth;
        cursorRectF.left = index - cursorWidth / 2;
        cursorRectF.right = cursorRectF.left + cursorWidth;

        if (index > rectF2.left) {
            index = rectF2.left;
            cursorRectF.right = index + cursorWidth / 2;
            cursorRectF.left = index - cursorWidth / 2;
        }
        updateListener();

        if (index == rectF2.left) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveCursorToStart();
                    updateListener();
                }
            }, 20);
        }
    }


    private void updateListener() {
        if (onScrollListener != null) {
            float indexPx = cursorRectF.left + cursorWidth / 2;
            int indexTime = (int) ((indexPx - thumbWidth) * maxMilliSecond / maxPx);
            int startTime = (int) ((rectF1.right - thumbWidth) * maxMilliSecond / maxPx);
            int endTime = (int) ((rectF2.left - thumbWidth) * maxMilliSecond / maxPx);
            ScrollInfo info = new ScrollInfo();
            info.setStartTime(startTime);
            info.setEndTime(endTime);
            info.setIndexTime(indexTime);
            info.setStartPx(rectF1.right);
            info.setEndPx(rectF2.left);
            info.setIndexPx(indexPx);
            onScrollListener.onScrollThumb(info);
            if (scrollCursor) {
                onScrollListener.onScrollCursor(info);
            }
        }
        invalidate();
    }

    public void moveCursorToStart() {
        if (mWidth == 0) {
            return;
        }
        cursorRectF.left = rectF1.right - cursorWidth / 2;// 一滑动,指示器就回到最左边
        cursorRectF.right = cursorRectF.left + cursorWidth;// 一滑动,指示器就回到最左边
        invalidate();
    }


    public class ScrollInfo {
        private int startTime; // 左侧滑块最右边位置对应的时间毫秒
        private int endTime; //右侧滑块结最左边端位置对应的时间毫秒
        private int indexTime; //指示器位置对应时间毫秒
        private float startPx; //左侧滑块最右边位置
        private float endPx; //右侧滑块结最左边端位置
        private float indexPx; //指示器位置

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getIndexTime() {
            return indexTime;
        }

        public void setIndexTime(int indexTime) {
            this.indexTime = indexTime;
        }

        public float getStartPx() {
            return startPx;
        }

        public void setStartPx(float startPx) {
            this.startPx = startPx;
        }

        public float getEndPx() {
            return endPx;
        }

        public void setEndPx(float endPx) {
            this.endPx = endPx;
        }

        public float getIndexPx() {
            return indexPx;
        }

        private void setIndexPx(float indexPx) {
            this.indexPx = indexPx;
        }

        @Override
        public String toString() {
            return "startTime = " + startTime
                    + " , endTime = " + endTime
                    + " , indexTime = " + indexTime
                    + " , startPx = " + startPx
                    + " , endPx = " + endPx
                    + " , indexPx = " + indexPx;
        }
    }
}