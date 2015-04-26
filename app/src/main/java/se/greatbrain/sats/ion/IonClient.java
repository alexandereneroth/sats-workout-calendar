package se.greatbrain.sats.ion;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import se.greatbrain.sats.json.JsonParser;
import se.greatbrain.sats.model.realm.ClassCategory;
import se.greatbrain.sats.model.realm.ClassType;
import se.greatbrain.sats.model.realm.Instructor;
import se.greatbrain.sats.model.realm.Region;
import se.greatbrain.sats.model.realm.TrainingActivity;
import se.greatbrain.sats.model.realm.Type;
import se.greatbrain.sats.realm.RealmClient;

public class IonClient
{
    private static final String TAG = "IonClient";
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
        this.context = context.getApplicationContext();
    }

    public static IonClient getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new IonClient(context);
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
                    public void onCompleted(final Exception e, final JsonArray result)
                    {
                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... params)
                            {
                                if(e == null)
                                {
                                    RealmClient.addDataToDB(result, context, TrainingActivity.class);
                                }
                                else
                                {
                                    Log.d(TAG, e.getMessage());
                                }

                                return null;
                            }
                        }.execute();
                    }
                });
    }

    private void getAllClassTypes()
    {
        Ion.with(context).load(CLASSTYPES_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonObject result)
                    {
                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... params)
                            {
                                if(e == null)
                                {
                                    JsonArray classTypes = result.getAsJsonArray("classTypes");
                                    JsonArray reparsedClassTypes = JsonParser.refactorClassTypes(classTypes);

                                    RealmClient.addDataToDB(reparsedClassTypes, context, ClassType.class);
                                }
                                else
                                {
                                    Log.d(TAG, e.getMessage());
                                }

                                return null;
                            }
                        }.execute();
                    }
                });
    }

    private void getAllCenters()
    {
        Ion.with(context).load(CENTERS_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonObject result)
                    {
                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... params)
                            {
                                if(e == null)
                                {
                                    JsonArray regions = result.getAsJsonArray("regions");
                                    regions = JsonParser.refactorCenters(regions);

                                    RealmClient.addDataToDB(regions, context, Region.class);
                                }
                                else
                                {
                                    Log.d(TAG, e.getMessage());
                                }

                                return null;
                            }
                        }.execute();
                    }
                });
    }

    private void getAllClassCategories()
    {
        Ion.with(context).load(CLASS_CATEGORY_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonObject result)
                    {
                        new AsyncTask<Void, Void, Void>()
                        {
                            @Override
                            protected Void doInBackground(Void... params)
                            {
                                if (e == null)
                                {
                                    JsonArray classCategories = result.getAsJsonArray("classCategories");
                                    RealmClient.addDataToDB(classCategories, context, ClassCategory.class);
                                }
                                else
                                {
                                    Log.d(TAG, e.getMessage());
                                }

                                return null;
                            }
                        }.execute();
                    }
                });
    }

    private void getAllInstructors()
    {
        Ion.with(context).load(INSTRUCTOR_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonObject result)
                    {
                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... params)
                            {
                                if(e == null)
                                {
                                    JsonArray instructors = result.getAsJsonArray("instructors");
                                    RealmClient.addDataToDB(instructors, context, Instructor.class);
                                }
                                else
                                {
                                    Log.d(TAG, e.getMessage());
                                }

                                return null;
                            }
                        }.execute();
                    }
                });
    }

    private void getAllTypes()
    {
        Ion.with(context).load(TYPES_URL).asJsonArray().setCallback(new FutureCallback<JsonArray>
                ()
        {
            @Override
            public void onCompleted(final Exception e, final JsonArray result)
            {
                new AsyncTask<Void, Void, Void>(){
                    @Override
                    protected Void doInBackground(Void... params)
                    {
                        if(e == null)
                        {
                            RealmClient.addDataToDB(result, context, Type.class);
                        }
                        else
                        {
                            Log.d(TAG, e.getMessage());
                        }

                        return null;
                    }
                }.execute();
            }
        });
    }
}
