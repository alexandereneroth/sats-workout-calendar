package se.greatbrain.sats.data;

import se.greatbrain.sats.data.model.TrainingActivity;
import se.greatbrain.sats.util.DateUtil;

public class ActivityWrapper
{
    public static final int GROUP = 2;
    public static final int PRIVATE = 3;

    public final int year;
    public final int week;
    public final int activityType;
    public final boolean isCompleted;
    public final TrainingActivity trainingActivity;

    public ActivityWrapper(final int year, final int week, TrainingActivity activity)
    {
        this.year = year;
        this.week = week;
        this.trainingActivity = activity;

        isCompleted = activity.getStatus().equalsIgnoreCase("completed");

        if (activity.getType().equalsIgnoreCase("group") && activity.getBooking() != null)
        {
            activityType = GROUP;
        }
        else
        {
            activityType = PRIVATE;
        }
    }

    public boolean isPastOrCompleted()
    {
        return isCompleted || DateUtil.dateHasPassed(trainingActivity.getDate());
    }
}
