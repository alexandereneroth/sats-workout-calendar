package se.greatbrain.sats.event;

public final class JsonParseCompleteEvent
{
    private final String sourceEvent;

    public JsonParseCompleteEvent(String sourceEvent)
    {
        this.sourceEvent = sourceEvent;
    }

    public String getSourceEvent()
    {
        return sourceEvent;
    }
}
