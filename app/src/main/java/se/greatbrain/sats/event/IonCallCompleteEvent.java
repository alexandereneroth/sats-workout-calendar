package se.greatbrain.sats.event;

public final class IonCallCompleteEvent
{
    private final String sourceEvent;

    public IonCallCompleteEvent(String sourceEvent)
    {
        this.sourceEvent = sourceEvent;
    }

    public String getSourceEvent()
    {
        return sourceEvent;
    }
}
