package se.greatbrain.sats.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.R;
import se.greatbrain.sats.adapter.CalendarPagerAdapter;
import se.greatbrain.sats.event.CalendarColumnClickedEvent;
import se.greatbrain.sats.event.MyTrainingRefreshEvent;
import se.greatbrain.sats.event.WorkoutListScrollEvent;

public class CalendarFragment extends Fragment
{
    private static final String TAG = "CalendarFragment";
    public static final int NUM_SIMULTANEOUS_PAGES = 5;

    private ImageView left_marker;
    private ImageView right_marker;

    public ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);

        left_marker = (ImageView) view.findViewById(R.id.back_to_now_left);
        right_marker = (ImageView) view.findViewById(R.id.back_to_now_right);

        EventBus.getDefault().register(this);

        CalendarPagerAdapter pagerAdapter = new CalendarPagerAdapter(getFragmentManager(), getActivity());

        // It is recommended to preload two times, or three times the number of simultaneous pages
        // shown
        pager.setOffscreenPageLimit(NUM_SIMULTANEOUS_PAGES * 2);
        pager.setOnPageChangeListener(new CalendarOnScrollListener(pagerAdapter));
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pagerAdapter.getPositionOfCurrentWeek_inCalendar(), false);
        setCustomScroller();

        return view;
    }

    @Override
    public void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(CalendarColumnClickedEvent event)
    {
        pager.setCurrentItem(event.position - (CalendarFragment.NUM_SIMULTANEOUS_PAGES / 2), true);
    }

    public void onEvent(MyTrainingRefreshEvent event)
    {
        CalendarPagerAdapter pagerAdapter = new CalendarPagerAdapter(getFragmentManager(), getActivity());
        pager.setOnPageChangeListener(new CalendarOnScrollListener(pagerAdapter));
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pagerAdapter.getPositionOfCurrentWeek_inCalendar(), true);
    }

    private void setCustomScroller()
    {
        try
        {
            Field scrollerInViewPager;
            scrollerInViewPager = ViewPager.class.getDeclaredField("mScroller");
            scrollerInViewPager.setAccessible(true);
            CalendarScroller scroller = new CalendarScroller(pager.getContext(), new DecelerateInterpolator());
            scrollerInViewPager.set(pager, scroller);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private final class CalendarOnScrollListener extends ViewPager.SimpleOnPageChangeListener
    {
        private final CalendarPagerAdapter pagerAdapter;

        public CalendarOnScrollListener(CalendarPagerAdapter pagerAdapter)
        {
            this.pagerAdapter = pagerAdapter;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            setMarkerVisibility(position, pagerAdapter);
        }

        @Override
        public void onPageSelected(int position)
        {
            position += NUM_SIMULTANEOUS_PAGES / 2;
            int weekHash = pagerAdapter.getWeekHashForPosition(position);
            EventBus.getDefault().post(new WorkoutListScrollEvent(weekHash));
        }
    }

    private void setMarkerVisibility(int position, CalendarPagerAdapter pagerAdapter)
    {
        if (position > pagerAdapter.getPositionOfCurrentWeek_inCalendar() + 1)
        {
            right_marker.setVisibility(View.INVISIBLE);
            left_marker.setVisibility(View.VISIBLE);
            left_marker.setPaddingRelative(0, 25, 0, 0);
        }
        else if (position < pagerAdapter.getPositionOfCurrentWeek_inCalendar() -2)
        {
            right_marker.setVisibility(View.VISIBLE);
            left_marker.setVisibility(View.INVISIBLE);
            right_marker.setPaddingRelative(0, 25, 0, 0);
        }
        else
        {
            right_marker.setVisibility(View.INVISIBLE);
            left_marker.setVisibility(View.INVISIBLE);
        }
    }

    private final class CalendarScroller extends Scroller
    {
        private int scrollDuration = 250;

        public CalendarScroller(Context context)
        {
            super(context);
        }

        public CalendarScroller(Context context, Interpolator interpolator)
        {
            super(context, interpolator);
        }

        public CalendarScroller(Context context, Interpolator interpolator, boolean flywheel)
        {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy)
        {
            super.startScroll(startX, startY, dx, dy, scrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration)
        {
            super.startScroll(startX, startY, dx, dy, scrollDuration);
        }
    }
}