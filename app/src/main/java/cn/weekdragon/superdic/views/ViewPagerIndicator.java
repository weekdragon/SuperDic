package cn.weekdragon.superdic.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ViewPagerIndicator extends LinearLayout {

    private Paint mPaint;
    private Path mPath;
    private int mTriangleWidth;
    private int mTriangleHeight;

    private int mInitTranslationX;//初始偏移X
    private int mTranslationX;//viewPager滑动的时候的偏移X
    private int mVisible_Count = 5;
    private List<String> mTitles;
    private ViewPager mViewPager;
    public PageChangeLisner mlisner;
    private static final float TRIANGLE_WIDTH_SCALE = 1 / 6F;
    private final int TRIANGLE_WIDTH_MAX = (int) (getScreenWidth() / 4 * TRIANGLE_WIDTH_SCALE);
    private static final int COLOR_TEXT_NORMAL = 0x77ffffff;
    private static final int COLOR_TEXT_HIGHLIGHT = 0xffffffff;

    public interface PageChangeLisner {
        public void onPageSelected(int arg0);

        public void onPageScrolled(int arg0, float arg1, int arg2);

        public void onPageScrollStateChanged(int arg0);
    }

    public void setOnPageChangeLisner(PageChangeLisner lisner) {
        this.mlisner = lisner;
    }

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);

        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / mVisible_Count * TRIANGLE_WIDTH_SCALE);
        mTriangleWidth = Math.min(mTriangleWidth, TRIANGLE_WIDTH_MAX);
        mInitTranslationX = w / mVisible_Count / 2 - mTriangleWidth / 2;
        initTriangle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if (cCount == 0) return;
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mVisible_Count;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        // TODO Auto-generated method stub
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public void setmVisible_Count(int mVisible_Count) {
        this.mVisible_Count = mVisible_Count;
    }

    private void initTriangle() {
        // TODO Auto-generated method stub
        mTriangleHeight = mTriangleWidth / 2;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
    }

    /**
     * 手指移动的时候滚动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        // TODO Auto-generated method stub
        int tabWidth = getWidth() / mVisible_Count;
        mTranslationX = (int) (tabWidth * (position + offset));
        if (position >= (mVisible_Count - 2) && getChildCount() > mVisible_Count) {
            if (mVisible_Count != 1) {
                this.scrollTo((int) ((offset + position - mVisible_Count + 2) * tabWidth), 0);
            } else {
                this.scrollTo((int) ((offset + position) * tabWidth), 0);
            }

        } else if (position == 1) {
            this.scrollTo(0, 0);
        }
        invalidate();
    }

    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            this.mTitles = titles;
            for (String title : mTitles) {
                addView(generateTextView(title));
            }
        }
        setItemClickEvent();

    }


    private View generateTextView(String title) {
        // TODO Auto-generated method stub
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mVisible_Count;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setLayoutParams(lp);
        return tv;
    }

    public void setViewPager(ViewPager viewPager, int pos) {
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                if (mlisner != null) {
                    mlisner.onPageSelected(arg0);
                }
                highLightTextView(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                scroll(arg0, arg1);
                if (mlisner != null) {
                    mlisner.onPageScrolled(arg0, arg1, arg2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                if (mlisner != null) {
                    mlisner.onPageScrollStateChanged(arg0);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }

    private void resetTextViewColor() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 高亮文本
     *
     * @param pos
     */
    private void highLightTextView(int pos) {

        View view = getChildAt(pos);
        if (view instanceof TextView) {
            resetTextViewColor();
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    private void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
