package se.greatbrain.sats.event;

public final class WorkoutListScrollEvent
{
    public final int weekHash;

    public WorkoutListScrollEvent(int weekHash)
    {
        this.weekHash = weekHash;
    }
}
