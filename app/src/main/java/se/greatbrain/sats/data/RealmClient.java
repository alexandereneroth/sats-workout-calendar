package se.greatbrain.sats.data;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import se.greatbrain.sats.data.model.Center;
import se.greatbrain.sats.data.model.ClassCategoryIds;
import se.greatbrain.sats.data.model.ClassType;
import se.greatbrain.sats.data.model.Profile;
import se.greatbrain.sats.data.model.TrainingActivity;
import se.greatbrain.sats.util.DateUtil;

public class RealmClient
{
    private static final String TAG = "RealmClient";
    private final Context context;
    private static RealmClient INSTANCE;

    private RealmClient(Context context)
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

    public void addDataToDB(final JsonArray result, final Class type)
    {
        Realm realm = Realm.getInstance(context);

        if (type == ClassType.class)
        {
            realm.beginTransaction();
            realm.clear(Profile.class);
            realm.clear(ClassCategoryIds.class);
            realm.commitTransaction();
        }

        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                for (JsonElement element : result)
                {
                    realm.createOrUpdateObjectFromJson(type, String.valueOf(element));
                }
            }
        });

        realm.close();
    }

    public List<ActivityWrapper> getAllActivitiesWithWeek()
    {
        Realm realm = Realm.getInstance(context);
        RealmResults<TrainingActivity> activities = realm.where(TrainingActivity.class).findAll();
        activities.sort("date");

        List<ActivityWrapper> activitiesWithWeek = new ArrayList<>();

        for (TrainingActivity activity : activities)
        {
            Date date = DateUtil.parseString(activity.getDate());

            if (date != null)
            {
                int year = DateUtil.getYearFromDate(date);
                int weekOfYear = DateUtil.getWeekFromDate(date);
                ActivityWrapper activityWrapper = new ActivityWrapper(year, weekOfYear, activity);
                activitiesWithWeek.add(activityWrapper);
            }
            else
            {
                Log.d(TAG, "Could not parse date: " + activity.getDate());
            }
        }

        return activitiesWithWeek;
    }

    public RealmResults<Center> getAllCenters()
    {
        Realm realm = Realm.getInstance(context);
        return  realm.where(Center.class).findAll();
    }
}
