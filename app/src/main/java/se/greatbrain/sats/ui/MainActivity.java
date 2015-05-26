package se.greatbrain.sats.ui;

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
import se.greatbrain.sats.ui.menu.MenuDrawerAdapter;
import se.greatbrain.sats.ui.menu.MenuDrawerListener;
import se.greatbrain.sats.event.IonCallCompleteEvent;
import se.greatbrain.sats.event.MyTrainingRefreshEvent;
import se.greatbrain.sats.data.IonClient;
import se.greatbrain.sats.ui.menu.MenuDrawerItem;
import se.greatbrain.sats.ui.calendar.CalendarFragment;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private MenuItem reloadButton;
    private HashSet<String> finishedIonCalls = new HashSet<>();
    private boolean errorMessageNotShown = true;
    private int numberOfIonErrors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadJsonDataFromWeb();
        EventBus.getDefault().register(this);

        WorkoutListFragment workoutListFragment = new WorkoutListFragment();
        CalendarFragment calendarFragment = new CalendarFragment();
        android.support.v4.app.FragmentManager supportManager = getSupportFragmentManager();

        supportManager.beginTransaction()
                .add(R.id.top_fragment_container, calendarFragment)
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

        // remove actionbar left padding
        android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar)
                actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        reloadButton = menu.findItem(R.id.action_bar_refresh_button);
        setupReloadItemMenu();
        setupDrawerMenu();
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

        if(finishedIonCalls.size() == 0 && numberOfIonErrors < 6)
        {
            reloadButton.setActionView(R.layout.action_bar_reloading);
            ImageView imageView = (ImageView) reloadButton.getActionView()
                    .findViewById(R.id.action_bar_refresh_button_reloading);
            imageView.startAnimation(reloadAnimation);
            numberOfIonErrors = 0;
        }
        else
        {
            numberOfIonErrors = 0;
        }
    }

    public void onEventMainThread(IonCallCompleteEvent event)
    {
        if (event.getSourceEvent().contains("error"))
        {
            numberOfIonErrors++;

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
                postMyTrainingRefreshEvent();
            }
        }
    }

    private void postMyTrainingRefreshEvent()
    {
        if (reloadButton != null)
        {
            reloadButton.setActionView(null);
            EventBus.getDefault().post(new MyTrainingRefreshEvent());
        }
    }

    private void setupDrawerMenu()
    {
        ListView drawerMenu = (ListView) findViewById(R.id.drawer_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerMenu.setAdapter(new MenuDrawerAdapter(this, getDrawerMenuItemList()));
        MenuDrawerListener listener = new MenuDrawerListener(this);
        drawerMenu.setOnItemClickListener(listener);
        drawerLayout.setDrawerListener(listener);
    }

    private List<MenuDrawerItem> getDrawerMenuItemList()
    {
        List<MenuDrawerItem> items = new ArrayList<>();
        items.add(new MenuDrawerItem(R.drawable.my_training, "min träning"));
        items.add(new MenuDrawerItem(R.drawable.sats_pin_drawer_menu, "hitta center"));

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
        if (MenuDrawerListener.wasBackPressed)
        {
            super.onBackPressed();
            MenuDrawerListener.wasBackPressed = false;
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
                MenuDrawerListener.wasBackPressed = true;
            }
        }
    }
}

