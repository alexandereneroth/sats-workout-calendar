package se.greatbrain.sats.ui.calendar;

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
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.R;
import se.greatbrain.sats.event.CalendarColumnClickedEvent;
import se.greatbrain.sats.event.MyTrainingRefreshEvent;
import se.greatbrain.sats.event.WorkoutListScrollEvent;

public class CalendarFragment extends Fragment
{
    private static final String TAG = "CalendarFragment";
    public static final int NUM_SIMULTANEOUS_PAGES = 5;
    /**
     * Static rows are: the top row, the date row, and the zero row(it is half the size of other
     * rows, hence the .5 decimal).
     */
    public static final float NUM_STATIC_ROWS = 2.5F;

    private ImageView backToCurrentWeekFloatingMarker_left;
    private ImageView backToCurrentWeekFloatingMarker_right;
    private View shadowOverlay;

    public ViewPager pager;

    private boolean showMarker = false;
    private static float calendarHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {

        calendarHeight = getResources().getDimension(R.dimen.calendar_height);

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        pager = (ViewPager) view.findViewById(R.id.fragment_calendar_view_pager);

        backToCurrentWeekFloatingMarker_left = (ImageView) view.findViewById(
                R.id.fragment_calendar_back_to_current_week_left_button);
        backToCurrentWeekFloatingMarker_right = (ImageView) view.findViewById(
                R.id.fragment_calendar_back_to_current_week_right_button);
        shadowOverlay = (View) view.findViewById(
                R.id.fragment_calendar_center_focus_shadow);

        EventBus.getDefault().register(this);

        final CalendarPagerAdapter pagerAdapter = new CalendarPagerAdapter(getFragmentManager(),
                getActivity());

        backToCurrentWeekFloatingMarker_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pager.setCurrentItem(pagerAdapter.getPositionOfCurrentWeek_inCalendar(), true);
            }
        });

        backToCurrentWeekFloatingMarker_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pager.setCurrentItem(pagerAdapter.getPositionOfCurrentWeek_inCalendar(), true);
            }
        });

        setShadowOverlayHeightAndTopInset();

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
    }

    private void setShadowOverlayHeightAndTopInset()
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shadowOverlay
                .getLayoutParams();
        int calendarHeight = getResources().getDimensionPixelSize(R.dimen.calendar_height);
        int rowHeight = getHeightOfOneRow();
        int shadowHeight = calendarHeight - Math.round(rowHeight * 1.5f);
        int shadowTopInset = rowHeight / 2;

        params.height = shadowHeight;
        params.topMargin = shadowTopInset;
        shadowOverlay.setLayoutParams(params);
    }

    private void setMarkerVisibility(int position, float positionOffset, CalendarPagerAdapter
            pagerAdapter)
    {
        final int topPadding = (int) Math.round(getHeightOfOneRow() / 2.7);
        boolean rightMarkerShouldBeVisible = position > pagerAdapter
                .getPositionOfCurrentWeek_inCalendar() + 1 && (positionOffset >
                0.5 || showMarker);
        boolean leftMarkerShouldBeVisible = position < pagerAdapter
                .getPositionOfCurrentWeek_inCalendar() - 2 &&
                (positionOffset < 0.5 || showMarker);

        if (rightMarkerShouldBeVisible)
        {
            backToCurrentWeekFloatingMarker_right.setVisibility(View.INVISIBLE);
            backToCurrentWeekFloatingMarker_left.setVisibility(View.VISIBLE);
            backToCurrentWeekFloatingMarker_left.setPaddingRelative(0, topPadding, 0, 0);
            showMarker = true;
        }
        else if (leftMarkerShouldBeVisible)
        {
            backToCurrentWeekFloatingMarker_right.setVisibility(View.VISIBLE);
            backToCurrentWeekFloatingMarker_left.setVisibility(View.INVISIBLE);
            backToCurrentWeekFloatingMarker_right.setPaddingRelative(0, topPadding, 0, 0);
            showMarker = true;
        }
        else
        {
            backToCurrentWeekFloatingMarker_right.setVisibility(View.INVISIBLE);
            backToCurrentWeekFloatingMarker_left.setVisibility(View.INVISIBLE);
            showMarker = false;
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

    public static int getHeightOfOneRow()
    {
        return Math.round(calendarHeight / (CalendarPagerAdapter.numRows + NUM_STATIC_ROWS));
    }
}