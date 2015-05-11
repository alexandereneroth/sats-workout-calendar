package se.greatbrain.sats.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.R;
import se.greatbrain.sats.adapter.DrawerMenuAdapter;
import se.greatbrain.sats.adapter.DrawerMenuListener;
import se.greatbrain.sats.event.IonCallCompleteEvent;
import se.greatbrain.sats.event.RefreshEvent;
import se.greatbrain.sats.event.ScrollEvent;
import se.greatbrain.sats.fragment.CalendarColumnFragment;
import se.greatbrain.sats.fragment.TopViewPagerFragment;
import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.ion.IonClient;
import se.greatbrain.sats.model.DrawerMenuItem;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private MenuItem reloadButton;
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

        android.support.v4.app.FragmentManager supportManager = getSupportFragmentManager();

        WorkoutListFragment workoutListFragment = new WorkoutListFragment();
        TopViewPagerFragment topViewPagerFragment = new TopViewPagerFragment();
        supportManager.beginTransaction()
                .add(R.id.top_fragment_container, topViewPagerFragment)
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
        actionBar.setDisplayShowHomeEnabled(true);

        View actionBarView = getLayoutInflater().inflate(R.layout.action_bar_menu, null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);

        // remove left actionbar padding
        android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar)
                actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        reloadButton = menu.findItem(R.id.action_bar_refresh_button);
        setupReloadItemMenu();
        setupSlidingMenu();
        setOnClickHomeButton();

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
                sendOutRefreshEvent();
            }
        }
    }

    private void sendOutRefreshEvent()
    {
        if (reloadButton != null)
        {
            reloadButton.setActionView(null);
            EventBus.getDefault().post(new RefreshEvent());
        }
    }

    private void setupSlidingMenu()
    {
        ListView drawerMenu = (ListView) findViewById(R.id.drawer_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerMenu.setAdapter(new DrawerMenuAdapter(this, populateDrawerList()));
        DrawerMenuListener listener = new DrawerMenuListener(this);
        drawerMenu.setOnItemClickListener(listener);
        drawerLayout.setDrawerListener(listener);
    }

    private List<DrawerMenuItem> populateDrawerList()
    {
        List<DrawerMenuItem> items = new ArrayList<>();
        items.add(new DrawerMenuItem(R.drawable.my_training, "min träning"));
        items.add(new DrawerMenuItem(R.drawable.sats_pin_drawer_menu, "hitta center"));

        return items;
    }

    private void setOnClickHomeButton()
    {
        ImageView menuIcon = (ImageView) findViewById(R.id.btn_dots_logo_sats_menu);
        menuIcon.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        if (drawer.isDrawerOpen(GravityCompat.START))
                        {
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        else
                        {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed()
    {
        if (DrawerMenuListener.wasBackPressed)
        {
            super.onBackPressed();
            DrawerMenuListener.wasBackPressed = false;
        }
        else
        {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
            {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else
            {
                drawerLayout.openDrawer(GravityCompat.START);
                DrawerMenuListener.wasBackPressed = true;
            }
        }
    }
}

