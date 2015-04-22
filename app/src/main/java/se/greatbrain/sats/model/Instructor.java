package se.greatbrain.sats.model;

import io.realm.RealmObject;
import io.realm.annotations.*;

/**
 * Created by aymenarbi on 21/04/15.
 */
public class Instructor extends RealmObject {

    @PrimaryKey
    private String id;

    private String name;

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
