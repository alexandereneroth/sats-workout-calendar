package se.greatbrain.sats.event;

import se.greatbrain.sats.ActivityWrapper;

public final class ClassDetailButtonClickedEvent
{
    private final ActivityWrapper sourceEvent;

    public ClassDetailButtonClickedEvent(ActivityWrapper sourceEvent)
    {
        this.sourceEvent = sourceEvent;
    }

    public ActivityWrapper getSourceEvent()
    {
        return sourceEvent;
    }
}
