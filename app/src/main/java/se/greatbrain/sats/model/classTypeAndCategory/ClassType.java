package se.greatbrain.sats.model.classTypeAndCategory;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aymenarbi on 22/04/15.
 */
public class ClassType extends RealmObject {

    @PrimaryKey
    private String id;

    private String name;
    private String videoUrl;
    private String description;
    private RealmList<RealmString> classCategoriesIds;
    private RealmList<Profile> profile;

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RealmList<RealmString> getClassCategoriesIds() {
        return classCategoriesIds;
    }

    public void setClassCategoriesIds(RealmList<RealmString> classCategoriesIds) {
        this.classCategoriesIds = classCategoriesIds;
    }

    //    public String[] getClassCategoriesIds() {
//        return classCategoriesIds;
//    }
//
//    public void setClassCategoriesIds(String[] classCategoriesIds) {
//        this.classCategoriesIds = classCategoriesIds;
//    }

    public RealmList<Profile> getProfile() {
        return profile;
    }

    public void setProfile(RealmList<Profile> profile) {
        this.profile = profile;
    }
}
