package se.greatbrain.sats.handler;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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
//                Toast.makeText(activity, result.toString(), Toast.LENGTH_LONG).show();
                Log.d("api", result.toString());
            }
        });
    }

}
