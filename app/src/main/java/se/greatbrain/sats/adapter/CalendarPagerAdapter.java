package se.greatbrain.sats.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.greatbrain.sats.ActivityWrapper;
import se.greatbrain.sats.fragment.CalendarColumnFragment;
import se.greatbrain.sats.fragment.CalendarFragment;
import se.greatbrain.sats.model.CalendarDate;
import se.greatbrain.sats.realm.RealmClient;
import se.greatbrain.sats.util.DateUtil;

public class CalendarPagerAdapter extends FragmentStatePagerAdapter
{
    public static final String ADAPTER_POSITION = "item_index";
    public static final String DATE_STRING = "date string";
    public static final String NUMBER_OF_ACTIVITIES = "number_activities";
    public static final String POINT_IN_TIME = "week in time";

    public static int NUM_PAGES;
    public static int NUM_ROWS;
    public static int CURRENT_WEEK;

    private List<ActivityWrapper> activities;
    private List<CalendarDate> dates = new ArrayList<>();
    private Map<Integer, Integer> numberOfActivitiesInWeek = new LinkedHashMap<>();

    public CalendarPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        activities = RealmClient.getInstance(context).getAllActivitiesWithWeek();
        if(activities.size() > 0)
        {
            String fromDate = activities.get(0).trainingActivity.getDate();
            dates = DateUtil.getDatesInWeekBetween(fromDate);
        }

        NUM_PAGES = dates.size();
        mapPositionToNumberOfActivities();
        NUM_ROWS = getHighestActivityCount();
    }

    @Override
    public Fragment getItem(int position)
    {
        CalendarColumnFragment fragment = new CalendarColumnFragment();

        Bundle bundle = new Bundle(position);

        bundle.putInt(NUMBER_OF_ACTIVITIES, getNumberOfActivities(position));
        bundle.putInt(POINT_IN_TIME, DateUtil.getWeekPointOfTime(dates.get(position)));
        bundle.putInt(ADAPTER_POSITION, position);
        bundle.putString(DATE_STRING, dates.get(position).mDate);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount()
    {
        return NUM_PAGES;
    }

    /* For multiple pages */

    @Override
    public float getPageWidth(int position)
    {
        return 1f / CalendarFragment.NUM_SIMULTANEOUS_PAGES;
    }

    public int getThisWeeksPosition()
    {
        for (int i = 0; i < dates.size(); i++)
        {
            if (DateUtil.getWeekPointOfTime(dates.get(i)) == 0)
            {
                CURRENT_WEEK = i;
                break;
            }
        }

        return CURRENT_WEEK;
    }

    private int getNumberOfActivities(int position)
    {
        if (numberOfActivitiesInWeek.get(position) == null)
        {
            return 0;
        }
        else
        {
            return numberOfActivitiesInWeek.get(position);
        }
    }

    private int getHighestActivityCount()
    {
        int highestCount = 0;
        for (Integer count : numberOfActivitiesInWeek.values())
        {
            if (count > highestCount)
            {
                highestCount = count;

                if (highestCount >= 7)
                {
                    return 7;
                }
            }
        }

        if(highestCount < 4)
        {
            return 4;
        }

        return highestCount;
    }

    private void mapPositionToNumberOfActivities()
    {
        for (int i = 0; i < activities.size(); i++)
        {
            for (int j = 0; j < dates.size(); j++)
            {
                if (activities.get(i).year == dates.get(j).mYear)
                {
                    if (activities.get(i).week == dates.get(j).mWeek)
                    {
                        if (numberOfActivitiesInWeek.get(j) != null)
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
}