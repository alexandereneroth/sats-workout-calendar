package se.greatbrain.sats.ui.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.greatbrain.sats.data.ActivityWrapper;
import se.greatbrain.sats.data.RealmClient;
import se.greatbrain.sats.util.DateUtil;

public class CalendarPagerAdapter extends FragmentStatePagerAdapter
{
    public static final String ADAPTER_POSITION = "item_index";
    public static final String DATE_STRING = "date string";
    public static final String NUMBER_OF_ACTIVITIES = "number_activities";
    public static final String POINT_IN_TIME = "week in time";
    public static final String NEXT_NUMBER_OF_ACTIVITIES = "next activity num";
    public static final String PREVIOUS_NUMBER_OF_ACTIVITIES = "previous activity num";
    public static final String HAS_NEXT_WEEK_PASSED = "has next week passed";

    public static int numPages;
    public static int numRows;
    public static int positionOfCurrentWeekInDates;

    private static final int MAX_ROWS = 7;
    private static final int MIN_ROWS = 4;
    private List<ActivityWrapper> activities;
    private List<CalendarWeek> weeks = new ArrayList<>();
    private Map<Integer, Integer> numberOfActivitiesInWeek = new HashMap<>();

    public CalendarPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        activities = RealmClient.getInstance(context).getAllActivitiesWithWeek();

        weeks = DateUtil.getWeeksInRangeFromToday(10);

        numPages = weeks.size();
        mapPositionToNumberOfActivities();
        positionOfCurrentWeekInDates = getPositionOfCurrentWeek_inDates();
        numRows = getHighestActivityCount();
    }

    @Override
    public Fragment getItem(int position)
    {
        CalendarColumnFragment fragment = new CalendarColumnFragment();

        Bundle bundle = new Bundle(position);

        bundle.putBoolean(HAS_NEXT_WEEK_PASSED, hasNextWeekPassed(position));
        bundle.putInt(NUMBER_OF_ACTIVITIES, getNumberOfActivities(position));
        bundle.putInt(POINT_IN_TIME, DateUtil.getWeekPointInTime(weeks.get(position)));
        bundle.putInt(ADAPTER_POSITION, position);
        bundle.putString(DATE_STRING, weeks.get(position).mDate);
        bundle.putInt(NEXT_NUMBER_OF_ACTIVITIES, getNextWeeksActivityCount(position));
        bundle.putInt(PREVIOUS_NUMBER_OF_ACTIVITIES, getPreviousWeeksActivityCount(position));

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount()
    {
        return numPages;
    }

    /* For multiple pages */

    @Override
    public float getPageWidth(int position)
    {
        return 1f / CalendarFragment.NUM_SIMULTANEOUS_PAGES;
    }

    public int getPositionOfCurrentWeek_inCalendar()
    {
        return positionOfCurrentWeekInDates - CalendarFragment.NUM_SIMULTANEOUS_PAGES / 2;
    }

    private int getPositionOfCurrentWeek_inDates()
    {
        for (int i = 0; i < weeks.size(); i++)
        {
            if (DateUtil.getWeekPointInTime(weeks.get(i)) == DateUtil.CURRENT_WEEK)
            {
                return i;
            }
        }

        return 0;
    }

    public int getWeekHashForPosition(int position)
    {
        CalendarWeek date = weeks.get(position);
        return (date.mYear * 100) + date.mWeek;
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

    private boolean hasNextWeekPassed(int position)
    {
        return position < positionOfCurrentWeekInDates - 1;
    }

    private int getNextWeeksActivityCount(int position)
    {
        if (numberOfActivitiesInWeek.get(position + 1) == null)
        {
            return 0;
        }

        int numberOfActivitiesInNextWeek = numberOfActivitiesInWeek.get(position + 1);

        if(numberOfActivitiesInNextWeek > MAX_ROWS)
        {
            return MAX_ROWS;
        }

        return numberOfActivitiesInNextWeek;
    }

    private int getPreviousWeeksActivityCount(int position)
    {
        if (numberOfActivitiesInWeek.get(position - 1) == null)
        {
            return 0;
        }

        int numberOfActivitiesInPreviousWeek = numberOfActivitiesInWeek.get(position - 1);

        if(numberOfActivitiesInPreviousWeek > MAX_ROWS)
        {
            return MAX_ROWS;
        }

        return numberOfActivitiesInPreviousWeek;
    }

    private int getHighestActivityCount()
    {
        int highestCount = 0;
        for (Integer count : numberOfActivitiesInWeek.values())
        {
            if (count > highestCount)
            {
                highestCount = count;

                if (highestCount >= MAX_ROWS)
                {
                    return MAX_ROWS;
                }
            }
        }

        if(highestCount < MIN_ROWS)
        {
            return MIN_ROWS;
        }

        return highestCount;
    }

    private void mapPositionToNumberOfActivities()
    {
        for (int i = 0; i < activities.size(); i++)
        {
            for (int j = 0; j < weeks.size(); j++)
            {
                if (activities.get(i).year == weeks.get(j).mYear)
                {
                    if (activities.get(i).week == weeks.get(j).mWeek)
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