package se.greatbrain.sats.event;

public final class ScrollEvent
{
    public final int mPosition;
    public final int mWeekHash;

    public ScrollEvent(int position, int weekHash)
    {
        this.mPosition = position;
        this.mWeekHash = weekHash;
    }
}
