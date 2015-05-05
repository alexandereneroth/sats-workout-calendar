package se.greatbrain.sats.event;

import se.greatbrain.sats.ActivityWrapper;

public class ClassDetailEvent
{
    private final ActivityWrapper activityWrapper;

    public ClassDetailEvent(ActivityWrapper activityWrapper)
    {
        this.activityWrapper = activityWrapper;
    }

    public ActivityWrapper getActivityWrapper()
    {
        return activityWrapper;
    }
}
