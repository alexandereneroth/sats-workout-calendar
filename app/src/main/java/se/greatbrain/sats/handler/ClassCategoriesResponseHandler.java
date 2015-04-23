package se.greatbrain.sats.handler;

import android.app.Activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import se.greatbrain.sats.model.realm.ClassCategory;

public class ClassCategoriesResponseHandler
{

    private final static String BASE_URL = "https://api2.sats.com/v1.0/se/classtypes";
    private Activity activity;

    public ClassCategoriesResponseHandler(Activity activity)
    {
        this.activity = activity;
    }

    public void get()
    {
        Ion.with(activity).load(BASE_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {

                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        Realm realm = Realm.getInstance(activity);
                        JsonArray classCategories = result.getAsJsonArray("classCategories");

                        for (JsonElement element : classCategories)
                        {
                            realm.beginTransaction();
                            try
                            {
                                ClassCategory classCategory = realm.createOrUpdateObjectFromJson(
                                        ClassCategory.class, String.valueOf(element));
                                realm.commitTransaction();
                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                                realm.cancelTransaction();
                            }
                        }

                        RealmResults<ClassCategory> result2 = realm.where(ClassCategory.class)
//                        .equalTo("name", "Running")
                                .findAll();
//                        Log.d("api classCategories", result2.toString());
                        realm.close();
                    }
                });
    }
}
