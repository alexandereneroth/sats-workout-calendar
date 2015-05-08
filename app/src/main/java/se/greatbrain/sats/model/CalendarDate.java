package se.greatbrain.sats.model;

public class CalendarDate
{
    public final String mDate;
    public final int mWeek;
    public final int mYear;

    public CalendarDate(String date, int week, int year)
    {
        this.mDate = date;
        this.mWeek = week;
        this.mYear = year;
    }
}
