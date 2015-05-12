package se.greatbrain.sats.event;

public final class ScrollEvent
{
    private final int position;

    public ScrollEvent(int position)
    {
        this.position = position;
    }

    public int getPosition()
    {
        return position;
    }
}
