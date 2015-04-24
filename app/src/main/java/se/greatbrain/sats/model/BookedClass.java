package se.greatbrain.sats.model;

import se.greatbrain.sats.ActivityType;
import se.greatbrain.sats.model.realm.Booking;
import se.greatbrain.sats.model.realm.Center;
import se.greatbrain.sats.model.realm.ClassType;
import se.greatbrain.sats.model.realm.Instructor;
import se.greatbrain.sats.model.realm.SatsClass;
import se.greatbrain.sats.model.realm.TrainingActivity;

/**
 * Created by patrikappelqvist on 15-04-24.
 */
public final class BookedClass extends ClassModel
{
    public final String hour;
    public final String minute;
    public final int durationInMinutes;
    public final String className;
    public final String instructor;
    public final int waitingListCount;
    public final ClassType classType;

    public BookedClass(ActivityType activityType, String hour, String minute, int durationInMinutes,
            String className, String instructor, int waitingListCount,
            ClassType classType)
    {
        super(activityType);
        this.hour = hour;
        this.minute = minute;
        this.durationInMinutes = durationInMinutes;
        this.className = className;
        this.instructor = instructor;
        this.waitingListCount = waitingListCount;
        this.classType = classType;
    }




}
