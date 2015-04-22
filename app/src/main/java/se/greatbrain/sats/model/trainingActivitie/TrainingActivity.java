package se.greatbrain.sats.model.trainingActivitie;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aymenarbi on 22/04/15.
 */
public class TrainingActivity extends RealmObject {

    @PrimaryKey
    private String id;

    private Booking booking;
    private String comment;
    private String date;
    private int distanceInKm;
    private int durationInMinutes;
    private String source;
    private String status;
    private String subType;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

        public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(int distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
