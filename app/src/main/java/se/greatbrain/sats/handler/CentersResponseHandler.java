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
import se.greatbrain.sats.model.realm.Region;

public class CentersResponseHandler
{

    private final static String BASE_URL = "https://api2.sats.com/v1.0/se/centers";
    private Activity activity;

    public CentersResponseHandler(Activity activity)
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

                        JsonArray regions = result.getAsJsonArray("regions");

                        for (JsonElement element : regions)
                        {
                            realm.beginTransaction();
                            try
                            {
                                Region region = realm.createOrUpdateObjectFromJson(Region.class,
                                        String.valueOf(element));
                                realm.commitTransaction();
                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                                realm.cancelTransaction();
                            }
                        }

                        RealmResults<Region> result1 = realm.where(Region.class)
//                        .equalTo("id", "124")
                                .findAll();

//                for(Region region : result1)
//                {
//                    for (Center center : region.getCenters())
//                    {
//                        Log.d("api centers", center.toString());
//
//                    }
//                }

//                        Log.d("api centers", result1.toString());
                        realm.close();
                    }
                });
    }
}
