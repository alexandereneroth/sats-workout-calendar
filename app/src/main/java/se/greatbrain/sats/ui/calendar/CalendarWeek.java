package se.greatbrain.sats.ui.calendar;

public class CalendarWeek
{
    public final String mDate;
    public final int mWeek;
    public final int mYear;

    public CalendarWeek(String date, int week, int year)
    {
        this.mDate = date;
        this.mWeek = week;
        this.mYear = year;
    }
}
