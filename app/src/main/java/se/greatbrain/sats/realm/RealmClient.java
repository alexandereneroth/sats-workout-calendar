package se.greatbrain.sats.realm;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import se.greatbrain.sats.model.realm.TrainingActivity;

/**
 * Created by patrikappelqvist on 15-04-24.
 */
public class RealmClient
{
    private static final String TAG = "RealmClient";
    private final Context context;
    private final Realm realm;

    public RealmClient(Context context)
    {
        realm = Realm.getInstance(context.getApplicationContext());
        this.context = context.getApplicationContext();
    }

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

    public SparseArray<List<TrainingActivity>> getAllActivitiesWithWeek()
    {
        RealmResults<TrainingActivity> activities = realm.where(TrainingActivity.class).findAll();
        SparseArray<List<TrainingActivity>> activitiesWithWeek = new SparseArray<>();

        for(TrainingActivity activity : activities)
        {
            Date date = null;
            try
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = dateFormat.parse(activity.getDate());
            }
            catch (ParseException e)
            {
                Log.d("DateParse", e.getMessage());
                continue;
            }

            if(date != null)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if(activitiesWithWeek.get(calendar.get(Calendar.WEEK_OF_YEAR)) == null)
                {
                    List<TrainingActivity> trainingActivities = new ArrayList<>();
                    trainingActivities.add(activity);
                    activitiesWithWeek.put(calendar.get(Calendar.WEEK_OF_YEAR), trainingActivities);
                    Log.d("RealmDate", calendar.get(Calendar.WEEK_OF_YEAR) + ": " + activity
                            .getDate());
                }
                else
                {
                    activitiesWithWeek.get(calendar.get(Calendar.WEEK_OF_YEAR)).add(activity);
                    Log.d("RealmDate", calendar.get(Calendar.WEEK_OF_YEAR) + ": " + activity
                            .getDate());
                }

            }
        }

        return activitiesWithWeek;
    }
}
