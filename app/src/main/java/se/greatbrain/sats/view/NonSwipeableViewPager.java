package se.greatbrain.sats.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public final class NonSwipeAbleViewPager extends ViewPager
{
    public NonSwipeAbleViewPager(Context context)
    {
        super(context);
    }

    public NonSwipeAbleViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return false;
    }
}
