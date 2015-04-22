package se.greatbrain.sats.model.classTypeAndCategory;

import io.realm.RealmObject;

/**
 * Created by aymenarbi on 22/04/15.
 */
public class RealmString extends RealmObject {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
