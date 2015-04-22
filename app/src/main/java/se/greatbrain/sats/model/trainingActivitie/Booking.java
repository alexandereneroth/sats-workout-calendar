package se.greatbrain.sats.model.trainingActivitie;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aymenarbi on 22/04/15.
 */
public class Booking extends RealmObject {

    @PrimaryKey
    private String id;

    private String center;
    private int positionInQueue;
    private String status;
    private ClassObject classObj;

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

    public ClassObject getClassObj() {
        return classObj;
    }

    public void setClassObj(ClassObject classObj) {
        this.classObj = classObj;
    }
}
