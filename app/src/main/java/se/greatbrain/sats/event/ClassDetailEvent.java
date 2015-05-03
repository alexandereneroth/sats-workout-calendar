package se.greatbrain.sats.event;

import se.greatbrain.sats.ActivityWrapper;

public class ClassDetailEvent
{
    private final ActivityWrapper sourceEvent;

    public ClassDetailEvent(ActivityWrapper sourceEvent)
    {
        this.sourceEvent = sourceEvent;
    }

    public ActivityWrapper getSourceEvent()
    {
        return sourceEvent;
    }
}
