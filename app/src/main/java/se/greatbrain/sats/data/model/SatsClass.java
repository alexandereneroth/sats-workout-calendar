package se.greatbrain.sats.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SatsClass extends RealmObject {

    @PrimaryKey
    private String id;

    private String centerFilterId;
    private String classTypeId;
    private int durationInMinutes;
    private String instructorId;
    private String name;
    private String startTime;
    private int bookedPersonsCount;
    private int maxPersonsCount;
    private String regionId;
    private int waitingListCount;
    private RealmList<ClassCategoryIds> classCategoryIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenterFilterId() {
        return centerFilterId;
    }

    public void setCenterFilterId(String centerFilterId) {
        this.centerFilterId = centerFilterId;
    }

    public String getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(String classTypeId) {
        this.classTypeId = classTypeId;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getBookedPersonsCount() {
        return bookedPersonsCount;
    }

    public void setBookedPersonsCount(int bookedPersonsCount) {
        this.bookedPersonsCount = bookedPersonsCount;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public int getWaitingListCount() {
        return waitingListCount;
    }

    public void setWaitingListCount(int waitingListCount) {
        this.waitingListCount = waitingListCount;
    }

    public void setMaxPersonsCount(int maxPersonsCount)
    {
        this.maxPersonsCount = maxPersonsCount;
    }

    public int getMaxPersonsCount()
    {

        return maxPersonsCount;
    }

    public RealmList<ClassCategoryIds> getClassCategoryIds() {
        return classCategoryIds;
    }

    public void setClassCategoryIds(RealmList<ClassCategoryIds> classCategoryIds) {
        this.classCategoryIds = classCategoryIds;
    }
}
