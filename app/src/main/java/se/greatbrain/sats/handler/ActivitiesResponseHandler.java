package se.greatbrain.sats.handler;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import se.greatbrain.sats.model.trainingActivitie.TrainingActivity;

/**
 * Created by aymenarbi on 22/04/15.
 */
public class ActivitiesResponseHandler {
    private final static String BASE_URL ="http://sats-greatbrain.rhcloud.com/se/training/activities" ;
    private Activity activity;

    public ActivitiesResponseHandler(Activity activity) {
        this.activity = activity;
    }

    public void get()
    {
        Ion.with(activity).load(BASE_URL).asJsonArray().setCallback(new FutureCallback<JsonArray>() {

            @Override
            public void onCompleted(Exception e, JsonArray result) {

                Realm.deleteRealmFile(activity);
                Realm realm = Realm.getInstance(activity);

                Log.d("api training activities", result.toString());

                for (JsonElement element : result ) {
                    realm.beginTransaction();
                    JsonObject obj = element.getAsJsonObject();

                    try {
                        TrainingActivity trainingActivity = realm.createOrUpdateObjectFromJson(TrainingActivity.class, String.valueOf(element));
                        realm.commitTransaction();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        realm.cancelTransaction();

                    }
                }

                RealmResults<TrainingActivity> result2 = realm.where(TrainingActivity.class)
                        .equalTo("id", "114608")
                        .findAll();
                Log.d("api training activities", result2.toString());

                Toast.makeText(activity, result2.toString(), Toast.LENGTH_LONG).show();
                realm.close();
            }
        });
    }

}
