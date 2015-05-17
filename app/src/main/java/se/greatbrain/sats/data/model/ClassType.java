package se.greatbrain.sats.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClassType extends RealmObject {

    @PrimaryKey
    private String id;

    private String name;
    private String videoUrl;
    private String description;
    private RealmList<ClassCategoryIds> classCategories;
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

    public RealmList<ClassCategoryIds> getClassCategories() {
        return classCategories;
    }

    public void setClassCategories(RealmList<ClassCategoryIds> classCategories) {
        this.classCategories = classCategories;
    }

    public RealmList<Profile> getProfile() {
        return profile;
    }

    public void setProfile(RealmList<Profile> profile) {
        this.profile = profile;
    }
}
