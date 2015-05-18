package se.greatbrain.sats.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Booking extends RealmObject {

    @PrimaryKey
    private String id;

    private String centerId;
    private int positionInQueue;
    private String status;
    private SatsClass satsClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
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
