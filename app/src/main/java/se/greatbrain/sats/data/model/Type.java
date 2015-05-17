package se.greatbrain.sats.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Type extends RealmObject
{
    @PrimaryKey
    private String name;
    private String subType;
    private String type;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSubType() {
        return subType;
    }
    public void setSubType(String subName) {
        this.subType = subName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
