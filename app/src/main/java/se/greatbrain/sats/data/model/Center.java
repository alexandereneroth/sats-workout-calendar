package se.greatbrain.sats.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Center extends RealmObject {

    @PrimaryKey
    private String id;

    private boolean availableForOnlineBooking;
    private String description;
    private String filterId;
    private float lat;
    private float lng;
    private String name;
    private String regionId;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAvailableForOnlineBooking() {
        return availableForOnlineBooking;
    }

    public void setAvailableForOnlineBooking(boolean availableForOnlineBooking) {
        this.availableForOnlineBooking = availableForOnlineBooking;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
