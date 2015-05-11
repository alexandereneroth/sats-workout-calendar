package se.greatbrain.sats.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.R;
import se.greatbrain.sats.adapter.CalendarPagerAdapter;
import se.greatbrain.sats.event.RefreshEvent;
import se.greatbrain.sats.util.DateUtil;

public class CalendarFragment extends Fragment
{
    private static final String TAG = "MainActivity";
    public static final int NUM_SIMULTANEOUS_PAGES = 5;

    public ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);

        EventBus.getDefault().register(this);

        CalendarPagerAdapter pagerAdapter = new CalendarPagerAdapter(getFragmentManager(), getActivity());

        // It is recommended to preload two times, or three times the number of simultaneous pages
        // shown
        pager.setOffscreenPageLimit(NUM_SIMULTANEOUS_PAGES * 2);

        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pagerAdapter.getThisWeeksPosition() - NUM_SIMULTANEOUS_PAGES/2, false);

        return view;
    }

    @Override
    public void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(RefreshEvent event)
    {
        CalendarPagerAdapter pagerAdapter = new CalendarPagerAdapter(getFragmentManager(), getActivity());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pagerAdapter.getThisWeeksPosition() - NUM_SIMULTANEOUS_PAGES/2, true);
    }
}