package se.greatbrain.sats.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import se.greatbrain.sats.model.TimeOfDay;

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

    public static String getListTitlePlanned(String dateString)
    {
        Date date = parseString(dateString);
        calendar.setTime(date);
        String dayOfWeek = getWeekDayAsString(calendar.get(Calendar.DAY_OF_WEEK));
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String month = getMonthAsString(calendar.get(Calendar.MONTH));

        if (isToday(date))
        {
            return "Idag, " + dayOfWeek + dayOfMonth + " " + month;
        }
        else
        {
            return dayOfWeek + dayOfMonth + " " + month;
        }
    }

    private static boolean isToday(Date date)
    {
        calendar.setTime(date);
        Calendar calendarToday = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) == calendarToday.get(Calendar.DAY_OF_MONTH))
        {
            return true;
        }

        return false;
    }

    private static String getMonthAsString(int month)
    {
        String result = null;
        switch (month)
        {
            case 0:
                result = "Januari ";
                break;
            case 1:
                result = "Februari ";
                break;
            case 2:
                result = "Mars ";
                break;
            case 3:
                result = "April ";
                break;
            case 4:
                result = "Maj ";
                break;
            case 5:
                result = "Juni ";
                break;
            case 6:
                result = "Juli ";
                break;
            case 7:
                result = "Augusti ";
                break;
            case 8:
                result = "September ";
                break;
            case 9:
                result = "Oktober ";
                break;
            case 10:
                result = "November ";
                break;
            case 11:
                result = "December ";
                break;
        }

        return result;
    }

    private static String getWeekDayAsString(int weekDay)
    {
        String result = null;

        switch (weekDay)
        {
            case 1:
                result = "Söndag ";
                break;
            case 2:
                result = "Måndag ";
                break;
            case 3:
                result = "Tisdag ";
                break;
            case 4:
                result = "Onsdag ";
                break;
            case 5:
                result = "Torsdag ";
                break;
            case 6:
                result = "Fredag ";
                break;
            case 7:
                result = "Lördag ";
                break;
        }

        return result;
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
}
