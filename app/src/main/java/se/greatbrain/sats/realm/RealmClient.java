package se.greatbrain.sats.realm;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import io.realm.Realm;
import io.realm.internal.IOException;

public class RealmClient
{
    private static final String TAG = "RealmClient";

    public static void addDataToDB(JsonArray result, Context context, Class type)
    {
        Realm realm = Realm.getInstance(context);

        for (JsonElement element : result)
        {
            realm.beginTransaction();

            try
            {
                realm.createOrUpdateObjectFromJson(type, String.valueOf(element));
                realm.commitTransaction();
            }
            catch (IOException e)
            {
                realm.cancelTransaction();
            }
        }

        realm.close();
    }
}
