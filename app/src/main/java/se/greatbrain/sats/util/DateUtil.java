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

    public static int getStartDateForWeek(int week, int year)
    {
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
