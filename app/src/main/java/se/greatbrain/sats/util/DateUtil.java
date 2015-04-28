package se.greatbrain.sats.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil
{
    private static final String TAG = "DateUtil";
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yy-MM-dd HH:mm:ss",
            Locale.US);
    private static final Calendar calendar = Calendar.getInstance();


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
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getYearFromDate(Date date)
    {
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static String getListTitleCompleted(String dateString, int week)
    {
        Date date = parseString(dateString);
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

    public static String getCompletedActivityDate(String dateString)
    {
        Date date = parseString(dateString);
        calendar.setTime(date);
        String weekDay = toProperCase(getWeekDayAsString());
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = calendar.get(Calendar.MONTH);

        return weekDay + " " + dayOfMonth + "/" + monthOfYear;
    }

    public static String getListTitlePlanned(String dateString)
    {
        Date date = parseString(dateString);
        calendar.setTime(date);

        String dayOfWeek = toProperCase(getWeekDayAsString());
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String month = toProperCase(getMonthAsString());

        if(dateIsToday(date))
        {
            return "Idag, " + dayOfWeek + " " + dayOfMonth + " " + month;
        }
        else
        {
            return dayOfWeek + " " + dayOfMonth + " " + month;
        }
    }

    private static boolean dateIsToday(Date date)
    {
        calendar.setTime(date);
        Calendar calendarToday = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int yearToday = calendarToday.get(Calendar.YEAR);

        if(year == yearToday)
        {
            if(calendar.get(Calendar.DAY_OF_MONTH) == calendarToday.get(Calendar.DAY_OF_MONTH))
            {
                return true;
            }
        }

        return false;
    }

    private static String getMonthAsString()
    {
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    private static String getWeekDayAsString()
    {
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    private static String toProperCase(String value)
    {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}