package se.greatbrain.sats.model.Type;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aymenarbi on 22/04/15.
 */
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
