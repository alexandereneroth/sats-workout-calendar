package se.greatbrain.sats;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
import se.greatbrain.sats.event.JsonParseCompleteEvent;
import se.greatbrain.sats.event.ServerErrorEvent;
import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.ion.IonClient;

public class MainActivity extends ActionBarActivity
{
    private static final String TAG_LOG = "MainActivity";
    private MenuItem reloadButton;
    private WorkoutListFragment workoutListFragment;
    private HashSet<String> finishedJsonParseEvents = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRealm();
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

    private void setupRealm()
    {
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

        setReloadButtonListener(menu);

        return true;
    }

    public void onEventMainThread(ServerErrorEvent event)
    {
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void setReloadButtonListener(Menu menu)
    {
        final Animation reloadAnimation = AnimationUtils.loadAnimation(this, R.anim.reload_rotate);
        reloadButton = menu.findItem(R.id.action_bar_refresh_button);
        reloadButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                reloadButton.setActionView(R.layout.action_bar_reloading);
                ImageView imageView = (ImageView) reloadButton.getActionView().findViewById(R.id
                        .action_bar_refresh_button_reloading);
                imageView.startAnimation(reloadAnimation);

                return true;
            }
        });
    }

    public void onEventMainThread(JsonParseCompleteEvent event)
    {
        Log.d("jsonEvent", event.getSourceEvent());
        if (finishedJsonParseEvents.add(event.getSourceEvent()))
        {
            if (finishedJsonParseEvents.size() == 6)
            {
                Toast.makeText(this, "Updating list with new data", Toast.LENGTH_LONG).show();
                workoutListFragment.refreshList();
            }
        }
    }
}


