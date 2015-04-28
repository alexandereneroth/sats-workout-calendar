package se.greatbrain.sats.event;

public final class ServerErrorEvent
{
    private final String message;

    public ServerErrorEvent(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
