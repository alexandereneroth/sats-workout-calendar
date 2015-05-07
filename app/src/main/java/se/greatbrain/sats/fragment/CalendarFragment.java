package se.greatbrain.sats.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.greatbrain.sats.R;

public class CalendarFragment extends Fragment
{
    private static final String TAG = "MainActivity";

    public static final int NUM_PAGES = 100;
    public static final int NUM_SIMULTANEOUS_PAGES = 5;

    public ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);

        pagerAdapter = new CalendarPagerAdapter( getFragmentManager() );

        // It is recommended to preload two times, or three times the number of simultaneous pages
        // shown
        pager.setOffscreenPageLimit(NUM_SIMULTANEOUS_PAGES * 2);

        pager.setAdapter(pagerAdapter);
        //TODO set to this week
        pager.setCurrentItem((NUM_PAGES / 2) - (NUM_SIMULTANEOUS_PAGES / 2), false);

        return view;
    }

    public class CalendarPagerAdapter extends FragmentStatePagerAdapter
    {
        public static final String ADAPTER_POSITION = "item_index";

        public CalendarPagerAdapter(FragmentManager fm)
        {
            super( fm );
        }

        @Override
        public Fragment getItem (int position)
        {
            CalendarColumnFragment fragment = new CalendarColumnFragment();

            //TODO provide a GraphColumnFragment with all the data it needs to display:
            // 1) How many activities there are this week?
            // 2) Is the week in the future or in the past, or is it this week?
            // 3) The date string

            Bundle bundle = new Bundle( position );
            bundle.putInt(ADAPTER_POSITION, position );
            fragment.setArguments( bundle );

            return fragment;
        }

        @Override
        public int getCount ()
        {
            return NUM_PAGES;
        }

    /* For multiple pages */

        @Override
        public float getPageWidth (int position)
        {
            return 1f / NUM_SIMULTANEOUS_PAGES;
        }
    }
}
