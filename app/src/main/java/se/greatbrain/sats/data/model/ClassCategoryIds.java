package se.greatbrain.sats.data.model;

import io.realm.RealmObject;

public class ClassCategoryIds extends RealmObject {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
