package se.greatbrain.sats.handler;

import android.app.Activity;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import se.greatbrain.sats.model.realm.Instructor;

public class InstructorsResponseHandler
{

    private final static String BASE_URL = "https://api2.sats.com/v1.0/se/instructors";
    private Activity activity;

    public InstructorsResponseHandler(Activity activity)
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
                        Log.d("api instructors", result.toString());

                        Realm realm = Realm.getInstance(activity);
                        JsonArray instructors = result.getAsJsonArray("instructors");

                        for (JsonElement element : instructors)
                        {

                            realm.beginTransaction();
                            try
                            {
                                Instructor instructor = realm.createOrUpdateObjectFromJson(
                                        Instructor.class, String.valueOf(element));
                                realm.commitTransaction();
                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                                realm.cancelTransaction();

                            }
                        }

                        RealmResults<Instructor> result2 = realm.where(Instructor.class)
//                                .equalTo("name", "Alexander Gustafsson")
                                .findAll();
//                        Log.d("api instructors", result2.toString());

                        realm.close();
                    }
                });
    }
}
