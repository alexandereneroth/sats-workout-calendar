package se.greatbrain.sats.handler;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import io.realm.Realm;

/**
 * Created by aymenarbi on 21/04/15.
 */
public class ClassTypesResponseHandler {

    private final static String BASE_URL ="https://api2.sats.com/v1.0/se/classtypes" ;
    private Activity activity;

    public ClassTypesResponseHandler(Activity activity) {
        this.activity = activity;
    }

    public void get()
    {
        Ion.with(activity).load(BASE_URL).asJsonObject().setCallback(new FutureCallback<JsonObject>() {

            @Override
            public void onCompleted(Exception e, JsonObject result) {

                Realm realm = Realm.getInstance(activity);

//                Toast.makeText(activity,result.toString(),Toast.LENGTH_LONG).show();
                Log.d("api",result.toString());
            }
        });
    }



}
