package se.greatbrain.sats;

import java.util.Calendar;
import java.util.Date;

import se.greatbrain.sats.model.realm.TrainingActivity;
import se.greatbrain.sats.util.DateUtil;

public class Activiteee
{
    public static final int COMPLETED = 0;
    public static final int PLANNED = 1;
    public static final int GROUP = 2;
    public static final int PRIVATE = 3;


    public final int year;
    public final int week;
    public final int activityType;
    public final int activityStatus;
    public final TrainingActivity trainingActivity;

    public Activiteee(final int year, final int week, TrainingActivity activity)
    {
        this.year = year;
        this.week = week;
        this.trainingActivity = activity;

        if (activity.getStatus().equalsIgnoreCase("completed"))
        {
            activityStatus = COMPLETED;
        }
        else
        {
            activityStatus = PLANNED;
        }
        if (activity.getType().equalsIgnoreCase("group"))
        {
            activityType = GROUP;
        }
        else
        {
            activityType = PRIVATE;
        }
    }

    public boolean dateHasPassed()
    {
        Calendar calendar = Calendar.getInstance();
        Calendar activityCalendar = Calendar.getInstance();
        Date activityDate = DateUtil.parseString(trainingActivity.getDate());
        activityCalendar.setTime(activityDate);
        return calendar.after(activityCalendar);
    }
}
