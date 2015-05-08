package se.greatbrain.sats.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.greatbrain.sats.ActivityWrapper;
import se.greatbrain.sats.R;
import se.greatbrain.sats.model.CalendarDate;
import se.greatbrain.sats.model.WeekAndDate;
import se.greatbrain.sats.model.realm.TrainingActivity;
import se.greatbrain.sats.realm.RealmClient;
import se.greatbrain.sats.util.DateUtil;

public class CalendarFragment extends Fragment
{
    private static final String TAG = "MainActivity";

    public static int NUM_PAGES;
    public static int NUM_ROWS;
    public static final int NUM_SIMULTANEOUS_PAGES = 5;

    public ViewPager pager;
    private PagerAdapter pagerAdapter;
    private List<ActivityWrapper> activities;
    private List<CalendarDate> dates;
    private Map<Integer, Integer> numberOfActivitiesInWeek = new LinkedHashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);
        activities = RealmClient.getInstance(getActivity()).getAllActivitiesWithWeek();
        dates = DateUtil.getDatesInWeekBetween(1990, 2020);
        NUM_PAGES = dates.size();

        mapPositionToNumberOfActivities();

        NUM_ROWS = getHighestActivityCount();

        pagerAdapter = new CalendarPagerAdapter( getFragmentManager(), dates );

        // It is recommended to preload two times, or three times the number of simultaneous pages
        // shown
        pager.setOffscreenPageLimit(NUM_SIMULTANEOUS_PAGES * 2);

        pager.setAdapter(pagerAdapter);

        for(int i = 0; i < dates.size(); i++)
        {
            if(DateUtil.getWeekPointOfTime(dates.get(i)) == 0)
            {
                pager.setCurrentItem(i - (NUM_SIMULTANEOUS_PAGES / 2), false);
                break;
            }
        }

        return view;
    }

    private int getHighestActivityCount()
    {
        int highestCount = 0;
        for(Integer count : numberOfActivitiesInWeek.values())
        {
            if(count > highestCount)
            {
                highestCount = count;
            }
        }

        return highestCount;
    }

    private void mapPositionToNumberOfActivities()
    {
        for(int i = 0; i < activities.size(); i++)
        {
            for(int j = 0; j < dates.size(); j++)
            {
                if(activities.get(i).year == dates.get(j).mYear)
                {
                    if(activities.get(i).week == dates.get(j).mWeek)
                    {
                        if(numberOfActivitiesInWeek.get(j) != null)
                        {
                            int lastNumberOfActivities = numberOfActivitiesInWeek.get(j);
                            int newNumberOfActivities = ++lastNumberOfActivities;
                            numberOfActivitiesInWeek.remove(j);
                            numberOfActivitiesInWeek.put(j, newNumberOfActivities);
                        }
                        else
                        {
                            numberOfActivitiesInWeek.put(j, 1);
                        }
                    }
                }
            }
        }
    }

    public class CalendarPagerAdapter extends FragmentStatePagerAdapter
    {
        public static final String ADAPTER_POSITION = "item_index";
        public static final String DATE_STRING = "date string";
        public static final String NUMBER_OF_ACTIVITIES = "number_activities";
        public static final String POINT_IN_TIME = "week in time";

        public final List<CalendarDate> dates;

        public CalendarPagerAdapter(FragmentManager fm, List<CalendarDate> dates)
        {
            super(fm);
            this.dates = dates;
        }

        @Override
        public Fragment getItem (int position)
        {
            CalendarColumnFragment fragment = new CalendarColumnFragment();

            Bundle bundle = new Bundle( position );

            if(numberOfActivitiesInWeek.get(position) == null)
            {
                bundle.putInt(NUMBER_OF_ACTIVITIES, 0);
            }
            else
            {
                bundle.putInt(NUMBER_OF_ACTIVITIES, numberOfActivitiesInWeek.get(position));
            }

            bundle.putInt(POINT_IN_TIME, DateUtil.getWeekPointOfTime(dates.get(position)));
            bundle.putInt(ADAPTER_POSITION, position );
            bundle.putString(DATE_STRING, dates.get(position).mDate);
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
