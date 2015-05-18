package se.greatbrain.sats.data;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.event.IonCallCompleteEvent;
import se.greatbrain.sats.data.model.ClassCategory;
import se.greatbrain.sats.data.model.ClassType;
import se.greatbrain.sats.data.model.Instructor;
import se.greatbrain.sats.data.model.Region;
import se.greatbrain.sats.data.model.TrainingActivity;
import se.greatbrain.sats.data.model.Type;
import se.greatbrain.sats.util.Constants;

public class IonClient
{
    private static final String TAG = "IonClient";

    private static final EventBus bus = EventBus.getDefault();

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
        getAllClassTypes();
        getAllActivities();
        getAllCenters();
        getAllClassCategories();
        getAllInstructors();
        getAllTypes();
    }

    private void getAllActivities()
    {
        Ion.with(context).load(Constants.ACTIVITIES_URL).asJsonArray().setCallback(
                new FutureCallback<JsonArray>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonArray result)
                    {
                        if (e == null)
                        {
                            new DbWriteTask(result, TrainingActivity.class).execute();
                        }
                        else
                        {
                            bus.post(new IonCallCompleteEvent("error Activities"));
                        }
                    }
                });
    }

    private void getAllClassTypes()
    {
        Ion.with(context).load(Constants.CLASSTYPES_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonObject result)
                    {
                        if (e == null)
                        {
                            JsonArray classTypes = result.getAsJsonArray("classTypes");
                            classTypes = JsonParser.refactorClassTypes(classTypes);

                            new DbWriteTask(classTypes, ClassType.class).execute();
                        }
                        else
                        {
                            bus.post(new IonCallCompleteEvent("error Classtypes"));
                        }
                    }
                });
    }

    private void getAllCenters()
    {
        Ion.with(context).load(Constants.CENTERS_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonObject result)
                    {
                        if (e == null)
                        {
                            JsonArray regions = result.getAsJsonArray("regions");
                            regions = JsonParser.refactorCenters(regions);

                            new DbWriteTask(regions, Region.class).execute();
                        }
                        else
                        {
                            bus.post(new IonCallCompleteEvent("error Centers"));
                        }
                    }
                });
    }

    private void getAllClassCategories()
    {
        Ion.with(context).load(Constants.CLASS_CATEGORY_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonObject result)
                    {
                        if (e == null)
                        {
                            JsonArray classCategories = result.getAsJsonArray("classCategories");
                            new DbWriteTask(classCategories, ClassCategory.class).execute();
                        }
                        else
                        {
                            bus.post(new IonCallCompleteEvent("error ClassCategories"));
                        }
                    }
                });
    }

    private void getAllInstructors()
    {
        Ion.with(context).load(Constants.INSTRUCTOR_URL).asJsonObject().setCallback(
                new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonObject result)
                    {
                        if (e == null)
                        {
                            JsonArray instructors = result.getAsJsonArray("instructors");
                            instructors = JsonParser.refactorInstructors(instructors);

                            new DbWriteTask(instructors, Instructor.class).execute();
                        }
                        else
                        {
                            bus.post(new IonCallCompleteEvent("error Instructors"));
                        }
                    }
                });
    }

    private void getAllTypes()
    {
        Ion.with(context).load(Constants.TYPES_URL).asJsonArray().setCallback(
                new FutureCallback<JsonArray>
                        ()
                {
                    @Override
                    public void onCompleted(final Exception e, final JsonArray result)
                    {
                        if (e == null)
                        {
                            new DbWriteTask(result, Type.class).execute();
                        }
                        else
                        {
                            bus.post(new IonCallCompleteEvent("error Types"));
                        }
                    }
                });
    }

    private final class DbWriteTask extends AsyncTask<Void, Void, Void>
    {
        private final JsonArray array;
        private final Class type;

        private DbWriteTask(JsonArray array, Class type)
        {
            this.array = array;
            this.type = type;
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            RealmClient.getInstance(context).addDataToDB(array, type);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            bus.post(new IonCallCompleteEvent(type.getName()));
        }
    }
}
