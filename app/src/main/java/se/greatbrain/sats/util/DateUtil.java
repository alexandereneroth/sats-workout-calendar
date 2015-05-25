package se.greatbrain.sats.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import se.greatbrain.sats.ui.calendar.CalendarWeek;
import se.greatbrain.sats.data.TimeOfDay;

public final class DateUtil
{
    public static final int CURRENT_WEEK = 0;

    private static final String TAG = "DateUtil";
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yy-MM-dd HH:mm:ss",
            Locale.getDefault());

    public static Date parseString(String dateString)
    {
        Date date = null;
        try
        {
            date = FORMATTER.parse(dateString);
        }
        catch (ParseException e)
        {
            Log.d(TAG, e.getMessage());
        }

        return date;
    }

    public static int getWeekFromDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getYearFromDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static String getListTitleCompleted(String dateString)
    {
        Calendar calendar = Calendar.getInstance();
        Date date = parseString(dateString);
        int week = getWeekFromDate(date);

        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        int startDate = calendar.get(Calendar.DAY_OF_MONTH);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        int endDate = calendar.get(Calendar.DAY_OF_MONTH);
        int endMonth = calendar.get(Calendar.MONTH) + 1;

        String listTitle = null;

        if (startMonth == endMonth)
        {
            listTitle = "Vecka " + week + " (" + startDate + "-" + endDate + "/" +
                    startMonth + ")";
        }
        else
        {
            listTitle = "Vecka " + week + " (" + startDate + "/" + startMonth + "-" + endDate +
                    "/" + endMonth + ")";
        }

        return listTitle;
    }

    public static String getPastOrCompletedActivityDate(String dateString)
    {
        Calendar calendar = Calendar.getInstance();
        Date date = parseString(dateString);
        calendar.setTime(date);
        String weekDay = toProperCase(getWeekDayAsString(calendar));
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;

        return weekDay + " " + dayOfMonth + "/" + monthOfYear;
    }

    public static String getListTitlePlanned(String dateString)
    {
        Calendar calendar = Calendar.getInstance();
        Date date = parseString(dateString);
        calendar.setTime(date);

        String dayOfWeek = toProperCase(getWeekDayAsString(calendar));
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String month = toProperCase(getMonthAsString(calendar));

        if (dateIsToday(date))
        {
            return "Idag, " + dayOfWeek + " " + dayOfMonth + " " + month;
        }
        else
        {
            return dayOfWeek + " " + dayOfMonth + " " + month;
        }
    }

    public static boolean dateIsToday(String dateString)
    {
        final Date date = parseString(dateString);
        return dateIsToday(date);
    }

    private static boolean dateIsToday(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendarToday = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int yearToday = calendarToday.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int monthToday = calendarToday.get(Calendar.MONTH);
        if (year == yearToday)
        {
            if(month == monthToday)
            {
                if (calendar.get(Calendar.DAY_OF_MONTH) == calendarToday.get(Calendar.DAY_OF_MONTH))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean dateHasPassed(String dateString)
    {
        Calendar todaysCalendar = Calendar.getInstance();
        Date activitysDate = parseString(dateString);
        Calendar activityCalendar = Calendar.getInstance();
        activityCalendar.setTime(activitysDate);
        return todaysCalendar.after(activityCalendar);
    }

    public static int getWeekPointInTime(CalendarWeek date)
    {
        Calendar todaysCalendar = Calendar.getInstance();
        nullifyTimeAndDayInCalendar(todaysCalendar);
        Calendar datesCalendar = Calendar.getInstance();
        datesCalendar.set(Calendar.YEAR, date.mYear);
        datesCalendar.set(Calendar.WEEK_OF_YEAR, date.mWeek);
        datesCalendar.get(Calendar.YEAR);
        datesCalendar.get(Calendar.WEEK_OF_YEAR);
        nullifyTimeAndDayInCalendar(datesCalendar);

        return datesCalendar.compareTo(todaysCalendar);
    }

    public static TimeOfDay getTimeOfDayFromDate(String dateString)
    {
        Date date = parseString(dateString);
        Calendar datesCalendar = Calendar.getInstance();
        datesCalendar.setTime(date);

        int hourOfDay = datesCalendar.get(Calendar.HOUR_OF_DAY);
        int minuteOfHour = datesCalendar.get(Calendar.MINUTE);

        return new TimeOfDay(hourOfDay, minuteOfHour);
    }

    public static List<CalendarWeek> getWeeksInRangeFromToday(int range)
    {
        Date date = new Date();
        int yearToday = getYearFromDate(date);
        int fromYear = yearToday - range;
        int toYear = yearToday + range;

        List<CalendarWeek> dates = new ArrayList<>();
        for(int i = fromYear; i <= toYear; i++)
        {
            getWeeksWithDatesForYear(i, dates);
        }

        return dates;
    }

    private static void getWeeksWithDatesForYear(int year, List<CalendarWeek> dates)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.set(year, Calendar.JANUARY, 1);
        int numberOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);

        for(int week = 1; week <= numberOfWeeks; week++)
        {
            calendar.set(Calendar.WEEK_OF_YEAR, week);
            calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            int monday = calendar.get(Calendar.DAY_OF_MONTH);
            int startMonth = calendar.get(Calendar.MONTH) + 1;
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            int sunday = calendar.get(Calendar.DAY_OF_MONTH);
            int endMonth = calendar.get(Calendar.MONTH) + 1;

            if(startMonth == endMonth)
            {
                String date = monday + "-" + sunday + "/" + startMonth;
                dates.add(new CalendarWeek(date, week, year));
            }
            else
            {
                String date = monday + "/" + startMonth + "-" + sunday + "/" +
                        endMonth;
                dates.add(new CalendarWeek(date, week, year));
            }
        }
    }

    private static void nullifyTimeAndDayInCalendar(Calendar calendar)
    {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(Calendar.HOUR, 0);
        calendar.get(Calendar.HOUR);
        calendar.set(Calendar.MINUTE, 0);
        calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.SECOND, 0);
        calendar.get(Calendar.SECOND);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.get(Calendar.MILLISECOND);
    }

    private static String getMonthAsString(Calendar calendar)
    {
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    private static String getWeekDayAsString(Calendar calendar)
    {
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    private static String toProperCase(String value)
    {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}
