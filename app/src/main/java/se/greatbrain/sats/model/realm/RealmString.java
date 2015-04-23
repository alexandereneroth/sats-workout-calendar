package se.greatbrain.sats.model.realm;

import io.realm.RealmObject;

public class RealmString extends RealmObject {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
