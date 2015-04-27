package se.greatbrain.sats.realm;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import se.greatbrain.sats.Activiteee;
import se.greatbrain.sats.model.realm.ClassCategoryIds;
import se.greatbrain.sats.model.realm.ClassType;
import se.greatbrain.sats.model.realm.Profile;
import se.greatbrain.sats.model.realm.SatsClass;
import se.greatbrain.sats.model.realm.TrainingActivity;
import se.greatbrain.sats.util.DateUtil;

public class RealmClient
{
    private static final String TAG = "RealmClient";
    private final Context context;
    private static RealmClient INSTANCE;

    public RealmClient(Context context)
    {
        this.context = context.getApplicationContext();
    }

    public static RealmClient getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RealmClient(context);
        }

        return INSTANCE;
    }

    public void addDataToDB(JsonArray result, Class type)
    {
        Realm realm = Realm.getInstance(context);

        if (type.equals(ClassType.class))
        {
            realm.beginTransaction();
            realm.clear(Profile.class);
            realm.clear(ClassCategoryIds.class);
            realm.commitTransaction();
        }

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

    public List<Activiteee> getAllActivitiesWithWeek()
    {
        Realm realm = Realm.getInstance(context);
        RealmResults<TrainingActivity> activities = realm.where(TrainingActivity.class).findAll();
        activities.sort("date");
        List<Activiteee> activitiesWithWeek = new ArrayList<>();

        for (TrainingActivity activity : activities)
        {
            Date date = DateUtil.parseString(activity.getDate());

            if (date != null)
            {
                int year = DateUtil.getYearFromDate(date);
                int weekOfYear = DateUtil.getWeekFromDate(date);
                Activiteee activiteee = new Activiteee(year, weekOfYear, activity);
                activitiesWithWeek.add(activiteee);
            }
            else
            {
                Log.d(TAG, "Could not parse date: " + activity.getDate());
            }
        }

        return activitiesWithWeek;
    }
}
