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
import se.greatbrain.sats.model.realm.ClassType;

/**
 * Created by aymenarbi on 21/04/15.
 */
public class ClassTypesResponseHandler
{

    private final static String BASE_URL = "https://api2.sats.com/v1.0/se/classtypes";
    private Activity activity;

    public ClassTypesResponseHandler(Activity activity)
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

                        JsonArray classTypes = result.getAsJsonArray("classTypes");
//                        Log.d("api classTypes", classTypes.toString());

                        for (JsonElement element : classTypes)
                        {
                            realm.beginTransaction();
                            JsonObject obj = element.getAsJsonObject();

                            try
                            {
                                ClassType classType = realm.createOrUpdateObjectFromJson(
                                        ClassType.class, String.valueOf(element));
                                realm.commitTransaction();
                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                                realm.cancelTransaction();

                            }
                        }

                        RealmResults<ClassType> result2 = realm.where(ClassType.class)
//                        .equalTo("id", "6")
                                .findAll();
                        Log.d("api classTypes", result2.toString());

                        Toast.makeText(activity, result2.toString(), Toast.LENGTH_LONG).show();
                        realm.close();
                    }
                });
    }
}
