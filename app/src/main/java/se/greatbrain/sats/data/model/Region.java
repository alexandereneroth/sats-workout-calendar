package se.greatbrain.sats.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Region extends RealmObject {

    @PrimaryKey
    private String id;

    private String name;
    private RealmList<Center> centers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Center> getCenters() {
        return centers;
    }

    public void setCenters(RealmList<Center> centers) {
        this.centers = centers;
    }
}
