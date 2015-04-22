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
import se.greatbrain.sats.model.centerAndRegion.Region;

/**
 * Created by aymenarbi on 21/04/15.
 */
public class CentersResponseHandler {

    private final static String BASE_URL ="https://api2.sats.com/v1.0/se/centers" ;
    private Activity activity;

    public CentersResponseHandler(Activity activity) {
        this.activity = activity;
    }

    public void get()
    {
        Ion.with(activity).load(BASE_URL).asJsonObject().setCallback(new FutureCallback<JsonObject>() {

            @Override
            public void onCompleted(Exception e, JsonObject result) {

                Realm.deleteRealmFile(activity);
                Realm realm = Realm.getInstance(activity);

                JsonArray regions = result.getAsJsonArray("regions");
                    Log.d("api", result.toString());

                for (JsonElement element :regions ) {
                    realm.beginTransaction();
                    try {
                        Region region = realm.createOrUpdateObjectFromJson(Region.class, String.valueOf(element));
                        realm.commitTransaction();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        realm.cancelTransaction();
                    }
                }

                Log.d("api regions", result.toString());
                RealmResults<Region> result1 = realm.where(Region.class)
                        .equalTo("id", "124")
                        .findAll();

                Log.d("api regions",result1.toString());
                realm.close();
            }
        });
    }

}
