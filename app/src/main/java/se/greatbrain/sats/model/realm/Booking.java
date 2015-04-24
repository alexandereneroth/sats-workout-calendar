package se.greatbrain.sats.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Booking extends RealmObject {

    @PrimaryKey
    private String id;

    private String center;
    private int positionInQueue;
    private String status;
    private SatsClass satsClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public int getPositionInQueue() {
        return positionInQueue;
    }

    public void setPositionInQueue(int positionInQueue) {
        this.positionInQueue = positionInQueue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SatsClass getSatsClass() {
        return satsClass;
    }

    public void setSatsClass(SatsClass satsClass)
    {
        if (satsClass == null)
        {
            satsClass = new SatsClass();
        }
        this.satsClass = satsClass;
    }
}
