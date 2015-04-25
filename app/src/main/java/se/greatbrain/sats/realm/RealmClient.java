package se.greatbrain.sats.realm;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import se.greatbrain.sats.model.realm.TrainingActivity;

public class RealmClient
{
    private static final String TAG = "RealmClient";
    private final Context context;

    public RealmClient(Context context)
    {
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

    public Map<Integer, LinkedHashMap<Integer, List<TrainingActivity>>> getAllActivitiesWithWeek()
    {
        Realm realm = Realm.getInstance(context);
        RealmResults<TrainingActivity> activities = realm.where(TrainingActivity.class).findAll();
        activities.sort("date");
        Map<Integer, LinkedHashMap<Integer, List<TrainingActivity>>> activitiesWithWeek = new
                HashMap<>();
        Calendar calendar = Calendar.getInstance();

        for (TrainingActivity activity : activities)
        {
            Date date = null;
            try
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                date = dateFormat.parse(activity.getDate());
            }
            catch (ParseException e)
            {
                Log.d("DateParse", e.getMessage());
                continue;
            }

            if (date != null)
            {
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

                if (activitiesWithWeek.get(year) == null)
                {
                    LinkedHashMap<Integer, List<TrainingActivity>> trainingActivities = new
                            LinkedHashMap<>();
                    List<TrainingActivity> trainingActivityList = new ArrayList<>();
                    trainingActivityList.add(activity);
                    trainingActivities.put(weekOfYear, trainingActivityList);
                    activitiesWithWeek.put(year, trainingActivities);
                }
                else
                {
                    Map<Integer, List<TrainingActivity>> activitiesMap = activitiesWithWeek.get
                            (year);
                    if(activitiesMap.get(weekOfYear) == null)
                    {
                        List<TrainingActivity> trainingActivityList = new ArrayList<>();
                        trainingActivityList.add(activity);
                        activitiesMap.put(weekOfYear, trainingActivityList);
                    }
                    else
                    {
                        activitiesMap.get(weekOfYear).add(activity);
                    }
                }

            }
        }

        return activitiesWithWeek;
    }
}
