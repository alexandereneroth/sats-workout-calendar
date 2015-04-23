package se.greatbrain.sats.handler;

import android.app.Activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import io.realm.Realm;
import io.realm.RealmResults;
import se.greatbrain.sats.model.realm.Type;

public class TypeResponseHandler {
    private final static String BASE_URL = "http://sats-greatbrain.rhcloud.com/se/training/activities/types";
    private Activity activity;

    public TypeResponseHandler(Activity activity) {
        this.activity = activity;
    }

    public void get() {
        Ion.with(activity).load(BASE_URL).asJsonArray().setCallback(new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray result) {
                Realm realm = Realm.getInstance(activity);

                for (JsonElement element : result) {
                    realm.beginTransaction();
                    try {
                        Type instructor = realm.createOrUpdateObjectFromJson(Type.class, String.valueOf(element));
                        realm.commitTransaction();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        realm.cancelTransaction();
                    }
                }
                RealmResults<Type> types = realm.where(Type.class)
//                        .equalTo("name", "Badminton")
                        .findAll();
//                    Log.d("api type", types.toString());

                realm.close();
            }
        });
    }
}
