package se.greatbrain.sats.handler;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import se.greatbrain.sats.model.Instructor;

/**
 * Created by aymenarbi on 21/04/15.
 */
public class Instructors {

    private final static String BASE_URL ="https://api2.sats.com/v1.0/se/instructors" ;
    private Activity activity;

    public Instructors(Activity activity) {
        this.activity = activity;
    }

    public void get()
    {
        Ion.with(activity).load(BASE_URL).asJsonObject().setCallback(new FutureCallback<JsonObject>() {

            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Realm realm = Realm.getInstance(activity);

                JsonArray array = result.getAsJsonArray("instructors");

                for (int i = 0; i <array.size() ; i++) {

                    realm.beginTransaction();
                    try {
                        Instructor instructor = realm.createOrUpdateObjectFromJson(Instructor.class, String.valueOf(array.get(i)));
                        realm.commitTransaction();
//                        Log.d("api",result.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
//                        Log.d("api",e.toString());
                        realm.cancelTransaction();
                    }

                }


                RealmResults<Instructor> result2 = realm.where(Instructor.class)
                        .equalTo("name", "Alexander Gustafsson")
                        .findAll();
                Log.d("api",result2.toString());

                Toast.makeText(activity,result2.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
