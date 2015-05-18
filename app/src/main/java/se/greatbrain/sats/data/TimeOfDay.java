package se.greatbrain.sats.data;

public class TimeOfDay
{
    public final int hour;
    public final int minute;

    public TimeOfDay(int hour, int minute)
    {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString()
    {
        return "TimeOfDay{" +
                "hour=" + hour +
                ", minute=" + minute +
                '}';
    }

    public String getHourString()
    {
        return getNumberWithLeadingZeroIfLessThanTwoDigits(hour);
    }

    public String getMinuteString()
    {
        return getNumberWithLeadingZeroIfLessThanTwoDigits(minute);
    }

    private String getNumberWithLeadingZeroIfLessThanTwoDigits(int number)
    {
        if (number > 9)
        {
            return String.valueOf(number);
        }
        else
        {
            return "0" + number;
        }
    }
}
