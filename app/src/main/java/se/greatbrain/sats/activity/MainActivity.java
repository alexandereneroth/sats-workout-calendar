package se.greatbrain.sats.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import java.util.List;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.R;
import se.greatbrain.sats.event.JsonParseCompleteEvent;
import se.greatbrain.sats.event.ServerErrorEvent;
import se.greatbrain.sats.fragment.GraphColumnFragment;
import se.greatbrain.sats.fragment.GraphFragment;
import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.ion.IonClient;
import se.greatbrain.sats.model.WeekAndDate;
import se.greatbrain.sats.util.DateUtil;

public class MainActivity extends ActionBarActivity implements GraphColumnFragment.OnPageClickedListener
{
    private static final String TAG = "MainActivity";
    private MenuItem reloadButton;
    private WorkoutListFragment workoutListFragment;
    private GraphFragment graphFragment;
    private HashSet<String> finishedJsonParseEvents = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadJsonDataFromWeb();
        EventBus.getDefault().register(this);

        FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentManager supportManager = getSupportFragmentManager();

        workoutListFragment = new WorkoutListFragment();
        graphFragment = new GraphFragment();
        supportManager.beginTransaction()
                .add(R.id.top_fragment_container, graphFragment)
                .add(R.id.bottom_fragment_container, workoutListFragment)
                .commit();
    }

    @Override
    protected void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void loadJsonDataFromWeb()
    {
        Log.d(TAG, "loadJsonDataFromWeb");
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

        // remove left actionbar padding
        android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar)
                actionBarView.getParent();
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
        reloadButton.setActionView(R.layout.action_bar_reloading);

        ImageView imageView = (ImageView) reloadButton.getActionView()
                .findViewById(R.id.action_bar_refresh_button_reloading);

        imageView.startAnimation(reloadAnimation);
    }

    public void onEventMainThread(JsonParseCompleteEvent event)
    {
        Log.d("jsonEvent", event.getSourceEvent());
        if (finishedJsonParseEvents.add(event.getSourceEvent()))
        {
            if (finishedJsonParseEvents.size() == 6)
            {
                finishedJsonParseEvents.clear();
                updateWorkoutListFragment();
            }
        }
    }

    public void onEventMainThread(ServerErrorEvent event)
    {
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void updateWorkoutListFragment()
    {
        reloadButton.setActionView(null);
        workoutListFragment.refreshList();
    }

    @Override
    public void onPageClicked (int page)
    {
        graphFragment.mPager.setCurrentItem(page - (graphFragment.NUM_SIMULTANEOUS_PAGES / 2),
                true);
        Log.d(TAG, "Page: " + page);
    }
}
