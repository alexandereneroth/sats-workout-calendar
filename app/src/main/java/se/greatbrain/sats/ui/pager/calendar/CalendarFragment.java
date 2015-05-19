package se.greatbrain.sats.ui.pager.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.R;
import se.greatbrain.sats.event.CalendarColumnClickedEvent;
import se.greatbrain.sats.event.MyTrainingRefreshEvent;
import se.greatbrain.sats.event.WorkoutListScrollEvent;

public class CalendarFragment extends Fragment
{
    private static final String TAG = "CalendarFragment";
    public static final int NUM_SIMULTANEOUS_PAGES = 5;

    private ImageView leftMarker;
    private ImageView rightMarker;

    public ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);

        leftMarker = (ImageView) view.findViewById(R.id.back_to_now_left);
        rightMarker = (ImageView) view.findViewById(R.id.back_to_now_right);

        EventBus.getDefault().register(this);

        final CalendarPagerAdapter pagerAdapter = new CalendarPagerAdapter(getFragmentManager(),
                getActivity());

        leftMarker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pager.setCurrentItem(pagerAdapter.getPositionOfCurrentWeek_inCalendar(), true);
            }
        });

        rightMarker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pager.setCurrentItem(pagerAdapter.getPositionOfCurrentWeek_inCalendar(), true);
            }
        });

        // It is recommended to preload two times, or three times the number of simultaneous pages
        // shown
        pager.setOffscreenPageLimit(NUM_SIMULTANEOUS_PAGES * 2);
        pager.setOnPageChangeListener(new CalendarOnScrollListener(pagerAdapter));
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pagerAdapter.getPositionOfCurrentWeek_inCalendar(), true);
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
        CalendarPagerAdapter pagerAdapter = new CalendarPagerAdapter(getFragmentManager(),
                getActivity());
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
            CalendarScroller scroller = new CalendarScroller(pager.getContext(),
                    new DecelerateInterpolator());
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
            setMarkerVisibility(position, positionOffset, pagerAdapter);
        }

        @Override
        public void onPageSelected(int position)
        {
            position += NUM_SIMULTANEOUS_PAGES / 2;
            int weekHash = pagerAdapter.getWeekHashForPosition(position);
            EventBus.getDefault().post(new WorkoutListScrollEvent(weekHash));
        }

        private void setMarkerVisibility(int position, float positionOffset, CalendarPagerAdapter pagerAdapter)
        {
            int currentWeek = pagerAdapter.getPositionOfCurrentWeek_inCalendar();
            int topPadding = (int) Math.round(CalendarColumnFragment.getHeightOfOneRow() / 2.7);

            if (position > currentWeek + 1 && positionOffset > 0.65)
            {
                leftMarker.setVisibility(View.VISIBLE);
                leftMarker.setPadding(0, topPadding, 0, 0);
            }
            else if(position < currentWeek + 3 && positionOffset < 0.65)
            {
                leftMarker.setVisibility(View.INVISIBLE);
            }

            if(position < currentWeek - 2 && positionOffset < 0.35)
            {
                rightMarker.setVisibility(View.VISIBLE);
                rightMarker.setPadding(0, topPadding, 0, 0);
            }
            else if(position > currentWeek - 4 && positionOffset > 0.35)
            {
                rightMarker.setVisibility(View.INVISIBLE);
            }
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