package se.greatbrain.sats;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashSet;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.event.IonCallCompleteEvent;
import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.ion.IonClient;

public class MainActivity extends ActionBarActivity
{
    private static final String TAG_LOG = "MainActivity";
    private MenuItem reloadButton;
    private WorkoutListFragment workoutListFragment;
    private HashSet<String> finishedIonCalls = new HashSet<>();
    private boolean errorMessageNotShown = true;
    private int numberOfErrors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadJsonDataFromWeb();
        EventBus.getDefault().register(this);

        FragmentManager manager = getFragmentManager();
        workoutListFragment = new WorkoutListFragment();
        manager.beginTransaction().add(R.id.bottom_fragment_container,
                workoutListFragment).commit();
    }

    @Override
    protected void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void loadJsonDataFromWeb()
    {
        Log.d(TAG_LOG, "loadJsonDataFromWeb");
        IonClient.getInstance(this).getAllData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        View actionBarView = getLayoutInflater().inflate(R.layout.action_bar_menu, null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        reloadButton = menu.findItem(R.id.action_bar_refresh_button);
        setupReloadItemMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_bar_refresh_button)
        {
            setupReloadItemMenu();
            loadJsonDataFromWeb();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupReloadItemMenu()
    {
        Animation reloadAnimation = AnimationUtils.loadAnimation(this, R.anim.reload_rotate);

        if(finishedIonCalls.size() == 0 && numberOfErrors < 6)
        {
            reloadButton.setActionView(R.layout.action_bar_reloading);
            ImageView imageView = (ImageView) reloadButton.getActionView()
                    .findViewById(R.id.action_bar_refresh_button_reloading);
            imageView.startAnimation(reloadAnimation);
            numberOfErrors = 0;
        }
        else
        {
            numberOfErrors = 0;
        }
    }

    public void onEventMainThread(IonCallCompleteEvent event)
    {
        if (event.getSourceEvent().contains("error"))
        {
            numberOfErrors++;

            if(errorMessageNotShown)
            {
                Toast.makeText(this, "Server connection failed, please refresh", Toast.LENGTH_LONG).show();
                errorMessageNotShown = false;
            }
        }

        if (finishedIonCalls.add(event.getSourceEvent()))
        {
            if (finishedIonCalls.size() == 6)
            {
                finishedIonCalls.clear();
                errorMessageNotShown = true;
                updateWorkoutListFragment();
            }
        }
    }

    private void updateWorkoutListFragment()
    {
        reloadButton.setActionView(null);
        workoutListFragment.refreshList();
    }
}
