package se.greatbrain.sats;

public enum ActivityType
{
    BookedClass(0), BookedPrivate(1), Completed(2);

    public int id;

    ActivityType(int i)
    {
        this.id = i;
    }

    public static ActivityType getWithId(int number)
    {
        switch (number)
        {
            case 0:
                return BookedClass;
            case 1:
                return BookedPrivate;
            case 2:
                return Completed;
            default:
                throw new IllegalArgumentException("No ActivityType with id: " + number);
        }
    }
}
