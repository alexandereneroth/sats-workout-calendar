package se.greatbrain.sats.event;

import java.util.List;

import se.greatbrain.sats.ActivityWrapper;

public final class RealmUpdateCompleteEvent
{
    private final String sourceEvent;
    private final List<ActivityWrapper> activityWrappers;

    public RealmUpdateCompleteEvent(String sourceEvent, List<ActivityWrapper> activityWrappers)
    {
        this.sourceEvent = sourceEvent;
        this.activityWrappers = activityWrappers;
    }

    public String getSourceEvent()
    {
        return sourceEvent;
    }

    public List<ActivityWrapper> getActivityWrappers()
    {
        return activityWrappers;
    }
}
