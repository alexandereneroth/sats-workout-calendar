package se.greatbrain.sats.model;

import se.greatbrain.sats.ActivityType;

/**
 * Created by patrikappelqvist on 15-04-24.
 */
public abstract class ClassModel
{
    public final ActivityType activityType;

    protected ClassModel(ActivityType activityType)
    {
        this.activityType = activityType;
    }
}
