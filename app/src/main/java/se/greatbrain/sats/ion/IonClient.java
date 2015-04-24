package se.greatbrain.sats.ion;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;
import se.greatbrain.sats.model.realm.ClassCategory;
import se.greatbrain.sats.model.realm.ClassType;
import se.greatbrain.sats.model.realm.Instructor;
import se.greatbrain.sats.model.realm.Region;
import se.greatbrain.sats.model.realm.TrainingActivity;
import se.greatbrain.sats.model.realm.Type;
import se.greatbrain.sats.realm.RealmClient;

/**
 * Created by patrikappelqvist on 15-04-24.
 */
public class IonClient
{
    private final static String ACTIVITIES_URL = "http://sats-greatbrain.rhcloud" +
            ".com/se/training/activities";
    private final static String CLASSTYPES_URL = "https://api2.sats.com/v1.0/se/classtypes";
    private final static String CENTERS_URL = "https://api2.sats.com/v1.0/se/centers";
    private final static String CLASS_CATEGORY_URL = "https://api2.sats.com/v1.0/se/classtypes";
    private final static String INSTRUCTOR_URL = "https://api2.sats.com/v1.0/se/instructors";
    private final static String TYPES_URL = "http://sats-greatbrain.rhcloud" +
            ".com/se/training/activities/types";

    private final Context context;
    private static IonClient INSTANCE;


    public IonClient(Context context)
    {
        this.context = context;
    }

    public static IonClient getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new IonClient(context.getApplicationContext());
        }

        return INSTANCE;
    }

    public void getAllData()
    {
        getAllActivities();
        getAllCenters();
        getAllClassCategories();
        getAllClassTypes();
        getAllInstructors();
        getAllTypes();
    }

    private void getAllActivities()
    {
        Ion.with(context).load(ACTIVITIES_URL).asJsonArray().setCallback(
                new FutureCallback<JsonArray>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonArray result)
                    {
                        RealmClient.addDataToDB(result, context, TrainingActivity.class);
                    }
                });
    }

    private void getAllClassTypes()
    {
        Ion.with(context).load(CLASSTYPES_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        JsonArray classTypes = result.getAsJsonArray("classTypes");
                        JsonArray classTypesWithObjects = new JsonArray();
                        for(JsonElement element : classTypes)
                        {
                            JsonObject object = element.getAsJsonObject();
                            JsonArray classCategories = object.get("classCategories")
                                    .getAsJsonArray();
                            JsonArray classCategoryObjects = new JsonArray();
                            for(JsonElement id : classCategories)
                            {
                                JsonObject jsonCategory = new JsonObject();
                                jsonCategory.add("id", id.getAsJsonPrimitive());
                                classCategoryObjects.add(jsonCategory);
                            }
                            object.remove("classCategories");
                            object.add("classCategories", classCategoryObjects);
                            classTypesWithObjects.add(object);
                        }

                        RealmClient.addDataToDB(classTypesWithObjects, context, ClassType.class);
                    }
                });
    }

    private void getAllCenters()
    {
        Ion.with(context).load(CENTERS_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        JsonArray regions = result.getAsJsonArray("regions");
                        RealmClient.addDataToDB(regions, context, Region.class);
                    }
                });
    }

    private void getAllClassCategories()
    {
        Ion.with(context).load(CLASS_CATEGORY_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        JsonArray classCategories = result.getAsJsonArray("classCategories");
                        RealmClient.addDataToDB(classCategories, context, ClassCategory.class);
                    }
                });
    }

    private void getAllInstructors()
    {
        Ion.with(context).load(INSTRUCTOR_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        JsonArray instructors = result.getAsJsonArray("instructors");
                        RealmClient.addDataToDB(instructors, context, Instructor.class);
                    }
                });
    }

    private void getAllTypes()
    {
        Ion.with(context).load(TYPES_URL).asJsonArray().setCallback(new FutureCallback<JsonArray>
                ()
        {
            @Override
            public void onCompleted(Exception e, JsonArray result)
            {
                RealmClient.addDataToDB(result, context, Type.class);
            }
        });
    }
}
